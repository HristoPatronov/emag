package com.example.emag.services;

import com.example.emag.exceptions.NotFoundException;
import com.example.emag.model.dao.CategoryDAO;
import com.example.emag.model.dao.SubCategoryDAO;
import com.example.emag.model.dto.SubCategoryDTO;
import com.example.emag.model.pojo.Category;
import com.example.emag.model.pojo.SubCategory;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService extends AbstractService {

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
