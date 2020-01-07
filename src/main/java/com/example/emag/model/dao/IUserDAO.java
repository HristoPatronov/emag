package com.example.emag.model.dao;

import com.example.emag.model.pojo.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserDAO {

    User getUserById(Integer id) throws SQLException;
    void registerUser(User user) throws SQLException;
    boolean checkIfUserExists(String email, String password) throws SQLException;
    boolean checkIfUserExists(String email) throws SQLException;
    void updateUserInfo(User user) throws SQLException;
    User getUserByEmail(String email) throws SQLException;
    boolean isAdminByUserId(Integer id) throws SQLException;
    List<String> getAllSubscribedUsers() throws SQLException;
    void changePassword(Integer id, String newPassword) throws SQLException;
    void changeSubscriptionStatus(Integer id, boolean subscribed) throws SQLException;


}
