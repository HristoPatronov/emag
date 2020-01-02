package com.example.emag.dao;

import com.example.emag.model.Order;
import com.example.emag.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDAO implements IProductDAO {

    public static final double MIN_PRICE_OF_PRODUCT = 0;
    public static final double MAX_PRICE_OF_PRODUCT = Integer.MAX_VALUE;


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
    public List<Product> getProductsBySubCategory(Integer subCategoryId, Double minPrice, Double maxPrice, String orderBy) throws SQLException {
        minPrice = minPrice == null ? MIN_PRICE_OF_PRODUCT : minPrice;
        maxPrice = maxPrice == null ? MAX_PRICE_OF_PRODUCT : maxPrice;
        orderBy = orderBy == null ? "ASC" : orderBy;
        List<Product> products = new ArrayList<>();
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT * FROM products WHERE sub_category_id = ? AND price BETWEEN ? AND ? ORDER BY price " + orderBy + ";";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setInt(1, subCategoryId);
            statement.setDouble(2, minPrice);
            statement.setDouble(3, maxPrice);
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
            if (!set.next()){
                return null;
            }
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
    public List<Product> getProductsFromSearch(String searchInput, Double minPrice, Double maxPrice, String orderBy) throws SQLException {
        minPrice = minPrice == null ? MIN_PRICE_OF_PRODUCT : minPrice;
        maxPrice = maxPrice == null ? MAX_PRICE_OF_PRODUCT : maxPrice;
        orderBy = orderBy == null ? "ASC" : orderBy;
        List<Product> products = new ArrayList<>();
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT * FROM products WHERE name LIKE ? AND price BETWEEN ? AND ? ORDER BY price " + orderBy + ";";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setString(1, "%" + searchInput + "%");
            statement.setDouble(2, minPrice);
            statement.setDouble(3, maxPrice);
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
        String url = "SELECT p.* FROM products as p JOIN users_have_favourite_products AS uhfp " +
                "ON p.id = uhfp.product_id JOIN users AS u ON uhfp.user_id = u.id WHERE u.id = ?;";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setInt(1, userId);
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
    public Map<Product, Integer> getProductsByOrder(Integer orderId) throws SQLException {
        Map<Product, Integer> products = new HashMap<>();
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT p.*, ohp.quantity FROM products as p JOIN orders_have_products AS ohp ON p.id = ohp.product_id " +
                "JOIN orders AS o ON ohp.order_id = o.id WHERE o.id = ?;";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setInt(1, orderId);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                Product product = new Product(set.getInt(1),
                        set.getString(2),
                        set.getString(3),
                        set.getDouble(4),
                        set.getInt(5),
                        set.getInt(6),
                        set.getInt(7));
                products.put(product, set.getInt(8));
            }
        }
        return products;
    }

    @Override
    public List<Order> getOrdersByUserId(Integer userId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT * FROM orders WHERE user_id = ?;";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setInt(1, userId);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                Order order = new Order(set.getInt(1),
                        set.getDouble(2),
                        set.getDate(3).toLocalDate(),
                        set.getInt(4),
                        set.getInt(5),
                        set.getInt(6));
                orders.add(order);
            }
        }
        return orders;
    }

    @Override
    public void addFavouriteProduct(Integer userId, Integer productId) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "INSERT INTO users_have_favourite_products (user_id, product_id) VALUES (?, ?);";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setInt(1, userId);
            statement.setInt(2, productId);
            statement.executeUpdate();
        }
    }

    @Override
    public void removeFavouriteProduct(Integer userId, Integer productId) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "DELETE FROM users_have_favourite_products WHERE user_id = ? AND product_id = ?";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setInt(1, userId);
            statement.setInt(2, productId);
            statement.executeUpdate();
        }
    }

    @Override
    public void addProductsToOrder(Map<Product, Integer> products, Integer orderId) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "INSERT INTO orders_have_products (order_id, product_id, quantity) VALUES (?,?,?)";
        connection.setAutoCommit(false);
        for (Product product : products.keySet()) {
            try (PreparedStatement statement = connection.prepareStatement(url)) {
                statement.setInt(1, orderId);
                statement.setInt(2, product.getId());
                statement.setInt(3, products.get(product));
                statement.executeUpdate();
            }
        }
        connection.commit();
    }

    @Override
    public boolean checkIfProductsExist(Map<Product, Integer> products) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT stock FROM products WHERE id = ?";
        for (Product product : products.keySet()) {
            try (PreparedStatement statement = connection.prepareStatement(url)) {
                statement.setInt(1, product.getId());
                ResultSet set = statement.executeQuery();
                set.next();
                if (set.getInt(1) < products.get(product)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void removeProducts(Map<Product, Integer> products) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "UPDATE products SET stock = stock - ? WHERE id = ?;";
        for (Product product : products.keySet()) {
            try(PreparedStatement statement = connection.prepareStatement(url)) {
                statement.setInt(1, products.get(product));
                statement.setInt(2, product.getId());
                statement.executeUpdate();
            }
        }
    }
}
