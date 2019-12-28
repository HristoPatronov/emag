package com.example.emag.controller;

import com.example.emag.dao.AddressDAO;
import com.example.emag.dao.ProductDAO;
import com.example.emag.dao.UserDAO;
import com.example.emag.model.Address;
import com.example.emag.model.Order;
import com.example.emag.model.Product;
import com.example.emag.model.User;
import com.example.emag.utils.UserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @GetMapping("/index")
    public String index() {
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model) {

        if(!UserUtil.isEMailValid(email)){
            model.addAttribute("error", "E-mail address should be a valid one!");
            return "login";
        }
        try {
            if (UserDAO.getInstance().checkIfUserExists(email)) {
                User user = UserDAO.getInstance().getUserByEmail(email);
                if (user.getPassword().equals(password)) {
                    if (user.isSubscribed()) {
                        return "redirect:/index";
                    } else {
                        model.addAttribute("error", "User is not activated, please check your e-mail!");
                        return "login";
                    }
                } else {
                    model.addAttribute("error", "Invalid credentials!"); //password does not match
                    return "login";
                }
            } else {
                model.addAttribute("error", "Invalid credentials!"); //user not exist
                return "login";
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        model.addAttribute("error", "Error");
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String password2,
                            Model model) {

        if(!UserUtil.isFirstNameValid(firstName)){
            model.addAttribute("error", "First name should be at least 2 characters long " +
                    "and start with upper case caracter!");
            return "register";
        }

        if(!UserUtil.isLastNameValid(lastName)){
            model.addAttribute("error", "Last name should be at least 2 characters long " +
                    "and start with upper case caracter!");
            return "register";
        }

        if(!UserUtil.isUsernameValid(username)){
            model.addAttribute("error", "Username should be at least 4 characters long");
            return "register";
        }

        if(!UserUtil.isEMailValid(email)){
            model.addAttribute("error", "E-mail address should be a valid one!");
            return "register";
        }

        if(!UserUtil.isPasswordValid(password)) {
            model.addAttribute("error", "Password should contains at least one digit, " +
                    "at least one lower case character, at least one upper case character and " +
                    "at least one special character from [@ # $ % ! .]");
            return "register";
        }

        if (!password.equals(password2)) {
            model.addAttribute("error", "The passwords does not match each other!");
            return "register";
        }

        try {
            if (!UserDAO.getInstance().checkIfUserExists(email)) {
                User user = new User(firstName, lastName, username, password, email);
                UserDAO.getInstance().registerUser(user);
                return "login";
            } else {
                model.addAttribute("error", "User with that e-mail already exists");
                return "register";
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        model.addAttribute("error", "Error");
        return "register";
    }

    //get Personal info
    @GetMapping("/userInfo")
    public User getUserInfoById(@RequestParam int id){
        User user = null;
        try {
            user = UserDAO.getInstance().getUserById(id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    @GetMapping("/getAllAdresses")
    public List<Address> allAdressesByUserId(@RequestParam int id){
        List<Address> list = new ArrayList<>();
        try {
            list = AddressDAO.getInstance().getAllAddresses(id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
    //update Personal info
    @PostMapping("/updateInfo")
    public void updateUserInfo(@RequestBody User user){
        try {
            UserDAO.getInstance().updateUserInfo(user);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @GetMapping("/order")
    public List<Order> allOrders(@RequestParam int id){
        List<Order> list = new ArrayList<>();
        try {
            list = ProductDAO.getInstance().getOrdersByUserId(id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    @GetMapping("/order/products")
    public List<Product> productsByOrder(@RequestParam int id){
        List<Product> products = new ArrayList<>();
        try {
            products = ProductDAO.getInstance().getProductsByOrder(id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return products;
    }
    //add product to cart
    @PostMapping("/cart")
    public void addProductToCart(@RequestParam int id, HttpSession session, Model model){

        List<Product> products = new ArrayList<>();
        if (session.getAttribute("cart") != null) {
            products = (List<Product>) session.getAttribute("cart");
        }
        Product fetchedProduct = null;
        try {
            fetchedProduct = ProductDAO.getInstance().getProductById(id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if (fetchedProduct != null && fetchedProduct.getStock() > 0) {
            products.add(fetchedProduct);
            session.setAttribute("cart", products);
        } else {
            model.addAttribute("error", "This product doesn't exist");
        }
    }

    //remove product from cart
    @PostMapping("/removeFromCart")
    public void removeProductFromCart(@RequestParam int id, HttpSession session, Model model){
        List<Product> products = new ArrayList<>();
        if (session.getAttribute("cart") != null){
            products = (List<Product>) session.getAttribute("cart");
        } else {
            model.addAttribute("error", "The cart is empty");
            return;
        }
        products.removeIf(product -> product.getId() == id);
        session.setAttribute("cart", products);
    }

    //add to favourite
    @PostMapping("/addToFavourite")
    public void addProductToFavourite(@RequestParam int id, HttpSession session, Model model){
        Product product = null;
        try {
            product = ProductDAO.getInstance().getProductById(id);
            if (product != null){
                Integer userId = (Integer) session.getAttribute("userId");
                ProductDAO.getInstance().addFavouriteProduct(userId, product.getId());
            } else {
                model.addAttribute("error", "this item doesn't exists");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
