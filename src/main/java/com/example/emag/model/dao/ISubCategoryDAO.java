package com.example.emag.model.dao;

import com.example.emag.model.pojo.SubCategory;

import java.sql.SQLException;
import java.util.List;

public interface ISubCategoryDAO {

    List<SubCategory> getSubCategoryByCategory(long categoryId) throws SQLException;
    SubCategory getSubcategoryById(long subCategoryId) throws SQLException;
}
