package com.example.emag.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RegisterUserDto {

    private String first_name;
    private String last_name;
    private String userName;
    private String password;
    private String confirmPassword;
    private String eMail;
    private boolean subscribed;
}
