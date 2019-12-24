package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private Integer id;
    private Double totalPrice;
    private LocalDate date;
    private Integer userId;
    private Integer paymentTypeId;
    private Integer statusId;
    private List<Product> orderedProducts;

    public Order(Integer id, Double totalPrice, LocalDate date, Integer userId, Integer paymentTypeId, Integer statusId) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.date = date;
        this.userId = userId;
        this.paymentTypeId = paymentTypeId;
        this.statusId = statusId;
        this.orderedProducts = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(Integer paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public List<Product> getOrderedProducts() {
        return orderedProducts;
    }


}
