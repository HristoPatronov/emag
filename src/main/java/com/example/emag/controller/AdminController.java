package com.example.emag.controller;

import com.example.emag.model.dto.EditProductDTO;
import com.example.emag.model.dto.ProductWithSpecsDTO;
import com.example.emag.model.pojo.Product;
import com.example.emag.model.pojo.User;
import com.example.emag.services.AdminService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static com.example.emag.utils.UserUtil.SESSION_KEY_LOGGED_USER;

@RestController
public class AdminController extends AbstractController{

    @Autowired
    private AdminService adminService;

    //add product
    @SneakyThrows
    @PostMapping("/admin/products")
    public ProductWithSpecsDTO addProduct(@RequestBody ProductWithSpecsDTO productDto,
                              HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        return adminService.addProduct(productDto, user);
    }

    //remove product by ID
    @SneakyThrows
    @DeleteMapping("/admin/products/{productId}")
    public Product removeProduct(@PathVariable(name="productId") long productId,
                                  HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        return adminService.removeProduct(productId, user);
    }

    //edit product
    @SneakyThrows
    @PutMapping("admin/products")
    public Product editProduct(@RequestBody EditProductDTO editProductDTO,
                               HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        return adminService.editProduct(editProductDTO, user);
    }
}
