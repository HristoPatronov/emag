package com.example.emag.model.pojo;

import com.example.emag.model.dto.SpecificationWithProductIdDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Specification {

    private long id;
    private String title;
    private List<Spec> specifications;
    private long productId;

    public Specification(){
        this.specifications = new ArrayList<>();
    }
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Spec{
        private String descTitle;
        private String description;
    }

    public Specification(SpecificationWithProductIdDTO specification){
        setTitle(specification.getTitle());
        setSpecifications(specification.getSpecList());
        setProductId(specification.getProductId());
    }
}
