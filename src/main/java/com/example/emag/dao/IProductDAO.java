package com.example.emag.dao;

import com.example.emag.model.Order;
import com.example.emag.model.Product;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IProductDAO {

    List<Product> getAllProducts() throws SQLException;
    List<Product> getProductsBySubCategory(Integer subCategoryId, Double minPrice, Double maxPrice, String orderBy) throws SQLException;
    Product getProductById(Integer id) throws SQLException;
    List<Product> getProductsFromSearch(String searchInput, Double minPrice, Double maxPrice, String orderBy) throws SQLException;
    void addProduct(Product product) throws SQLException;  //ADMIN STUFF
    void removeProduct(Integer productId) throws SQLException; //ADMIN STUFF
    void updateQuantity(Integer productId, Integer quantity) throws SQLException; //ADMIN STUFF
    void setDiscount(Integer productId, Integer discount) throws SQLException; //ADMIN STUFF
    List<Product> getFavouriteProducts(Integer userId) throws SQLException;
    Map<Product, Integer> getProductsByOrder(Integer orderId) throws SQLException;
    List<Order> getOrdersByUserId(Integer userId) throws SQLException;
    void addFavouriteProduct(Integer userId, Integer productId) throws SQLException;
    void removeFavouriteProduct(Integer userId, Integer productId) throws SQLException;
    void addProductsToOrder(Map<Product, Integer> products, Integer orderId) throws SQLException;
    boolean checkIfProductsExist(Map<Product, Integer> products) throws SQLException;
    void removeProducts(Map<Product, Integer> products) throws SQLException;
}
