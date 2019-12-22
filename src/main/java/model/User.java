package model;

import java.util.Collection;

public class User {

    private Long id;
    private String first_name;
    private String last_name;
    private String userName;
    private String password;
    private String eMail;
    private boolean isAdmin;
    private ShoppingCart shoppingCart;
    private boolean isLogged;
    private boolean isActivated;
    private Collection<Address> addresses;

}
