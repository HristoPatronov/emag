package dao;

import model.Product;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO implements IUserDAO {
    @Override
    public User getUserById(Integer id) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT * FROM users WHERE id = ?;";
        User user = null;
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
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
        String url = "INSERT INTO users (first_name, last_name, username, password, e-mail, is_admin) VALUES (?, ?, ?, ?, ?, ?);";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setString(1, user.getFirst_name());
            statement.setString(2, user.getLast_name());
            statement.setString(3, user.getUserName());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.geteMail());
            statement.setBoolean(6, user.isAdmin());
            statement.executeUpdate();
        }
    }

    @Override
    public boolean checkIfUserExists(String email, String password) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT id FROM users WHERE email = ? AND password = ?;";
        ResultSet set = null;
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setString(1, email);
            statement.setString(2, password);
            set = statement.executeQuery();
        }
        return set != null;
    }

    @Override
    public boolean checkIfUserExists(String email) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT id FROM users WHERE email = ?;";
        ResultSet set = null;
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setString(1, email);
            set = statement.executeQuery();
        }
        return set != null;
    }

    @Override
    public void updateUserInfo(User user) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "UPDATE users SET first_name = ? , last_name = ?, username = ?, password = ?, e-mail = ? WHERE id = ?;";
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
}
