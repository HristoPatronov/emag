package com.example.emag.controller;

import com.example.emag.model.dto.SubCategoryDTO;
import com.example.emag.model.pojo.Category;
import com.example.emag.services.CategoryService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController extends AbstractController {

    @Autowired
    private CategoryService categoryUtil;
    //return categories
    @SneakyThrows
    @GetMapping("/categories")
    public List<Category> categoryList() {
        return categoryUtil.categoryList();
    }

    //return subcategories by category id
    @SneakyThrows
    @GetMapping("/subcategories/{categoryId}")
    public List<SubCategoryDTO> subCategoryList(@PathVariable(name="categoryId") long categoryId) {
        return categoryUtil.subCategoryList(categoryId);
    }
}
