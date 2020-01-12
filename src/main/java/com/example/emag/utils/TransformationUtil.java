package com.example.emag.utils;

import com.example.emag.model.dto.ProductWithQuantityDTO;
import com.example.emag.model.dto.ProductsWithPriceDTO;
import com.example.emag.model.pojo.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransformationUtil {

    public static ProductsWithPriceDTO transformMap(Map<Product, Integer> products){
        double price = 0;
        List<ProductWithQuantityDTO> productsWithQuantityDTO = new ArrayList<>();
        for (Map.Entry<Product, Integer> entry : products.entrySet()){
            ProductWithQuantityDTO productWithQuantityDTO = new ProductWithQuantityDTO();
            productWithQuantityDTO.setProduct(entry.getKey());
            productWithQuantityDTO.setQuantity(entry.getValue());
            productsWithQuantityDTO.add(productWithQuantityDTO);

            Product product = productWithQuantityDTO.getProduct();
            price += round((product.getPrice() * (1 -  ((double)product.getDiscount() / 100)))
                    * productWithQuantityDTO.getQuantity(), 2);
        }
        return new ProductsWithPriceDTO(productsWithQuantityDTO, price);
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
