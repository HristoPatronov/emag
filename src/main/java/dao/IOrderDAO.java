package dao;

import model.Order;

import java.util.Collection;

public interface IOrderDAO {

    void addOrder(Order order);

    Collection<Order> getOrdersByUserId(Long userId);
}
