package com.example.emag.model.dto;

import com.example.emag.model.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserWithoutPasswordDTO {

    private long id;
    private String first_name;
    private String last_name;
    private String eMail;
    private boolean subscribed;

    public UserWithoutPasswordDTO(User user) {
        setId(user.getId());
        setFirst_name(user.getFirst_name());
        setLast_name(user.getLast_name());
        setEMail(user.getEMail());
        setSubscribed(user.isSubscribed());
    }
}
