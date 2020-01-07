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
public class Category {

    @JsonIgnore
    private long id;
    private String name;

    public Category(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
