package com.example.emag.model.dao;

import com.example.emag.model.pojo.Order;
import com.example.emag.model.pojo.Product;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IOrderDAO {

    Order addOrder(Order order, Map<Product, Integer> products) throws SQLException;
    List<Order> getOrdersByUserId(long userId) throws SQLException;
    Order.Status getStatusById(long statusId) throws SQLException;
    Order.PaymentType getPaymentTypeById(long paymentTypeId) throws SQLException;
}
