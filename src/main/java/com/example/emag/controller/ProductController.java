package com.example.emag.controller;

import com.example.emag.exceptions.AuthorizationException;
import com.example.emag.exceptions.BadRequestException;
import com.example.emag.exceptions.NotFoundException;
import com.example.emag.model.dao.ProductDAO;
import com.example.emag.model.dto.ProductFilteringDTO;
import com.example.emag.model.pojo.Product;
import com.example.emag.model.pojo.Specification;
import com.example.emag.model.pojo.User;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController extends AbstractController{

    @Autowired
    private ProductDAO productDao;

    //return product with its information OK
    @SneakyThrows
    @GetMapping("/products/{productId}")
    public Product getProduct(@PathVariable(name = "productId") long productId,
                              HttpSession session) {
        Product product = productDao.getProductById(productId);
        checkForProductExistence(product);
        if (product.isDeleted()) {
            throw new BadRequestException("The product is not active!");
        }
        return product;
    }

    //return products by search
    @SneakyThrows
    @PostMapping("/products/search")
    public List<Product> productsFromSearch(@RequestBody ProductFilteringDTO productFilteringDTO,
                                            HttpSession session) {
        //TODO validete input data
        if (productFilteringDTO.getSubCategoryId() == null && productFilteringDTO.getSearchText() == null){
            throw new BadRequestException("You cannot search for products without sub category and search text");
        }
        List<Product> currentProducts = new ArrayList<>();
        if (productFilteringDTO.getSubCategoryId() != null){
            currentProducts = productDao.getProductsBySubCategory(productFilteringDTO.getSubCategoryId(),
                                                                  productFilteringDTO.getColumn(),
                                                                  productFilteringDTO.getMinPrice(),
                                                                  productFilteringDTO.getMaxPrice(),
                                                                  productFilteringDTO.getOrderBy());
        } else if (productFilteringDTO.getSearchText() != null){
            currentProducts = productDao.getProductsFromSearch( productFilteringDTO.getSearchText(),
                                                                productFilteringDTO.getColumn(),
                                                                productFilteringDTO.getMinPrice(),
                                                                productFilteringDTO.getMaxPrice(),
                                                                productFilteringDTO.getOrderBy());
        } else {
            throw new BadRequestException("Can't search without good input information");
        }
        return currentProducts;
    }

    //TODO specification
}
