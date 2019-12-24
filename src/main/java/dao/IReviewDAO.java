package dao;

import model.Review;
import java.sql.SQLException;
import java.util.List;

public interface IReviewDAO {

    void addReview(Review review) throws SQLException;
    List<Review> getAllReviewsForProduct(Integer productId) throws SQLException;
    List<Review> getAllReviewsForUser(Integer userId) throws SQLException;
}
