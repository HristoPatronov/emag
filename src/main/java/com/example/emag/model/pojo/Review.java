package com.example.emag.model.pojo;

import com.example.emag.model.dto.ReviewDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Review {

    private long id;
    private String title;
    private String text;
    private int rating;
    private LocalDate date;
    private User user;
    private Product product;

    public Review(ReviewDTO reviewDto) {
        this.text = reviewDto.getTitle();
        this.text = reviewDto.getText();
        this.rating = reviewDto.getRating();
    }

}
