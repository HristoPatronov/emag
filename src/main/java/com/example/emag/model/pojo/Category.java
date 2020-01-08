package com.example.emag.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    private long id;
    private String name;

    public Category(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
