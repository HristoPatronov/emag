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
import java.util.List;

@RestController
public class ProductController extends AbstractController{

    @Autowired
    private ProductDAO productDao;

    //return product with its information OK
    @GetMapping("/products/{productId}")
    public Product getProduct(@PathVariable(name = "productId") long productId) throws SQLException {
        Product product = productDao.getProductById(productId);
        if (product == null) {
            throw new NotFoundException("Product not found");
        }
        return product;
    }

    //return products by search TODO
    @GetMapping("/products/search/{text}/min/{min}/max/{max}/order/{orderBy}")
    public List<Product> productsFromSearch(@PathVariable(name="text") String text,
                                            @PathVariable(name="min") double min,
                                            @PathVariable(name="max") double max,
                                            @PathVariable(name="orderBy") String orderBy) throws SQLException {
        //TODO validete input data
        List<Product> currentProducts = productDao.getProductsFromSearch(text, min, max, orderBy);
        return currentProducts;
    }

    //@GetMapping("/products/search/{text}
    //@GetMapping("/products/search/{text}/order/{orderBy}")
    //@GetMapping("/products/search/{text}/min/{min}/max/{max}/
    //@GetMapping("/products/search/{text}/min/{min}/max/{max}/order/{orderBy}")

    //@GetMapping("/products/subcategory/{subcategory}")
    //@GetMapping("/products/subcategory/{subcategory}/order/{orderBy}")
    //@GetMapping("/products/subcategory/{subcategory}/min/{min}/max/{max}")
    //@GetMapping("/products/subcategory/{subcategory}/min/{min}/max/{max}/order/{orderBy}")
//
//    //TODO
//    @GetMapping("/products/{subcategory}")
//    public List<Product> productsFromSubCategory(@RequestParam int id, Double min, Double max, String orderBy) throws SQLException {
//        //check orderBy == ASC/DESC
//        //check id
//        //check min/max
//        List<Product> currentProducts = productDao.getProductsBySubCategory(id, min, max, orderBy);
//        return currentProducts;
//    }
}
