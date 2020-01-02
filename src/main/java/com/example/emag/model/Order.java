package com.example.emag.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class Order {

    private Integer id;
    private Double totalPrice;
    private LocalDate date;
    private Integer userId;
    private Integer paymentTypeId;
    private Integer statusId;

    public Order(Double totalPrice, LocalDate date, Integer userId, Integer paymentTypeId, Integer statusId) {
        this.totalPrice = totalPrice;
        this.date = date;
        this.userId = userId;
        this.paymentTypeId = paymentTypeId;
        this.statusId = statusId;
    }

    public Order(Integer id, Double totalPrice, LocalDate date, Integer userId, Integer paymentTypeId, Integer statusId) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.date = date;
        this.userId = userId;
        this.paymentTypeId = paymentTypeId;
        this.statusId = statusId;
    }
}
