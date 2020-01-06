package com.example.emag.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public class Product {

    @JsonIgnore
    private Integer id;
    private String name;
    private String description;
    private Double price;
    private Integer discount;
    private Integer stock;
    @JsonIgnore
    private Integer reservedQuantity;
    @JsonIgnore
    private Integer subCategoryId;


    //DTO for getProductsByOrder -- ProductDAO getProductsByOrder line 213
    public Product(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public Product(String name, String description, Double price, Integer discount, Integer stock, Integer reservedQuantity, Integer subCategoryId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.stock = stock;
        this.reservedQuantity = reservedQuantity;
        this.subCategoryId = subCategoryId;
    }

    public Product(Integer id, String name, String description, Double price, Integer discount, Integer stock, Integer reservedQuantity, Integer subCategoryId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.stock = stock;
        this.reservedQuantity = reservedQuantity;
        this.subCategoryId = subCategoryId;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", stock=" + stock +
                ", reservedQuantity=" + reservedQuantity +
                ", subCategoryId=" + subCategoryId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
