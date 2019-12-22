package dao;

import model.User;

public interface IUserDAO {

    User getUserById();
    void registerUser();
    boolean checkIfUserExists(String email, String password);
    boolean isUserAlreadyRegistered(String email);
    void updateUserInfo(User user);


}
