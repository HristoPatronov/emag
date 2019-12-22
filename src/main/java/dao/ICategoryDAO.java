package dao;

import model.Category;

import java.util.Collection;

public interface ICategoryDAO {

    Collection<Category> getAllCategories();
}
