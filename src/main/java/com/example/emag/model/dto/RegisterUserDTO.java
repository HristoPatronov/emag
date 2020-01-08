package com.example.emag.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDTO {

    private String first_name;
    private String last_name;
    private String password;
    private String confirmPassword;
    private String eMail;
    private boolean subscribed;
}
