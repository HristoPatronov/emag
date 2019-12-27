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
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        if (UserUtil.isUsernameValid(email)) {
            try {
                User user = UserDAO.getInstance().getUserByEmail(email);
                if (user != null) {
                    boolean exists = false;
                    if (UserUtil.isPasswordValid(password)) {
                        exists = user.getPassword().equals(password);
                    }
                    if (exists) {
                        return "redirect:/index";
                    } else {
                        model.addAttribute("msg", "invalid password");
                        return "login";
                    }
                } else {
                    return "register";
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String username, @RequestParam String password, @RequestParam String email, Model model) {
        try {
            if (!UserDAO.getInstance().checkIfUserExists(email)){
                User user = new User(firstName, lastName, username, password, email, false);
                UserDAO.getInstance().registerUser(user);
            } else {
                model.addAttribute("error", "User with that e-mail already exists");
                return "register";
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "login";
    }

}
