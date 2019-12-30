package com.example.emag.dao;

import com.example.emag.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO implements IUserDAO {


    private static UserDAO mInstance;

    private UserDAO() {
    }

    public static UserDAO getInstance() {
        if (mInstance == null) {
            mInstance = new UserDAO();
        }
        return mInstance;
    }

    @Override
    public User getUserById(Integer id) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT * FROM users WHERE id = ?;";
        User user = null;
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            set.next();
            user = new User(set.getInt(1),
                    set.getString(2),
                    set.getString(3),
                    set.getString(4),
                    set.getString(5),
                    set.getString(6),
                    set.getBoolean(7),
                    set.getBoolean(8));
        }
        return user;
    }

    @Override
    public void registerUser(User user) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "INSERT INTO users (first_name, last_name, username, password, email) VALUES (?, ?, ?, ?, ?);";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setString(1, user.getFirst_name());
            statement.setString(2, user.getLast_name());
            statement.setString(3, user.getUserName());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.geteMail());
            statement.executeUpdate();
        }
    }

    @Override
    public boolean checkIfUserExists(String email, String password) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT id FROM users WHERE email = ? AND password = ?;";
        boolean exist = false;
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet set = statement.executeQuery();
            exist = set.next();
        }
        return exist;
    }

    @Override
    public boolean checkIfUserExists(String email) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT id FROM users WHERE email = ?;";
        boolean exist = false;
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setString(1, email);
            ResultSet set = statement.executeQuery();
            exist = set.next();
        }
        return exist;
    }

    @Override
    public void updateUserInfo(User user) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "UPDATE users SET first_name = ? , last_name = ?, username = ?, password = ?, email = ? WHERE id = ?;";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setString(1,user.getFirst_name());
            statement.setString(2,user.getLast_name());
            statement.setString(3,user.getUserName());
            statement.setString(4,user.getPassword());
            statement.setString(5,user.geteMail());
            statement.setInt(6, user.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public User getUserByEmail(String email) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT * FROM users WHERE email = ?;";
        User user = null;
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setString(1, email);
            ResultSet set = statement.executeQuery();
            if (!set.next()){
                return null;
            }
            user = new User(set.getInt(1),
                    set.getString(2),
                    set.getString(3),
                    set.getString(4),
                    set.getString(5),
                    set.getString(6),
                    set.getBoolean(7),
                    set.getBoolean(8));
        }
        return user;
    }

    @Override
    public boolean isAdminByUserId(Integer id) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT is_admin FROM users WHERE id = ?;";
        boolean isAdmin = false;
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            set.next();
            isAdmin = set.getBoolean("is_admin");
        }
        return isAdmin;
    }
}
