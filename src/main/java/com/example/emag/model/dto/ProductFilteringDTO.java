package com.example.emag.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilteringDTO {

    private Long subCategoryId;
    private String column;
    private String searchText;
    private Double minPrice;
    private Double maxPrice;
    private String orderBy;
}
