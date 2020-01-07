package com.example.emag.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Product {

    private long id;
    private String name;
    private String description;
    private Double price;
    private int discount;
    private int stock;
    @JsonIgnore
    private int reservedQuantity;
    private SubCategory subCategory;


    //DTO for getProductsByOrder -- ProductDAO getProductsByOrder line 213


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
                ", subCategoryId=" + subCategory +
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
