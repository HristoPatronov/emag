package com.example.emag.controller;

import com.example.emag.dao.ProductDAO;
import com.example.emag.dao.UserDAO;
import com.example.emag.model.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@RestController
public class AdminController {

    //add product
    @PostMapping("/addProduct")
    public String addProduct(Product product, Model model, HttpSession session){
        int id = (Integer) session.getAttribute("userId");
        try {
            if (!UserDAO.getInstance().isAdminByUserId(id)){
                return "home";
            }
            ProductDAO.getInstance().addProduct(product);
            model.addAttribute("add", "product added successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "admin";
    }

    //remove product by ID
    @PostMapping("/removeProduct")
    public String removeProduct(@RequestParam int idProduct, Model model, HttpSession session){
        int id = (Integer) session.getAttribute("userId");
        try {
            if (!UserDAO.getInstance().isAdminByUserId(id)){
                return "home";
            }
            Product product = ProductDAO.getInstance().getProductById(idProduct);
            if (product != null) {
                ProductDAO.getInstance().removeProduct(idProduct);
                model.addAttribute("remove", "product removed successfully");
                return "admin";
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        model.addAttribute("remove", "product doesn't exist");
        return "admin";
    }

    //set discount
    @PostMapping("/setDiscount")
    public String setDiscountAtProduct(@RequestParam int Productid, @RequestParam int discount, Model model, HttpSession session){
        int id = (Integer) session.getAttribute("userId");
        try {
            if (!UserDAO.getInstance().isAdminByUserId(id)){
                return "home";
            }
            Product product = ProductDAO.getInstance().getProductById(Productid);
            if (product != null) {
                ProductDAO.getInstance().setDiscount(Productid, discount);
                model.addAttribute("discount", "Discount was set successfully");
                //send email to subscribers
//                List<String> email = UserDAO.getInstance().getAllSubscribedUsers();
//                for (String string : email){
//                    SendEmailController.sendMail(string, "discount", "discount was set to product " + product.getName());
//                }
                return "admin";
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        model.addAttribute("discount", "Discount wasn't set because there isn't product with that ID");
        return "admin";
    }

    //update quantity
    @PostMapping("/updateQuantity")
    public String updateQuantity(@RequestParam int id, @RequestParam int newQuantity, Model model, HttpSession session){
        int userId = (Integer) session.getAttribute("userId");
        try {
            if (!UserDAO.getInstance().isAdminByUserId(userId)){
                return "home";
            }
            Product product = ProductDAO.getInstance().getProductById(id);
            if (product != null) {
                ProductDAO.getInstance().updateQuantity(id, newQuantity);
                model.addAttribute("quantity", "Quantity was modified successfully");
                return "admin";
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        model.addAttribute("quantity", "Quantity wasn't modified because such product doesn't exist");
        return "admin";
    }
}
