package com.example.emag.model.dao;

import com.example.emag.model.pojo.Product;
import com.example.emag.model.pojo.Specification;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IProductDAO {

    List<Product> getProductsBySubCategory(Long subCategoryId, String column, Double minPrice,
                                           Double maxPrice, String orderBy) throws SQLException;
    Product getProductById(long id) throws SQLException;
    List<Product> getProductsFromSearch(String searchInput, String column, Double minPrice,
                                        Double maxPrice, String orderBy) throws SQLException;
    void addProduct(Product product) throws SQLException;
    void removeProduct(long productId) throws SQLException;
    void editProduct(Product product) throws SQLException;
    List<Product> getFavouriteProducts(long userId) throws SQLException;
    Map<Product, Integer> getProductsByOrder(long orderId) throws SQLException;
    void addFavouriteProduct(long userId, long productId) throws SQLException;
    void removeFavouriteProduct(long userId, long productId) throws SQLException;
    void addProductsToOrder(Map<Product, Integer> products, long orderId) throws SQLException;
    void removeProducts(Map<Product, Integer> products) throws SQLException;
    void addReservedQuantity(long productId, Integer quantity) throws SQLException;
    void removeReservedQuantity(long productId, Integer quantity) throws SQLException;
    void removeReservedQuantities(Map<Product, Integer> products) throws SQLException;
}
