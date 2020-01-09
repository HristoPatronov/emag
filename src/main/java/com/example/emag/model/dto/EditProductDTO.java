package com.example.emag.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditProductDTO {

    private long id;
    private String name;
    private String description;
    private Double price;
    private int discount;
    private int stock;
}
