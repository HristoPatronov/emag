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

@RestController
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
    public List<Product> productsFromSearch(@RequestParam String text, Double min, Double max, String orderBy){
        //check orderBy == ASC/DESC
        //check min/max
        List<Product> currentProducts = new ArrayList<>();
        try {
            currentProducts = ProductDAO.getInstance().getProductsFromSearch(text, min, max, orderBy);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return currentProducts;
    }

    @GetMapping("/productsBySubCategory")
    public List<Product> productsFromSubCategory(@RequestParam int id, Double min, Double max, String orderBy){
        //check orderBy == ASC/DESC
        //check id
        //check min/max
        List<Product> currentProducts = new ArrayList<>();
        try {
            currentProducts = ProductDAO.getInstance().getProductsBySubCategory(id, min, max, orderBy);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return currentProducts;
    }
}
