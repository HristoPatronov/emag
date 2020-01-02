package com.example.emag.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class Review {

    @JsonIgnore
    private Integer id;
    private String title;
    private String text;
    private Integer rating;
    private LocalDate date;
    @JsonIgnore
    private Integer userId;
    @JsonIgnore
    private Integer productId;

    public Review(String title, String text, Integer rating, LocalDate date, Integer userId, Integer productId) {
        this.title = title;
        this.text = text;
        this.rating = rating;
        this.date = date;
        this.userId = userId;
        this.productId = productId;
    }

    public Review(Integer id, String title, String text, Integer rating, LocalDate date, Integer userId, Integer productId) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.rating = rating;
        this.date = date;
        this.userId = userId;
        this.productId = productId;
    }
}
