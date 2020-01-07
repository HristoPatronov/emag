package com.example.emag.model.dto;

import com.example.emag.model.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class UserForReviewsDTO {

    private long id;
    private String first_name;
    private String last_name;

    public UserForReviewsDTO(User user) {
        setId(user.getId());
        setFirst_name(user.getFirst_name());
        setLast_name(user.getLast_name());
    }
}
