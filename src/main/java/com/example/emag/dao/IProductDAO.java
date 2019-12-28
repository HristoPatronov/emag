package com.example.emag.dao;

import com.example.emag.model.Order;
import com.example.emag.model.Product;

import java.sql.SQLException;
import java.util.List;

public interface IProductDAO {

    List<Product> getAllProducts() throws SQLException;
    List<Product> getProductsBySubCategory(Integer subCategoryId) throws SQLException;
    Product getProductById(Integer id) throws SQLException;
    List<Product> getProductsFromSearch(String searchInput) throws SQLException;
    List<Product> getProductsByPriceAsc() throws SQLException;
    List<Product> getProductsByPriceDesc() throws SQLException;
    List<Product> getProductsBetweenMinMaxAsc(Double min, Double max) throws SQLException;
    List<Product> getProductsBetweenMinMaxDesc(Double min, Double max) throws SQLException;
    void addProduct(Product product) throws SQLException;  //ADMIN STUFF
    void removeProduct(Integer productId) throws SQLException; //ADMIN STUFF
    void updateQuantity(Integer productId, Integer quantity) throws SQLException; //ADMIN STUFF
    void setDiscount(Integer productId, Integer discount) throws SQLException; //ADMIN STUFF
    List<Product> getFavouriteProducts(Integer userId) throws SQLException;
    List<Product> getProductsByOrder(Integer orderId) throws SQLException;
    List<Order> getOrdersByUserId(Integer userId) throws SQLException;
    void addFavouriteProduct(Integer userId, Integer productId) throws SQLException;

}
