package com.example.emag.model.dao;

import com.example.emag.model.pojo.Order;
import com.example.emag.model.pojo.User;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderDAO implements IOrderDAO {

    @Override
    public int addOrder(Order order) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "INSERT INTO orders (total_price, date, user_id, payment_type_id, status_id) VALUES (?,?,?,?,?);";
        int id = 0;
        try(PreparedStatement statement = connection.prepareStatement(url, Statement.RETURN_GENERATED_KEYS)) {
            statement.setDouble(1, order.getTotalPrice());
            statement.setDate(2, Date.valueOf(order.getDate()));
            statement.setLong(3, order.getUser().getId());
            statement.setLong(4, order.getPaymentType().getId());
            statement.setLong(5, order.getStatus().getId());
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
        String url = "SELECT o.*,u.* ,pt.*, s.* FROM orders AS o " +
                "JOIN payment_types AS pt ON o.payment_type_id = pt.id " +
                "JOIN statuses AS s ON o.status_id = s.id " +
                "JOIN users AS u ON o.user_id = u.id WHERE o.user_id = ?;";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setInt(1, userId);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                Order order = new Order(set.getInt(1),
                        set.getDouble(2),
                        set.getDate(3).toLocalDate(),
                        new User(set.getLong(7),
                                set.getString(8),
                                set.getString(9),
                                set.getString(10),
                                set.getString(11),
                                set.getBoolean(12),
                                set.getBoolean(13)),
                        new Order.PaymentType(set.getInt(14),
                                set.getString(15)),
                        new Order.Status(set.getInt(16),
                                 set.getString(17)));
                orders.add(order);
            }
        }
        return orders;
    }
}
