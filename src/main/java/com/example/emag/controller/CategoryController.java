package com.example.emag.controller;

import com.example.emag.exceptions.BadRequestException;
import com.example.emag.exceptions.NotFoundException;
import com.example.emag.model.dao.CategoryDAO;
import com.example.emag.model.dao.SubCategoryDAO;
import com.example.emag.model.pojo.Category;
import com.example.emag.model.pojo.SubCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController extends AbstractController {

    @Autowired
    private CategoryDAO categoryDao;

    @Autowired
    private SubCategoryDAO subCategoryDao;

    //return categories
    @GetMapping("/categories")
    public List<Category> categoryList() throws SQLException{
        return categoryDao.getAllCategories();
    }

    //return subcategories
    @GetMapping("/subcategories/{categoryId}")
    public List<SubCategory> subCategoryList(@PathVariable(name = "categoryId") long categoryId) throws SQLException {
        List<SubCategory> subCategories = subCategoryDao.getSubCategoryByCategory(categoryId);
        if (subCategories == null) {
            throw new NotFoundException("No such category");
        }
        return subCategories;
    }
}
