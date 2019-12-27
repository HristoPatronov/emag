package com.example.emag.controller;

import com.example.emag.dao.UserDAO;
import com.example.emag.model.User;
import com.example.emag.utils.UserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
=======
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
>>>>>>> a70f15acc21f382f8fd49acb4f2d58f017d7bac5

import java.sql.SQLException;

@Controller
public class UserController {

<<<<<<< HEAD
    @GetMapping("/index")
    public String index() {
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
=======

    @GetMapping("/welcome")
    public ModelAndView firstPage() {
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/login")
    public ModelAndView loginPage() {
        return new ModelAndView("redirect:/login/index.html");
>>>>>>> a70f15acc21f382f8fd49acb4f2d58f017d7bac5
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        if (UserUtil.isUsernameValid(email)) {
            try {
<<<<<<< HEAD
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
=======
                exists = UserDAO.getInstance().checkIfUserExists(email);
                if (exists){
                    model.addAttribute("email", email);
                    return new ModelAndView("redirect:/fullyLogin.html", "email", email);
                } else {
                    return new ModelAndView("redirect:/register");
>>>>>>> a70f15acc21f382f8fd49acb4f2d58f017d7bac5
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
<<<<<<< HEAD

        return "login";
=======
        return new ModelAndView("redirect:/login");
>>>>>>> a70f15acc21f382f8fd49acb4f2d58f017d7bac5
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
<<<<<<< HEAD
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
=======
        } else {
            model.addAttribute("msg", "Invalid password");
            return new ModelAndView("redirect:/login");
        }
        if (exists){
            return new ModelAndView("redirect:/mainPage");
        } else {
            model.addAttribute("msg", "invalid password");
            return new ModelAndView("redirect:/login");
        }
    }
>>>>>>> a70f15acc21f382f8fd49acb4f2d58f017d7bac5

        return "login";
    }

}
