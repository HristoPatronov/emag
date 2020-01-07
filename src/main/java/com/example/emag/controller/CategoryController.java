package com.example.emag.controller;

import com.example.emag.model.dao.CategoryDAO;
import com.example.emag.model.dao.SubCategoryDAO;
import com.example.emag.model.pojo.Category;
import com.example.emag.model.pojo.SubCategory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController {

    //return categories
    @GetMapping("/category")
    public List<Category> categoryList(){
        List<Category> list = new ArrayList<>();
        try {
            list = CategoryDAO.getInstance().getAllCategories();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    //return subcategories
    @GetMapping("/subCategory")
    public List<SubCategory> subCategoryList(@RequestParam int id){
        List<SubCategory> subCategories = new ArrayList<>();
        try {
            subCategories = SubCategoryDAO.getInstance().getSubCategoryByCategory(id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return subCategories;
    }
}
