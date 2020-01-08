package com.example.emag.model.dto;

import com.example.emag.model.pojo.SubCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryDTO {
    private long id;
    private String name;
    private boolean isHeader;

    public SubCategoryDTO(SubCategory subCategory) {
        setId(subCategory.getId());
        setName(subCategory.getName());
        setHeader(subCategory.isHeader());
    }
}
