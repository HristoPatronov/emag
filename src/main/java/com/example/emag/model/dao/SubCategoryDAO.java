package com.example.emag.model.dao;

import com.example.emag.model.pojo.Category;
import com.example.emag.model.pojo.SubCategory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SubCategoryDAO implements ISubCategoryDAO {

    @Override
    public List<SubCategory> getSubCategoryByCategory(long categoryId) throws SQLException {
        List<SubCategory> subCategories = new ArrayList<>();
        try (Connection connection = DBManager.getInstance().getConnection()) {
            String url = "SELECT sc.*, c.* FROM sub_categories AS sc " +
                    "JOIN categories AS c ON sc.category_id = c.id WHERE sc.category_id = ?;";
            SubCategory subCategory = null;
            try (PreparedStatement statement = connection.prepareStatement(url)) {
                statement.setLong(1, categoryId);
                ResultSet set = statement.executeQuery();
                while (set.next()) {
                    subCategory = new SubCategory(set.getLong(1),
                            set.getString(2),
                            set.getBoolean(3),
                            new Category(set.getLong(5), set.getString(6)));
                    subCategories.add(subCategory);
                }
            }
        }
        return subCategories;
    }

    @Override
    public SubCategory getSubcategoryById(long subCategoryId) throws SQLException {
        SubCategory subCategory;
        try (Connection connection = DBManager.getInstance().getConnection()) {
            String url = "SELECT sc.*, c.* FROM sub_categories AS sc " +
                    "JOIN categories AS c ON sc.category_id = c.id WHERE sc.id = ?;";
            subCategory = null;
            try (PreparedStatement statement = connection.prepareStatement(url)) {
                statement.setLong(1, subCategoryId);
                ResultSet set = statement.executeQuery();
                if (!set.next()) {
                    return null;
                }
                subCategory = new SubCategory(set.getLong(1),
                        set.getString(2),
                        set.getBoolean(3),
                        new Category(set.getLong(5),
                                set.getString(6)));
            }
        }
        return subCategory;
    }
}
