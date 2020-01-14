package com.example.emag.controller;

import com.example.emag.model.dto.ProductFilteringDTO;
import com.example.emag.model.dto.ProductWithAllDTO;
import com.example.emag.model.pojo.Product;
import com.example.emag.services.ProductService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class ProductController extends AbstractController{

    @Autowired
    private ProductService productUtil;

    @Autowired
    private RenderController renderController;

    //return product with its information OK
    @SneakyThrows
    @GetMapping("/products/{productId}")
    public ProductWithAllDTO getProduct(@PathVariable(name = "productId") long productId) {
        return productUtil.getProduct(productId);
    }

    //return products by search
    @SneakyThrows
    @PostMapping("/products/search")
    public List<Product> productsFromSearch(@RequestBody ProductFilteringDTO productFilteringDTO) {
        return productUtil.productsFromSearch(productFilteringDTO);
    }

}
