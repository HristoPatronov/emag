package com.example.emag.controller;

import com.example.emag.exceptions.AuthorizationException;
import com.example.emag.exceptions.NotFoundException;
import com.example.emag.model.dao.ProductDAO;
import com.example.emag.model.dao.SubCategoryDAO;
import com.example.emag.model.dao.UserDAO;
import com.example.emag.model.dto.EditProductDTO;
import com.example.emag.model.dto.ProductFilteringDTO;
import com.example.emag.model.pojo.Product;
import com.example.emag.model.pojo.SubCategory;
import com.example.emag.model.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@RestController
public class AdminController extends AbstractController{

    @Autowired
    private ProductDAO productDao;
    @Autowired
    private UserDAO userDao;
    @Autowired
    private SubCategoryDAO subCategoryDao;

    //add product
    @PostMapping("/admin/products")
    public Product addProduct(@RequestBody Product product, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException();
        }
        if(!user.isAdmin()) {
            throw new AuthorizationException("You need to be admin to perform this!");
        }
        //TODO validate product
        productDao.addProduct(product);
        //product.setSubCategory(subCategoryDao.getSubCategoryById(product.getSubCategory().getId()));
        return product;
    }

    //remove product by ID
    @DeleteMapping("/admin/products/{productId}")
    public Product removeProduct(@PathVariable(name="productId") long productId, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException();
        }
        if(!user.isAdmin()) {
            throw new AuthorizationException("You need to be admin to perform this!");
        }
        Product product = productDao.getProductById(productId);
        if (product == null) {
            throw new NotFoundException("Product not found!");
        }
        productDao.removeProduct(productId);
        return product;
    }

    @PutMapping("admin/products/edit/{productId}")
    public Product editProduct(@PathVariable(name = "productId")long productId, @RequestBody EditProductDTO editProductDTO, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException();
        }
        if(!user.isAdmin()) {
            throw new AuthorizationException("You need to be admin to perform this!");
        }
        //TODO validate
        Product fetchedProduct = productDao.getProductById(productId);
        fetchedProduct.setName(editProductDTO.getName());
        fetchedProduct.setDescription(editProductDTO.getDescription());
        fetchedProduct.setPrice(editProductDTO.getPrice());
        fetchedProduct.setDiscount(editProductDTO.getDiscount());
        fetchedProduct.setStock(editProductDTO.getStock());
        productDao.editProduct(fetchedProduct);
        return fetchedProduct;
    }
}
