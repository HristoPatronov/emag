package dao;

import model.Product;

import java.util.Collection;

public interface IProductDAO {

    Collection<Product> getAllProducts();
    Collection<Product> getProductsBySubCategory();
    Product getProductById(Long id);
    Collection<Product> getProductsFromSearch(String searchInput);
    Collection<Product> getProductsByPrice(String orderIn); //ASC or DESC
    Collection<Product> getProductsBetweenMinMax(String orderIn, Double min, Double max);
    void addProduct(Product product);  //ADMIN STUFF
    void removeProduct(Long productId); //ADMIN STUFF
    void editProduct(Long productId, Integer quantity); //ADMIN STUFF
    void setDiscount(Long productId, Integer discount); //ADMIN STUFF
}
