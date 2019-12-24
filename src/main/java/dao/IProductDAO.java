package dao;

import model.Product;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public interface IProductDAO {

    List<Product> getAllProducts() throws SQLException;
    List<Product> getProductsBySubCategory(Integer subCategoryId) throws SQLException;
    Product getProductById(Integer id) throws SQLException;
    List<Product> getProductsFromSearch(String searchInput) throws SQLException;
    List<Product> getProductsByPriceAsc() throws SQLException; //ASC
    List<Product> getProductsByPriceDesc() throws SQLException; //DESC
    List<Product> getProductsBetweenMinMaxAsc(Double min, Double max) throws SQLException;
    List<Product> getProductsBetweenMinMaxDesc(Double min, Double max) throws SQLException;
    void addProduct(Product product) throws SQLException;  //ADMIN STUFF
    void removeProduct(Integer productId) throws SQLException; //ADMIN STUFF
    void updateQuantity(Integer productId, Integer quantity) throws SQLException; //ADMIN STUFF
    void setDiscount(Integer productId, Integer discount) throws SQLException; //ADMIN STUFF
}
