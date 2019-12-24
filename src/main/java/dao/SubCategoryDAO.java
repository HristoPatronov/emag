package dao;

import model.SubCategory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubCategoryDAO implements ISubCategoryDAO {

    @Override
    public List<SubCategory> getSubCategoryByCategory(Integer categoryId) throws SQLException {
        List<SubCategory> subCategories = new ArrayList<>();
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT * FROM sub_categories WHERE id = ?;";
        SubCategory subCategory = null;
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setInt(1, categoryId);
            ResultSet set = statement.executeQuery();
            subCategory = new SubCategory(set.getInt(1),
                    set.getString(2),
                    set.getInt(3),
                    set.getBoolean(4));
            subCategories.add(subCategory);
        }
        return subCategories;
    }
}
