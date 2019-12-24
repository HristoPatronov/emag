package dao;

import model.Category;
import model.SubCategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO implements ICategoryDAO {

    @Override
    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT * FROM categories;";
        Category category = null;
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            ResultSet set = statement.executeQuery();
            category = new Category(set.getInt(1),
                                    set.getString(2));
            categories.add(category);
        }
        return categories;
    }
}
