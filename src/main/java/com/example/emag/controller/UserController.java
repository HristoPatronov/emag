package com.example.emag.controller;

import com.example.emag.dao.UserDAO;
import com.example.emag.model.User;
import com.example.emag.utils.UserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;

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
                    if (user.isActivated()) {
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
}
