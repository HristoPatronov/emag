package com.example.emag.model.dto;

import com.example.emag.model.pojo.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductForReviewsDTO {

    private long id;
    private String name;

    public ProductForReviewsDTO(Product product) {
        setId(product.getId());
        setName(product.getName());
    }
}
