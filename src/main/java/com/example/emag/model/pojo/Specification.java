package com.example.emag.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Specification {

    private long id;
    private Product product;
    private HashMap<String, HashMap<String, String>> description;

}
