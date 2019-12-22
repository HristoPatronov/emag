package dao;

import model.Category;
import model.SubCategory;

import java.util.Collection;

public interface ISubCategoryDAO {

    Collection<SubCategory> getSubCategoryByCategory(Long categoryId);
}
