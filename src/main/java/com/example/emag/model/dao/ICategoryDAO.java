package com.example.emag.model.dao;

import com.example.emag.model.pojo.Category;

import java.sql.SQLException;
import java.util.List;

public interface ICategoryDAO {

    List<Category> getAllCategories() throws SQLException;
}
