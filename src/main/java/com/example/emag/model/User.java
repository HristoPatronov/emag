package com.example.emag.model;


public class User {

    private Integer id;
    private String first_name;
    private String last_name;
    private String userName;
    private String password;
    private String eMail;
    private boolean isAdmin;
    private boolean subscribed;

    public User() {
    }

    public User(String first_name, String last_name, String userName, String password, String eMail) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.userName = userName;
        this.password = password;
        this.eMail = eMail;
    }

    public User(Integer id, String first_name, String last_name, String userName, String password, String eMail, boolean isAdmin) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.userName = userName;
        this.password = password;
        this.eMail = eMail;
        this.isAdmin = isAdmin;
    }

    public User(Integer id, String first_name, String last_name, String userName, String password, String eMail, boolean isAdmin, boolean subscribed) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.userName = userName;
        this.password = password;
        this.eMail = eMail;
        this.isAdmin = isAdmin;
        this.subscribed = subscribed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }
}
