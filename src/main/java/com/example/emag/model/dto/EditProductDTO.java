package com.example.emag.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditProductDTO {

    private String name;
    private String description;
    private Double price;
    private int discount;
    private int stock;
}
