package com.example.emag.model.dto;

import com.example.emag.model.pojo.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderWithProductsDTO {

    private Order order;
    private List<ProductWithQuantityDTO> products;


}
