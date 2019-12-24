package dao;

import model.Order;

import java.sql.SQLException;
import java.util.List;

public interface IOrderDAO {

    void addOrder(Order order) throws SQLException;

    List<Order> getOrdersByUserId(Integer userId) throws SQLException;
}
