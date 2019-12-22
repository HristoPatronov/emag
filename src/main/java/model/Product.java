package model;

import java.util.Collection;

public class Product {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer discount;
    private Integer stock;
    private Long specificationId;
    private Long subCategoryId;
    private Collection<Review> reviews;

}
