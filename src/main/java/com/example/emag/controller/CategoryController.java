package com.example.emag.controller;

import com.example.emag.exceptions.NotFoundException;
import com.example.emag.model.dao.CategoryDAO;
import com.example.emag.model.dao.SubCategoryDAO;
import com.example.emag.model.dto.SubCategoryDTO;
import com.example.emag.model.pojo.Category;
import com.example.emag.model.pojo.SubCategory;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController extends AbstractController {

    @Autowired
    private CategoryDAO categoryDao;

    @Autowired
    private SubCategoryDAO subCategoryDao;

    //return categories
    @SneakyThrows
    @GetMapping("/categories")
    public List<Category> categoryList() {
        return categoryDao.getAllCategories();
    }

    //return subcategories by category id
    @SneakyThrows
    @GetMapping("/subcategories/{categoryId}")
    public List<SubCategoryDTO> subCategoryList(@PathVariable(name="categoryId") long categoryId) {
        List<SubCategory> subCategories = subCategoryDao.getSubCategoryByCategory(categoryId);
        if (subCategories.isEmpty()) {
            throw new NotFoundException("No such category");
        }
        List<SubCategoryDTO> subCategoriesDto = new ArrayList<>();
        for (SubCategory subCategory : subCategories) {
            subCategoriesDto.add((new SubCategoryDTO(subCategory)));
        }
        return subCategoriesDto;
    }
}
