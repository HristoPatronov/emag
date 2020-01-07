package com.example.emag.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ProductDto {

    private long id;
    private String name;
    private Double price;
    private Integer discount;
    private Integer stock;
}
