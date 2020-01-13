package com.example.emag.controller;

import com.example.emag.model.dto.AddReviewDTO;
import com.example.emag.model.dto.GetProductReviewDTO;
import com.example.emag.model.dto.GetUserReviewDTO;
import com.example.emag.model.pojo.User;
import com.example.emag.services.ReviewService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

import static com.example.emag.services.UserService.SESSION_KEY_LOGGED_USER;

@RestController
public class ReviewController extends AbstractController{

    @Autowired
    private ReviewService reviewUtil;

    //add review to product
    @SneakyThrows
    @PostMapping("/products/{productId}/reviews")
    public GetProductReviewDTO addReviewToProduct(@PathVariable(name = "productId") long productId,
                                                  @RequestBody AddReviewDTO addReviewDto,
                                                  HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        return reviewUtil.addReviewToProduct(productId, addReviewDto, user);
    }

    //get all reviews for product
    @SneakyThrows
    @GetMapping("/products/{productId}/reviews")
    public List<GetProductReviewDTO> getAllReviewsForProduct(@PathVariable(name="productId") long productId) {
        return reviewUtil.getAllReviewsForProduct(productId);
    }

    //get all reviews by user
    @SneakyThrows
    @GetMapping("/users/reviews")
    public List<GetUserReviewDTO> getAllReviewsForUser(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        return reviewUtil.getAllReviewsForUser(user);
    }
}
