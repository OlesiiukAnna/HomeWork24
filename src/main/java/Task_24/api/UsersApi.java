package Task_24.api;
/*
Создать API для работы с классом User (int id, String name, int age):
POST: создать нового юзера. Передать на сервер в формате x-www-form-urlencoded name и age.
Сервер должен вернуть JSON с созданным юзером у которого присвоен id.
PUT: изменить юзера по id
DELETE: удалить
GET: получить юзера по id
GET: получить всех юзеров

Каждый метод должен возвращать код возврата и ответ в формате JSON с обьянением результата.
Непример если DELETE запрос прошел успешно, то код возврата будет 200 и соотв.
сообщение а если была попытка удалить юзера которого нет, то вернуть код 400 и сооттв. текст.
На сервер хранить users в любом удобном виде - просто в памяти или в БД.
 */

import Task_24.Dao.BaseDao;
import Task_24.Dao.impl.UserDaoImpl;
import Task_24.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Path("/")
public class UsersApi {

    private BaseDao userDao = new UserDaoImpl();
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public UsersApi() throws SQLException {
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createNewUser(@FormParam("name") String name,
                                  @FormParam("age") int age) {
        User newUser = new User(name, age);
        userDao.createNewUser(newUser);
        LinkedList<User> users = new LinkedList<>(userDao.getAllUsers());
        String result = gson.toJson(users.getLast());
        return Response
                .status(Response.Status.OK)
                .entity(result)
                .build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateUserById(@FormParam("id") int id,
                                   @FormParam("name") String name,
                                   @FormParam("age") int age) {
        boolean isUpdated = userDao.updateUserById(new User(id, name, age));
        if (isUpdated) {
            return Response
                    .status(Response.Status.OK)
                    .entity("User was updated")
                    .build();
        }
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("User with such id not found")
                .build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response deleteUser(@FormParam("id") int id) {
        boolean isDeleted = userDao.deleteUser(id);
        if (isDeleted) {
            return Response
                    .status(Response.Status.OK)
                    .entity(isDeleted)
                    .build();
        }
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("User with such id not found")
                .build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") int id) {
        User user = userDao.getUserById(id);
        if (user != null) {
            String result = gson.toJson(user);
            return Response
                    .status(Response.Status.OK)
                    .entity(result)
                    .build();
        }
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("User with such id not found")
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        List<User> users = userDao.getAllUsers();
        String result = gson.toJson(users);
        return Response
                .status(Response.Status.OK)
                .entity(result)
                .build();
    }
}
