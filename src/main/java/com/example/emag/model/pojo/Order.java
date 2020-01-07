package com.example.emag.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Order {

    private long id;
    private Double totalPrice;
    private LocalDate date;
    private User user;
    private PaymentType paymentType;
    private Status status;

    @NoArgsConstructor
    @Setter
    @Getter
    @AllArgsConstructor
    public static class PaymentType {
        private int id;
        private String name;

    }

    @NoArgsConstructor
    @Setter
    @Getter
    @AllArgsConstructor
    public static class Status {
        private int id;
        private String name;

    }
}
