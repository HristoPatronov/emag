package com.example.emag.model.dao;

import com.example.emag.model.pojo.Category;
import com.example.emag.model.pojo.Product;
import com.example.emag.model.pojo.Specification;
import com.example.emag.model.pojo.SubCategory;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDAO implements IProductDAO {

    public static final double MIN_PRICE_OF_PRODUCT = 0;
    public static final double MAX_PRICE_OF_PRODUCT = Integer.MAX_VALUE;

    @Override
    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT p.*, sc.*, c.* FROM products AS p " +
                "JOIN sub_categories AS sc ON p.sub_category_id = sc.id " +
                "JOIN categories AS c ON sc.category_id = c.id;";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            ResultSet set = statement.executeQuery();
            while (set.next()){
                Product product = new Product(set.getLong(1),
                                            set.getString(2),
                                            set.getString(3),
                                            set.getDouble(4),
                                            set.getInt(5),
                                            set.getInt(6),
                                            set.getInt(7),
                                            new SubCategory(set.getLong(10),
                                                    set.getString(11),
                                                    set.getBoolean(12),
                                                    new Category(set.getLong(14),
                                                            set.getString(15))),
                                            set.getBoolean(9));
                products.add(product);
            }
        }
        return products;
    }

    @Override
    public List<Product> getProductsBySubCategory(Long subCategoryId, String column, Double minPrice, Double maxPrice, String orderBy) throws SQLException {
        minPrice = minPrice == null ? MIN_PRICE_OF_PRODUCT : minPrice;
        maxPrice = maxPrice == null ? MAX_PRICE_OF_PRODUCT : maxPrice;
        column = column == null ? "id" : column;
        orderBy = orderBy == null ? "ASC":  orderBy;
        List<Product> products = new ArrayList<>();
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT p.*, sc.*, c.* FROM products AS p " +
                "JOIN sub_categories AS sc ON p.sub_category_id = sc.id " +
                "JOIN categories AS c ON sc.category_id = c.id " +
                "WHERE p.sub_category_id = ? AND p.deleted = 0 AND price BETWEEN ? AND ? ORDER BY p."+ column + " " + orderBy + ";";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setLong(1, subCategoryId);
            statement.setDouble(2, minPrice);
            statement.setDouble(3, maxPrice);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                Product product = new Product(set.getLong(1),
                        set.getString(2),
                        set.getString(3),
                        set.getDouble(4),
                        set.getInt(5),
                        set.getInt(6),
                        set.getInt(7),
                        new SubCategory(set.getLong(10),
                                set.getString(11),
                                set.getBoolean(12),
                                new Category(set.getLong(14), set.getString(15))),
                        set.getBoolean(9));
                products.add(product);
            }
        }
        return products;
    }

    //TODO Hybernate
    @Override
    public Product getProductById(long id) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT p.*, sc.*, c.* FROM products AS p " +
                "JOIN sub_categories AS sc ON p.sub_category_id = sc.id " +
                "JOIN categories AS c ON sc.category_id = c.id WHERE p.id = ?;";
        Product product = null;
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setLong(1, id);
            ResultSet set = statement.executeQuery();
            if (!set.next()){
                return null;
            }
            product = new Product(set.getLong(1),
                    set.getString(2),
                    set.getString(3),
                    set.getDouble(4),
                    set.getInt(5),
                    set.getInt(6),
                    set.getInt(7),
                    new SubCategory(set.getLong(10),
                            set.getString(11),
                            set.getBoolean(12),
                            new Category(set.getLong(14), set.getString(15))),
                    set.getBoolean(9));
        }
        return product;
    }

    @Override
    public List<Product> getProductsFromSearch(String searchInput, String column, Double minPrice, Double maxPrice, String orderBy) throws SQLException {
        minPrice = minPrice == null ? MIN_PRICE_OF_PRODUCT : minPrice;
        maxPrice = maxPrice == null ? MAX_PRICE_OF_PRODUCT : maxPrice;
        column = column == null ? "id" : column;
        orderBy = orderBy == null ? "ASC" : orderBy;
        List<Product> products = new ArrayList<>();
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT p.*, sc.*, c.* FROM products AS p " +
                "JOIN sub_categories AS sc ON p.sub_category_id = sc.id " +
                "JOIN categories AS c ON sc.category_id = c.id " +
                "WHERE p.name LIKE ? AND p.deleted = 0 AND p.price BETWEEN ? AND ? ORDER BY p."+ column + " " + orderBy +";";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setString(1, "%" + searchInput + "%");
            statement.setDouble(2, minPrice);
            statement.setDouble(3, maxPrice);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                Product product = new Product(set.getLong(1),
                        set.getString(2),
                        set.getString(3),
                        set.getDouble(4),
                        set.getInt(5),
                        set.getInt(6),
                        set.getInt(7),
                        new SubCategory(set.getLong(10),
                                set.getString(11),
                                set.getBoolean(12),
                                new Category(set.getLong(14), set.getString(15))),
                        set.getBoolean(9));
                products.add(product);
            }
        }
        return products;
    }

    @Override
    public void addProduct(Product product) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "INSERT INTO products (name, description, price, discount, stock, sub_category_id) " +
                "VALUES (?, ?, ?, ?, ?, ?);";
        try(PreparedStatement statement = connection.prepareStatement(url, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getDiscount());
            statement.setInt(5, product.getStock());
            statement.setLong(6, product.getSubCategory().getId());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            product.setId(keys.getLong(1));
        }
    }

    @Override
    public void removeProduct(long productId) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "UPDATE products SET deleted = ? WHERE id = ?;";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setBoolean(1, true);
            statement.setLong(2, productId);
            statement.executeUpdate();
        }
    }

    @Override
    public void editProduct(Product product) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "UPDATE products SET name = ?, description = ?, price = ?, discount = ?, stock = ? WHERE id = ?;";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getDiscount());
            statement.setInt(5, product.getStock());
            statement.setLong(6, product.getId());
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
    public List<Product> getFavouriteProducts(long userId) throws SQLException {
        List<Product> products = new ArrayList<>();
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT p.*, sc.*, c.* FROM products AS p " +
                "JOIN sub_categories AS sc ON p.sub_category_id = sc.id " +
                "JOIN categories AS c ON sc.category_id = c.id " +
                " JOIN users_have_favourite_products AS uhfp ON p.id = uhfp.product_id " +
                "JOIN users AS u ON uhfp.user_id = u.id WHERE u.id = ?;";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setLong(1, userId);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                Product product = new Product(set.getLong(1),
                        set.getString(2),
                        set.getString(3),
                        set.getDouble(4),
                        set.getInt(5),
                        set.getInt(6),
                        set.getInt(7),
                        new SubCategory(set.getLong(10),
                                set.getString(11),
                                set.getBoolean(12),
                                new Category(set.getLong(14), set.getString(15))),
                        set.getBoolean(9));
                products.add(product);
            }
        }
        return products;
    }

    @Override
    public Map<Product, Integer> getProductsByOrder(long orderId) throws SQLException {
        Map<Product, Integer> products = new HashMap<>();
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT p.*, sc.*, c.*, ohp.quantity FROM products AS p " +
                "JOIN sub_categories AS sc ON p.sub_category_id = sc.id " +
                "JOIN categories AS c ON sc.category_id = c.id " +
                "JOIN orders_have_products AS ohp ON p.id = ohp.product_id " +
                "JOIN orders AS o ON ohp.order_id = o.id WHERE o.id = ?;";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setLong(1, orderId);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                Product product = new Product(set.getLong(1),
                        set.getString(2),
                        set.getString(3),
                        set.getDouble(4),
                        set.getInt(5),
                        set.getInt(6),
                        set.getInt(7),
                        new SubCategory(set.getLong(10),
                                set.getString(11),
                                set.getBoolean(12),
                                new Category(set.getLong(14), set.getString(15))),
                        set.getBoolean(9));
                products.put(product, set.getInt(16));
            }
        }
        return products;
    }

    @Override
    public void addFavouriteProduct(long userId, long productId) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "INSERT INTO users_have_favourite_products (user_id, product_id) VALUES (?, ?);";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setLong(1, userId);
            statement.setLong(2, productId);
            statement.executeUpdate();
        }
    }

    @Override
    public void removeFavouriteProduct(long userId, long productId) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "DELETE FROM users_have_favourite_products WHERE user_id = ? AND product_id = ?";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setLong(1, userId);
            statement.setLong(2, productId);
            statement.executeUpdate();
        }
    }

    @Override
    public void addProductsToOrder(Map<Product, Integer> products, long orderId) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "INSERT INTO orders_have_products (order_id, product_id, quantity) VALUES (?,?,?)";
        for (Product product : products.keySet()) {
            try (PreparedStatement statement = connection.prepareStatement(url)) {
                statement.setLong(1, orderId);
                statement.setLong(2, product.getId());
                statement.setInt(3, products.get(product));
                statement.executeUpdate();
            }
        }
    }

    @Override
    public boolean checkIfProductsExist(Map<Product, Integer> products) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT stock FROM products WHERE id = ?";
        for (Product product : products.keySet()) {
            try (PreparedStatement statement = connection.prepareStatement(url)) {
                statement.setLong(1, product.getId());
                ResultSet set = statement.executeQuery();
                if (!set.next()){
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
                statement.setLong(2, product.getId());
                statement.executeUpdate();
            }
        }
    }

    @Override
    public void addReservedQuantity(long productId, Integer quantity) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "UPDATE products SET reserved_quantity = reserved_quantity + ? WHERE id = ?;";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setInt(1, quantity);
            statement.setLong(2, productId);
            statement.executeUpdate();
        }
    }

    @Override
    public void removeReservedQuantity(long productId, Integer quantity) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "UPDATE products SET reserved_quantity = reserved_quantity - ? WHERE id = ?;";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setInt(1, quantity);
            statement.setLong(2, productId);
            statement.executeUpdate();
        }
    }

    @Override
    public void removeReservedQuantities(Map<Product, Integer> products) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "UPDATE products SET reserved_quantity = reserved_quantity - ? WHERE id = ?;";
        for (Product product : products.keySet()) {
            try(PreparedStatement statement = connection.prepareStatement(url)) {
                statement.setInt(1, products.get(product));
                statement.setLong(2, product.getId());
                statement.executeUpdate();
            }
        }
    }
}
