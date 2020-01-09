package com.example.emag.utils;

public class UserUtil {

    private static final String FIRST_NAME_PATTERN = "([A-Z][a-zA-Z]*).{2,45}";
    private static final String LAST_NAME_PATTERN = "([a-zA-z]+([ '-][a-zA-Z]+)*).{2,45}";
   // private static final String USERNAME_PATTERN = "([A-Za-z0-9_]+).{4,45}";
    private static final String EMAIL_PATTERN = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    private static final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{6,225})";

    //validate firstName
    public static boolean isFirstNameValid(String firstName){
        if (firstName.isEmpty() || firstName.trim().isEmpty()){
            return false;
        } else {
            return firstName.matches(FIRST_NAME_PATTERN);
        }
    }

    //validate lastName
    public static boolean isLastNameValid(String lastName){
        if (lastName.isEmpty() || lastName.trim().isEmpty()){
            return false;
        } else {
            return lastName.matches(LAST_NAME_PATTERN);
        }
    }

    //validate username
//    public static boolean isUsernameValid(String username) {
//        if (username.isEmpty() || username.trim().isEmpty()){
//            return false;
//        } else {
//            return username.matches(USERNAME_PATTERN);
//        }
//    }

    //validate eMail
    public static boolean isEMailValid(String email){
        if (email.isEmpty() || email.trim().isEmpty()){
            return false;
        } else {
            return email.matches(EMAIL_PATTERN);
        }
    }

    //validate password
    public static boolean isPasswordValid(String password){
        if (password.isEmpty() || password.trim().isEmpty()){
            return false;
        } else {
            return password.matches(PASSWORD_PATTERN);
        }
    }
}
