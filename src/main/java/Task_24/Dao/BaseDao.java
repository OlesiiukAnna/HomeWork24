package Task_24.Dao;

import Task_24.model.User;

import java.util.List;

public interface BaseDao {

    boolean createNewUser(User user);

    boolean updateUserById(User user);

    boolean deleteUser(int id);

    User getUserById(int id);

    List<User> getAllUsers();
}
