package com.example.emag.model.pojo;

import com.example.emag.model.dto.RegisterUserDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private long id;
    private String first_name;
    private String last_name;
    @JsonIgnore
    private String password;
    private String eMail;
    private boolean admin;
    private boolean subscribed;

    public User(RegisterUserDTO dto) {
        setFirst_name(dto.getFirst_name());
        setLast_name(dto.getLast_name());
        setPassword(dto.getPassword());  //TODO BCRYPT!
        setEMail(dto.getEMail());
        setSubscribed(dto.isSubscribed());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(eMail, user.eMail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eMail);
    }
}
