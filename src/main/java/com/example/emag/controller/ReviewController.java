package com.example.emag.controller;

import com.example.emag.exceptions.NotFoundException;
import com.example.emag.model.dao.ProductDAO;
import com.example.emag.model.dao.ReviewDAO;
import com.example.emag.model.dto.AddReviewDTO;
import com.example.emag.model.dto.GetProductReviewDTO;
import com.example.emag.model.dto.GetUserReviewDTO;
import com.example.emag.model.pojo.Product;
import com.example.emag.model.pojo.Review;
import com.example.emag.model.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ReviewController extends AbstractController{

    @Autowired
    private ReviewDAO reviewDao;

    @Autowired
    private ProductDAO productDao;

    //add review to product
    @PostMapping("/products/{productId}/reviews")
    public GetProductReviewDTO addReviewToProduct(@PathVariable(name = "productId") long productId,
                                                  @RequestBody AddReviewDTO addReviewDto,
                                                  HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForLoggedUser(user);
        Product product = productDao.getProductById(productId);
        checkForProductExistence(product);
        //TODO validete review info
        Review review = new Review(addReviewDto);
        review.setDate(LocalDate.now());
        review.setProduct(product);
        review.setUser(user);
        reviewDao.addReview(review);
        return new GetProductReviewDTO(review);
    }

    //get all reviews for product
    @GetMapping("/products/{productId}/reviews")
    public List<GetProductReviewDTO> getAllReviewsForProduct(@PathVariable(name="productId") long productId) throws SQLException{
        Product product = productDao.getProductById(productId);
        checkForProductExistence(product);
        List<Review> reviews = reviewDao.getAllReviewsForProduct(product.getId());
        if (reviews.isEmpty()) {
            throw new NotFoundException("This product has no reviews!");
        }
        List<GetProductReviewDTO> responseDto = new ArrayList<>();
        for (Review review : reviews) {
            responseDto.add(new GetProductReviewDTO(review));
        }
        return responseDto;
    }

    //get all reviews by user
    @GetMapping("/users/reviews")
    public List<GetUserReviewDTO> getAllReviewsForUser(HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        checkForLoggedUser(user);
        List<Review> reviews = reviewDao.getAllReviewsForUser(user.getId());
        if (reviews.isEmpty()) {
            throw new NotFoundException("You have no reviews");
        }
        List<GetUserReviewDTO> responseDto = new ArrayList<>();
        for (Review review : reviews) {
            responseDto.add(new GetUserReviewDTO(review));
        }
        return responseDto;
    }
}
