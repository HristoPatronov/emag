package com.example.emag.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Integer userId;
    @JsonIgnore
    private Integer paymentTypeId;
    private PaymentType paymentType;
    @JsonIgnore
    private Integer statusId;
    private Status status;

    @NoArgsConstructor
    @Setter
    @Getter
    public static class PaymentType {
        private int id;
        private String name;

        public PaymentType(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @NoArgsConstructor
    @Setter
    @Getter
    public static class Status {
        private int id;
        private String name;

        public Status(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

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
