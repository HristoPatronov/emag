package com.example.emag.controller;

import com.example.emag.exceptions.NotFoundException;
import com.example.emag.model.dao.ProductDAO;
import com.example.emag.model.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController extends AbstractController{

    @Autowired
    private ProductDAO productDao;

    //return product with its information
    @GetMapping("/products/{productId}")
    public Product getProduct(@PathVariable(name = "productId") long productId) throws SQLException {
        Product product = productDao.getProductById(productId);
        if (product == null) {
            throw new NotFoundException("Product not found");
        }
        return product;
    }

    //return products by search TODO
    @GetMapping("/products/{text}")
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

    //TODO
    @GetMapping("/products/{subcategory}")
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
