package com.example.emag.controller;

import com.example.emag.dao.ProductDAO;
import com.example.emag.model.Product;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {


    //return product with its information
    @GetMapping("/product")
    public Product getProduct(@RequestParam int id){
        Product product = null;
        try {
            product = ProductDAO.getInstance().getProductById(id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return product;
    }

    //return products by search
    @GetMapping("/search")
    public List<Product> productsFromSearch(@RequestParam String text){
        List<Product> list = new ArrayList<>();
        try {
            list = ProductDAO.getInstance().getProductsFromSearch(text);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
}
