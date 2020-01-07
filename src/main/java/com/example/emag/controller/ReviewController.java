package com.example.emag.controller;

import com.example.emag.exceptions.AuthorizationException;
import com.example.emag.exceptions.NotFoundException;
import com.example.emag.model.dao.ProductDAO;
import com.example.emag.model.dao.ReviewDAO;
import com.example.emag.model.dto.ReviewDTO;
import com.example.emag.model.pojo.Product;
import com.example.emag.model.pojo.Review;
import com.example.emag.model.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@RestController
public class ReviewController extends AbstractController{



    @Autowired
    private ReviewDAO reviewDao;

    @Autowired
    private ProductDAO productDao;

    //add review to product
    @PostMapping("/products/{productId}/reviews")
    public Review addReviewToProduct(@PathVariable(name = "productId") long productId,
                            @RequestBody ReviewDTO reviewDto,
                            HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException();
        }
        Product product = productDao.getProductById(productId);
        if (product == null) {
            throw new NotFoundException("Product not found");
        }
        //TODO validete review info
        Review review = new Review(reviewDto);
        review.setDate(LocalDate.now());
        review.setProduct(product);
        review.setUser(user);
        reviewDao.addReview(review);
        return review;
    }

    @GetMapping("/products/{productId}/reviews")
    public List<Review> getAllReviewsForProduct(@PathVariable(name = "productId") long productId) throws SQLException{
        Product product = productDao.getProductById(productId);
        if (product == null) {
            throw new NotFoundException("Product not found");
        }
        return reviewDao.getAllReviewsForProduct(product.getId());
    }

    @GetMapping("/users/reviews")
    public List<Review> getAllReviewsForUser(HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException();
        }
        return reviewDao.getAllReviewsForUser(user.getId());
    }


//    //add review to product
//    @PostMapping("/addReview")
//    public String addReview(@RequestParam String title,
//                          @RequestParam String text,
//                          @RequestParam int rating,
//                          @RequestParam int productId,
//                          HttpSession session,
//                          HttpServletResponse response,
//                          Model model) {
//        if(session.getAttribute("userId") == null) {
//            model.addAttribute("error", "you should be logged in");
//            response.setStatus(405);
//            return "login";
//        }
//        int userId = (int) session.getAttribute("userId");
//        Review review = new Review(title, text, rating, LocalDate.now(), userId, productId);
//        try {
//            ReviewDAO.getInstance().addReview(review);
//            model.addAttribute("msg", "success");
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        return "product";
//    }

//    //return reviews by product ID
//    @GetMapping("/review")
//    public List<Review> getReviewsByProductId(@RequestParam int productId) {
//        List<Review> list = new ArrayList<>();
//        try {
//            list = ReviewDAO.getInstance().getAllReviewsForProduct(productId);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        return list;
//    }

    //return reviews by user ID
//    @GetMapping("/user/reviews")
//    public List<Review> getReviewsByUserId(HttpSession session,
//                                           HttpServletResponse response,
//                                           Model model) {
//        if(session.getAttribute("userId") == null) {
//            model.addAttribute("error", "you should be logged in");
//            response.setStatus(405);
//            return null;
//        }
//        int userId = (int) session.getAttribute("userId");
//        List<Review> list = new ArrayList<>();
//        try {
//            list = ReviewDAO.getInstance().getAllReviewsForUser(userId);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        return list;
//    }
}
