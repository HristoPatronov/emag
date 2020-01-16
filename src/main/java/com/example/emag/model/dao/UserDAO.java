package com.example.emag.model.dao;

import com.example.emag.model.pojo.User;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDAO implements IUserDAO {

    @Override
    public void registerUser(User user) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "INSERT INTO users (first_name, last_name, password, email, subscribed) VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(url, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getFirst_name());
            statement.setString(2, user.getLast_name());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getEMail());
            statement.setBoolean(5, user.isSubscribed());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            user.setId(keys.getLong(1));
        }
    }

    @Override
    public void updateUserInfo(User user) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "UPDATE users SET first_name = ? , last_name = ?, email = ? WHERE id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setString(1, user.getFirst_name());
            statement.setString(2, user.getLast_name());
            statement.setString(3, user.getEMail());
            statement.setLong(4, user.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void changePassword(long id, String newPassword) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "UPDATE users SET password = ? WHERE id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setString(1, newPassword);
            statement.setLong(2, id);
            statement.executeUpdate();
        }
    }

    @Override
    public void changeSubscriptionStatus(long id, boolean subscribed) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "UPDATE users SET subscribed = ? WHERE id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setBoolean(1, subscribed);
            statement.setLong(2, id);
            statement.executeUpdate();
        }
    }

    @Override
    public User getUserByEmail(String email) throws SQLException {
        User user;
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT * FROM users WHERE email = ?;";
        user = null;
        try (PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setString(1, email);
            ResultSet set = statement.executeQuery();
            if (!set.next()) {
                return null;
            }
            user = new User(set.getInt(1),
                    set.getString(2),
                    set.getString(3),
                    set.getString(4),
                    set.getString(5),
                    set.getBoolean(6),
                    set.getBoolean(7));
        }
        return user;
    }

    @Override
    public List<String> getAllSubscribedUsers() throws SQLException {
        List<String> users = new ArrayList<>();
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT email FROM users WHERE subscribed = 1;";
        try (PreparedStatement statement = connection.prepareStatement(url)) {
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                users.add(set.getString("email"));
            }
        }
        return users;
    }
}
