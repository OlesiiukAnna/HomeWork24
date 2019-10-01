package Task_24.Dao.impl;

import Task_24.Const;
import Task_24.Dao.BaseDao;
import Task_24.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements BaseDao {
    private Connection connection;

    private static final String SQL_INSERT_NEW_USER = "INSERT INTO users(name, age) VALUES(?, ?)";
    private static final String SQL_UPDATE_USER_BY_ID = "UPDATE users SET name = ? , age = ? WHERE id = ?";
    private static final String SQL_GET_USER_BY_ID = "SELECT id, name, age FROM users WHERE id = ?";
    private static final String SQL_GET_ALL_USERS = "SELECT id, name, age FROM users";
    private static final String SQL_DELETE_USER_BY_ID = "DELETE FROM users WHERE id=?";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public UserDaoImpl() throws SQLException {
        connection = DriverManager.getConnection(Const.JDBC_URL, Const.USER, Const.PASSWORD);
        maybeCreateUsersTable();
    }

    private void maybeCreateUsersTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String request = "CREATE TABLE IF NOT EXISTS users \n" +
                    "(id SERIAL, \n" +
                    "name varchar(100),\n" +
                    "age int, PRIMARY KEY (id));";
            statement.execute(request);
        }
    }

    @Override
    public boolean createNewUser(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_NEW_USER);
            statement.setString(1, user.getName());
            statement.setInt(2, user.getAge());
            statement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateUserById(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_USER_BY_ID);
            statement.setString(1, user.getName());
            statement.setInt(2, user.getAge());
            statement.setInt(3, user.getId());
            int result = statement.executeUpdate();
            if (result > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteUser(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE_USER_BY_ID);
            statement.setInt(1, id);
            int result = statement.executeUpdate();
            if (result > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public User getUserById(int id) {
        User user = null;
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_GET_USER_BY_ID);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            int userID = 0;
            while (resultSet.next() && id != userID) {
                userID = resultSet.getInt(1);
                String name = resultSet.getString(2);
                int age = resultSet.getInt(3);
                user = new User(userID, name, age);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SQL_GET_ALL_USERS);
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                int age = resultSet.getInt(3);
                users.add(new User(id, name, age));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
