package com.example.emag.controller;

import com.example.emag.dao.ReviewDAO;
import com.example.emag.model.Review;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ReviewController {

    //add review to product
    @PostMapping("/addReview")
    public String addReview(@RequestParam String title,
                          @RequestParam String text,
                          @RequestParam int rating,
                          @RequestParam int productId,
                          HttpSession session,
                          HttpServletResponse response,
                          Model model) {
        if(session.getAttribute("userId") == null) {
            model.addAttribute("error", "you should be logged in");
            response.setStatus(405);
            return "login";
        }
        int userId = (int) session.getAttribute("userId");
        Review review = new Review(title, text, rating, LocalDate.now(), userId, productId);
        try {
            ReviewDAO.getInstance().addReview(review);
            model.addAttribute("msg", "success");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "product";
    }

    //return reviews by product ID
    @GetMapping("/review")
    public List<Review> getReviewsByProductId(@RequestParam int productId) {
        List<Review> list = new ArrayList<>();
        try {
            list = ReviewDAO.getInstance().getAllReviewsForProduct(productId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    //return reviews by user ID
    @GetMapping("/user/reviews")
    public List<Review> getReviewsByUserId(HttpSession session,
                                           HttpServletResponse response,
                                           Model model) {
        if(session.getAttribute("userId") == null) {
            model.addAttribute("error", "you should be logged in");
            response.setStatus(405);
            return null;
        }
        int userId = (int) session.getAttribute("userId");
        List<Review> list = new ArrayList<>();
        try {
            list = ReviewDAO.getInstance().getAllReviewsForUser(userId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
}
