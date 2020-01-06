package com.example.emag.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SubCategory {

    @JsonIgnore
    private Integer id;
    private String name;
    private boolean isHeader;
    @JsonIgnore
    private Integer categoryId;

    public SubCategory(Integer id, String name,  boolean isHeader, Integer categoryId) {
        this.id = id;
        this.name = name;
        this.isHeader = isHeader;
        this.categoryId = categoryId;

    }
}
