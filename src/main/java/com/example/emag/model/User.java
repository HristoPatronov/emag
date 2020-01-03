package com.example.emag.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class User {
    @JsonIgnore
    private Integer id;
    private String first_name;
    private String last_name;
    private String userName;
    //perhaps should be ignored also
    @JsonIgnore
    private String password;
    private String eMail;
    @JsonIgnore
    private boolean isAdmin;
    @JsonIgnore
    private boolean subscribed;

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
}
