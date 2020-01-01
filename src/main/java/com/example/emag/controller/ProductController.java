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

    private List<Product> currentProducts = new ArrayList<>();

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
        try {
            this.currentProducts = ProductDAO.getInstance().getProductsFromSearch(text);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return currentProducts;
    }

    @GetMapping("/productsBySubCategory")
    public List<Product> productsFromSubCategory(@RequestParam int id){
        try {
            this.currentProducts = ProductDAO.getInstance().getProductsBySubCategory(id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return this.currentProducts;
    }

    @GetMapping("/orderBy")
    public List<Product> products(@RequestParam int order){
            switch (order){
                case 0:
                    this.currentProducts.sort((o1, o2) -> Double.compare(o2.getPrice(), o1.getPrice()));
                break;
                case 1:
                    this.currentProducts.sort((o1, o2) -> Double.compare(o1.getPrice(), o2.getPrice()));
                break;

                default:
                    break;
            }
            return this.currentProducts;
    }

    @GetMapping("/between")
    public List<Product> between(@RequestParam int min, @RequestParam int max){
        List<Product> newList = new ArrayList<>();
        for (Product product : this.currentProducts){
            if (product.getPrice() >= min && product.getPrice() <= max){
                newList.add(product);
            }
        }
        return newList;
    }
    //TODO how to connect between min max and order by asc/desc
}
