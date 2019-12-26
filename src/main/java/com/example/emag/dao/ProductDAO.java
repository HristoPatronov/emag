package com.example.emag.dao;

import com.example.emag.model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements IProductDAO {


    private static ProductDAO mInstance;

    private ProductDAO() {
    }

    public static ProductDAO getInstance() {
        if (mInstance == null) {
            mInstance = new ProductDAO();
        }
        return mInstance;
    }

    @Override
    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT * FROM products;";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            ResultSet set = statement.executeQuery();
            while (set.next()){
                Product product = new Product(set.getInt(1),
                                            set.getString(2),
                                            set.getString(3),
                                            set.getDouble(4),
                                            set.getInt(5),
                                            set.getInt(6),
                                            set.getInt(7));
                products.add(product);
            }
        }
        return products;
    }

    @Override
    public List<Product> getProductsBySubCategory(Integer subCategoryId) throws SQLException {
        List<Product> products = new ArrayList<>();
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT * FROM products WHERE sub_category_id = ?;";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setInt(1, subCategoryId);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                Product product = new Product(set.getInt(1),
                        set.getString(2),
                        set.getString(3),
                        set.getDouble(4),
                        set.getInt(5),
                        set.getInt(6),
                        set.getInt(7));
                products.add(product);
            }
        }
        return products;
    }

    @Override
    public Product getProductById(Integer id) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT * FROM products WHERE id = ?;";
        Product product = null;
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            set.next();
            product = new Product(set.getInt(1),
                    set.getString(2),
                    set.getString(3),
                    set.getDouble(4),
                    set.getInt(5),
                    set.getInt(6),
                    set.getInt(7));
        }
        return product;
    }

    @Override
    public List<Product> getProductsFromSearch(String searchInput) throws SQLException {
        List<Product> products = new ArrayList<>();
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT * FROM products WHERE name = %?%;";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setString(1, searchInput);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                Product product = new Product(set.getInt(1),
                        set.getString(2),
                        set.getString(3),
                        set.getDouble(4),
                        set.getInt(5),
                        set.getInt(6),
                        set.getInt(7));
                products.add(product);
            }
        }
        return products;
    }

    @Override
    public List<Product> getProductsByPriceAsc() throws SQLException {
        List<Product> products = new ArrayList<>();
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT * FROM products ORDER BY ASC;";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            ResultSet set = statement.executeQuery();
            while (set.next()){
                Product product = new Product(set.getInt(1),
                        set.getString(2),
                        set.getString(3),
                        set.getDouble(4),
                        set.getInt(5),
                        set.getInt(6),
                        set.getInt(7));
                products.add(product);
            }
        }
        return products;
    }

    @Override
    public List<Product> getProductsByPriceDesc() throws SQLException {
        List<Product> products = new ArrayList<>();
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT * FROM products ORDER BY DESC;";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            ResultSet set = statement.executeQuery();
            while (set.next()){
                Product product = new Product(set.getInt(1),
                        set.getString(2),
                        set.getString(3),
                        set.getDouble(4),
                        set.getInt(5),
                        set.getInt(6),
                        set.getInt(7));
                products.add(product);
            }
        }
        return products;
    }

    @Override
    public List<Product> getProductsBetweenMinMaxAsc(Double min, Double max) throws SQLException {
        List<Product> products = new ArrayList<>();
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT * FROM products ORDER BY ASC WHERE price >= ? AND price <= ?;";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setDouble(1, min);
            statement.setDouble(2, max);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                Product product = new Product(set.getInt(1),
                        set.getString(2),
                        set.getString(3),
                        set.getDouble(4),
                        set.getInt(5),
                        set.getInt(6),
                        set.getInt(7));
                products.add(product);
            }
        }
        return products;
    }

    @Override
    public List<Product> getProductsBetweenMinMaxDesc(Double min, Double max) throws SQLException {
        List<Product> products = new ArrayList<>();
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT * FROM products ORDER BY DESC WHERE price >= ? AND price <= ?;";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setDouble(1, min);
            statement.setDouble(2, max);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                Product product = new Product(set.getInt(1),
                        set.getString(2),
                        set.getString(3),
                        set.getDouble(4),
                        set.getInt(5),
                        set.getInt(6),
                        set.getInt(7));
                products.add(product);
            }
        }
        return products;
    }

    @Override
    public void addProduct(Product product) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "INSERT INTO products (name, description, price, discount, stock, sub_category_id) VALUES (?, ?, ?, ?, ?, ?);";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getDiscount());
            statement.setInt(5, product.getStock());
            statement.setInt(6, product.getSubCategoryId());
            statement.executeUpdate();
        }
    }

    @Override
    public void removeProduct(Integer productId) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "DELETE FROM products WHERE id = ?;";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setInt(1, productId);
            statement.executeUpdate();
        }
    }

    @Override
    public void updateQuantity(Integer productId, Integer quantity) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "UPDATE products SET stock = ? WHERE id = ?;";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setInt(1, quantity);
            statement.setInt(2, productId);
            statement.executeUpdate();
        }
    }

    @Override
    public void setDiscount(Integer productId, Integer discount) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "UPDATE products SET discount = ? WHERE id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setInt(1, discount);
            statement.setInt(2, productId);
            statement.executeUpdate();
        }
    }

    @Override
    public List<Product> getFavouriteProducts(Integer userId) throws SQLException {
        List<Product> products = new ArrayList<>();
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT p.* FROM products as p JOIN orders_have_products AS ohp ON p.id = ohp.order_id " +
                "JOIN orders AS o ON ohp.product_id = o.id;";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            ResultSet set = statement.executeQuery();
            while (set.next()){
                Product product = new Product(set.getInt(1),
                        set.getString(2),
                        set.getString(3),
                        set.getDouble(4),
                        set.getInt(5),
                        set.getInt(6),
                        set.getInt(7));
                products.add(product);
            }
        }
        return products;
    }

    @Override
    public List<Product> getProductsByOrder(Integer orderId) throws SQLException {
        List<Product> products = new ArrayList<>();
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT p.* FROM products as p JOIN users_have_favourite_products AS uhfp ON p.id = uhfp.user_id" +
                " JOIN users AS u ON uhfp.product_id = u.id;";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            ResultSet set = statement.executeQuery();
            while (set.next()){
                Product product = new Product(set.getInt(1),
                        set.getString(2),
                        set.getString(3),
                        set.getDouble(4),
                        set.getInt(5),
                        set.getInt(6),
                        set.getInt(7));
                products.add(product);
            }
        }
        return products;
    }
}
