package com.example.emag.model.dto;

import com.example.emag.model.pojo.Specification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecificationWithProductIdDTO {

    private Long productId;
    private String title;
    private List<Specification.Spec> specList;

    public boolean checkNull() throws IllegalAccessException {
        for (Field f : getClass().getDeclaredFields()) {
            if (f.get(this) != null) {
                return false;
            }
        }
        return true;
    }
}
