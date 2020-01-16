package com.example.emag.model.dao;

import com.example.emag.model.pojo.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserDAO {

    void registerUser(User user) throws SQLException;
    void updateUserInfo(User user) throws SQLException;
    User getUserByEmail(String email) throws SQLException;
    List<String> getAllSubscribedUsers() throws SQLException;
    void changePassword(long id, String newPassword) throws SQLException;
    void changeSubscriptionStatus(long id, boolean subscribed) throws SQLException;
}
