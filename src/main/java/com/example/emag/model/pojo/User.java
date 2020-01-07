package com.example.emag.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class User {

    private long id;
    private String first_name;
    private String last_name;
    private String userName;
    @JsonIgnore
    private String password;
    private String eMail;
    private boolean admin;
    private boolean subscribed;

    public User(String first_name, String last_name, String userName, String password, String eMail) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.userName = userName;
        this.password = password;
        this.eMail = eMail;
    }

    public User(String first_name, String last_name, String userName, String password, String eMail, boolean subscribed) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.userName = userName;
        this.password = password;
        this.eMail = eMail;
        this.subscribed = subscribed;
    }

    public User(Integer id, String first_name, String last_name, String userName, String password, String eMail, boolean admin) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.userName = userName;
        this.password = password;
        this.eMail = eMail;
        this.admin = admin;
    }

    public User(Integer id, String first_name, String last_name, String userName, String password, String eMail, boolean admin, boolean subscribed) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.userName = userName;
        this.password = password;
        this.eMail = eMail;
        this.admin = admin;
        this.subscribed = subscribed;
    }
}
