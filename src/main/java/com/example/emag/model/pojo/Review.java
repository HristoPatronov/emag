package com.example.emag.model.pojo;

import com.example.emag.model.dto.AddReviewDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    private long id;
    private String title;
    private String text;
    private int rating;
    private LocalDate date;
    private User user;
    @JsonIgnore
    private Product product;

    public Review(AddReviewDTO addReviewDto) {
        this.title = addReviewDto.getTitle();
        this.text = addReviewDto.getText();
        this.rating = addReviewDto.getRating();
    }
}
