package com.example.emag.dao;

import com.example.emag.model.Order;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO implements IOrderDAO {

    private static OrderDAO mInstance;

    private OrderDAO() {
    }

    public static OrderDAO getInstance() {
        if (mInstance == null) {
            mInstance = new OrderDAO();
        }
        return mInstance;
    }

    @Override
    public int addOrder(Order order) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "INSERT INTO orders (total_price, date, user_id, payment_type_id, status_id) VALUES (?,?,?,?,?);";
        int id = 0;
        try(PreparedStatement statement = connection.prepareStatement(url, Statement.RETURN_GENERATED_KEYS)) {
            statement.setDouble(1, order.getTotalPrice());
            statement.setDate(2, Date.valueOf(order.getDate()));
            statement.setInt(3, order.getUserId());
            statement.setInt(4, order.getPaymentTypeId());
            statement.setInt(5, order.getStatusId());
            statement.executeUpdate();
            ResultSet set = statement.getGeneratedKeys();
            set.next();
            id = set.getInt(1);
        }
        return id;
    }

    @Override
    public List<Order> getOrdersByUserId(Integer userId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT * FROM orders WHERE user_id = ?;";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setInt(1, userId);
            ResultSet set = statement.executeQuery();
            while (set.next()){
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
}
