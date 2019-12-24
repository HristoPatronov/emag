package dao;

import model.SubCategory;

import java.sql.SQLException;
import java.util.List;

public interface ISubCategoryDAO {

    List<SubCategory> getSubCategoryByCategory(Integer categoryId) throws SQLException;
}
