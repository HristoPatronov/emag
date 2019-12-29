package com.example.emag.dao;

import com.example.emag.model.Order;

import java.sql.SQLException;
import java.util.List;

public interface IOrderDAO {

    int addOrder(Order order) throws SQLException;
    List<Order> getOrdersByUserId(Integer userId) throws SQLException;
}
