package com.example.emag.model;

import java.util.HashMap;

public class Specification {

    private Integer id;
    private Integer productId;
    private HashMap<String, HashMap<String, String>> description;
                    //title,        descTitle, desc

    public Specification(Integer id, Integer productId) {
        this.id = id;
        this.productId = productId;
        this.description = new HashMap<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public HashMap<String, HashMap<String, String>> getDescription() {
        return description;
    }
}
