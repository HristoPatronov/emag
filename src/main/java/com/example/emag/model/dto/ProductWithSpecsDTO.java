package com.example.emag.model.dto;

import com.example.emag.model.pojo.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductWithSpecsDTO {

    private Product product;
    private List<SpecificationWithProductIdDTO> specifications;
}
