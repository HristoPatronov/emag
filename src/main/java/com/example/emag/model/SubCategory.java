package com.example.emag.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SubCategory {

    private Integer id;
    private String name;
    private boolean isHeader;
    private Integer categoryId;

    public SubCategory(Integer id, String name, Integer categoryId, boolean isHeader) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.isHeader = isHeader;
    }
}
