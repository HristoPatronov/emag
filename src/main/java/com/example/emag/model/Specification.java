package com.example.emag.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@NoArgsConstructor
@Getter
@Setter
public class Specification {

    @JsonIgnore
    private Integer id;
    @JsonIgnore
    private Integer productId;
    private HashMap<String, HashMap<String, String>> description;

    public Specification(Integer id, Integer productId) {
        this.id = id;
        this.productId = productId;
        this.description = new HashMap<>();
    }
}
