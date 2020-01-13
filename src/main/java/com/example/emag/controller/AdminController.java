package com.example.emag.controller;

import com.example.emag.model.dto.EditProductDTO;
import com.example.emag.model.dto.ProductWithSpecsDTO;
import com.example.emag.model.pojo.Product;
import com.example.emag.services.AdminService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class AdminController extends AbstractController{

    @Autowired
    private AdminService adminUtil;

    //add product
    @SneakyThrows
    @PostMapping("/admin/products")
    public ProductWithSpecsDTO addProduct(@RequestBody ProductWithSpecsDTO productDto,
                              HttpSession session) {
        return adminUtil.addProduct(productDto, session);
    }

    //remove product by ID
    @SneakyThrows
    @DeleteMapping("/admin/products/{productId}")
    public Product removeProduct(@PathVariable(name="productId") long productId,
                                  HttpSession session) {
        return adminUtil.removeProduct(productId, session);
    }

    //edit product
    @SneakyThrows
    @PutMapping("admin/products")
    public Product editProduct(@RequestBody EditProductDTO editProductDTO,
                               HttpSession session) {
        return adminUtil.editProduct(editProductDTO, session);
    }
}
