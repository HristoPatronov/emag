package com.example.emag.model.dto;

import com.example.emag.model.pojo.Review;
import com.example.emag.model.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetProductReviewDTO {

    private long id;
    private String title;
    private String text;
    private int rating;
    private LocalDate date;
    private UserForReviewsDTO userForReviewsDTO;

    public GetProductReviewDTO(Review review) {
        setId(review.getId());
        setTitle(review.getTitle());
        setText(review.getText());
        setRating(review.getRating());
        setDate(review.getDate());
        setUserForReviewsDTO(new UserForReviewsDTO(review.getUser()));
    }
}
