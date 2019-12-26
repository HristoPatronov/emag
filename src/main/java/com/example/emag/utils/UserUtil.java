package com.example.emag.utils;

public class UserUtil {

    //static methods
    //validate username

    public static boolean isUsernameValid(String username) {
        if(username.isEmpty() || username.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    //validate password
}
