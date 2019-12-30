package com.example.emag.model;

import java.time.LocalDate;

public class Review {

    private Integer id;
    private String title;
    private String text;
    private Integer rating;
    private LocalDate date;
    private Integer userId;
    private Integer productId;

    public Review() {}

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
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

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}
