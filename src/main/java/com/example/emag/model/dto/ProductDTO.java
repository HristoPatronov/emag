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
public class ProductDTO {

    private long id;
    private String name;
    private String description;
    private Double price;
    private Integer discount;
    private Integer stock;
    private Long subCategoryId;

    public ProductDTO(Product product) {
        setId(product.getId());
        setName(product.getName());
        setPrice(product.getPrice());
        setDiscount(product.getDiscount());
        setStock(product.getStock());
        setSubCategoryId(product.getSubCategory().getId());
    }
}
