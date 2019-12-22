package dao;

import model.Review;

import java.util.Collection;

public interface IReviewDAO {

    void addReview(Review review);
    Collection<Review> getAllReviewsForProduct(Long productId);
    Collection<Review> getAllReviewsForUser(Long userId);

}
