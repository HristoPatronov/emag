package com.example.emag.controller;

import com.example.emag.dao.*;
import com.example.emag.model.Address;
import com.example.emag.model.Order;
import com.example.emag.model.Product;
import com.example.emag.model.User;
import com.example.emag.utils.UserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class UserController {

    @GetMapping("/index")
    public String index() {
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    //login
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {

        if(!UserUtil.isEMailValid(email)){
            model.addAttribute("error", "E-mail address should be a valid one!");
            return "login";
        }
        try {
                User user = UserDAO.getInstance().getUserByEmail(email);
                if (user != null && user.getPassword().equals(password)) {
                    session.setAttribute("userId", user.getId());
                    model.addAttribute("msg", "success");
                    if(user.isAdmin()) {
                        return "admin";
                    }
                    return "home";
                } else {
                    model.addAttribute("error", "Invalid credentials!"); //password does not match
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

    //register
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
                model.addAttribute("msg", "success");
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

    //logout
    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response, Model model) {
        if(session.getAttribute("userId") == null) {
            model.addAttribute("error", "you should be logged in");
            response.setStatus(405);
            return "login";
        }
        session.setAttribute("userId", null);
        model.addAttribute("msg", "success");
        return "login";
    }

    //get Personal info
    @GetMapping("/userInfo")
    public User getUserInfoById(@RequestParam int id, HttpSession session, HttpServletResponse response, Model model){
        if(session.getAttribute("userId") == null) {
            model.addAttribute("error", "you should be logged in");
            response.setStatus(405);
            return null;
        }
        User user = null;
        try {
            user = UserDAO.getInstance().getUserById(id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    //update Personal info
    @PostMapping("/updateInfo")
    public void updateUserInfo(@RequestBody User user, HttpSession session, HttpServletResponse response, Model model){
        if(session.getAttribute("userId") == null) {
            model.addAttribute("error", "you should be logged in");
            response.setStatus(405);
            return;
        }
        try {
            UserDAO.getInstance().updateUserInfo(user);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //get all addresses
    @GetMapping("/getAddresses")
    public List<Address> allAdressesByUserId(HttpSession session, HttpServletResponse response, Model model){
        if(session.getAttribute("userId") == null) {
            model.addAttribute("error", "you should be logged in");
            response.setStatus(405);
            return null;
        }
        int userId = (int) session.getAttribute("userId");
        List<Address> list = new ArrayList<>();
        try {
            list = AddressDAO.getInstance().getAllAddresses(userId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    //add address
    @PostMapping("/address")
    public void addAddress(@RequestBody Address address,
                           HttpServletResponse response,
                           HttpSession session,
                           Model model) {
        if(session.getAttribute("userId") == null) {
            model.addAttribute("error", "you should be logged in");
            response.setStatus(405);
            return;
        }
        int userId = (int) session.getAttribute("userId");
        try {
            AddressDAO.getInstance().addAddress(userId, address);
            model.addAttribute("msg", "success");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //remove address TODO
    @DeleteMapping("/address")
    public void deleteAddress(@RequestParam int addressId,
                              HttpServletResponse response,
                              HttpSession session,
                              Model model) {
        if(session.getAttribute("userId") == null) {
            model.addAttribute("error", "you should be logged in");
            response.setStatus(405);
            return;
        }
        int userId = (int) session.getAttribute("userId");
        try {
            List<Address> addresses = AddressDAO.getInstance().getAllAddresses(userId);
            for (Address address : addresses) {
                if (addressId == address.getId()) {
                    AddressDAO.getInstance().deleteAddress(addressId);
                    model.addAttribute("msg", "success");
                    return;
                }
            }
            model.addAttribute("error", "no such address");
            response.setStatus(405);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //edit address TODO
    @PostMapping("/editAddress")
    public void editAddress(@RequestBody Address address,
                            @RequestParam int addressId,
                            HttpServletResponse response,
                            HttpSession session,
                            Model model) {
        if(session.getAttribute("userId") == null) {
            model.addAttribute("error", "you should be logged in");
            response.setStatus(405);
            return;
        }
        int userId = (int) session.getAttribute("userId");

        try {
            List<Address> addresses = AddressDAO.getInstance().getAllAddresses(userId);
            for (Address currentAddress : addresses) {
                if (addressId == currentAddress.getId()) {
                    AddressDAO.getInstance().updateAddress(address, addressId);
                    model.addAttribute("msg", "success");
                    return;
                }
            }
            model.addAttribute("error", "no such address");
            response.setStatus(405);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //get orders
    @GetMapping("/order")
    public List<Order> allOrders(HttpSession session, HttpServletResponse response, Model model){
        if(session.getAttribute("userId") == null) {
            model.addAttribute("error", "you should be logged in");
            response.setStatus(405);
            return null;
        }
        int userId = (int) session.getAttribute("userId");
        List<Order> list = new ArrayList<>();
        try {
            list = ProductDAO.getInstance().getOrdersByUserId(userId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    //get products by order
    @GetMapping("/order/products")
    public Map<Product, Integer> productsByOrder(@RequestParam int id, HttpSession session, HttpServletResponse response, Model model){
        if(session.getAttribute("userId") == null) {
            model.addAttribute("error", "you should be logged in");
            response.setStatus(405);
            return null;
        }
        Map<Product, Integer> products = new HashMap<>();
        try {
            products = ProductDAO.getInstance().getProductsByOrder(id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return products;
    }


    //tested OK
    //get favourite products
    @GetMapping("/favourite")
    public List<Product> getFavouriteProducts(HttpSession session, HttpServletResponse response, Model model) {
        if (session.getAttribute("userId") == null) {
            model.addAttribute("error", "you should be logged in");
            response.setStatus(405);
            return null;
        }
        List<Product> products = new ArrayList<>();
        try {
            products = ProductDAO.getInstance().getFavouriteProducts((Integer) session.getAttribute("userId"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return products;
    }

    //tested OK
    //add to favourite
    @PostMapping("/favourite")
    public void addProductToFavourite(@RequestParam int id, HttpSession session, HttpServletResponse response, Model model){   //id = productId
        if (session.getAttribute("userId") == null) {
            model.addAttribute("error", "you should be logged in");
            response.setStatus(405);
            return;
        }
        List<Product> products;
        int userId = (int) session.getAttribute("userId");
        Product product = null;
        try {
            products = ProductDAO.getInstance().getFavouriteProducts(userId);
            product = ProductDAO.getInstance().getProductById(id);
            for (Product p : products) {
                if (p.getId().equals(product.getId())) {
                    model.addAttribute("error", "this product already present in favourite list");
                    return;
                }
            }
            if (product != null){
                ProductDAO.getInstance().addFavouriteProduct(userId, product.getId());
            } else {
                model.addAttribute("error", "this item doesn't exists");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //tested OK
    //remove favourite product
    @DeleteMapping("/favourite")
    public void deleteFavouriteProduct(@RequestParam int id, HttpSession session, HttpServletResponse response, Model model) {  //productId
        if (session.getAttribute("userId") == null) {
            model.addAttribute("error", "you should be logged in");
            response.setStatus(405);
            return;
        }
        List<Product> products;
        int userId = (int) session.getAttribute("userId");
        try {
            products = ProductDAO.getInstance().getFavouriteProducts(userId);
            for (Product p : products) {
                if(p.getId() == id) {
                    ProductDAO.getInstance().removeFavouriteProduct(userId, id);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
