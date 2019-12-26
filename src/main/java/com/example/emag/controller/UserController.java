package com.example.emag.controller;

import com.example.emag.dao.UserDAO;
import com.example.emag.responses.UserResponse;
import com.example.emag.utils.UserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@Controller
public class UserController {


    @GetMapping("/welcome")
    public ModelAndView firstPage() {
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/login")
    public ModelAndView loginPage() {
        return new ModelAndView("redirect:/login/index.html");
    }

    @PostMapping("/login")
    @ResponseBody public ModelAndView login(@RequestParam String email, Model model) {
        boolean exists = false;
        if (UserUtil.isUsernameValid(email)){
            try {
                exists = UserDAO.getInstance().checkIfUserExists(email);
                if (exists){
                    model.addAttribute("email", email);
                    return new ModelAndView("redirect:/fullyLogin.html", "email", email);
                } else {
                    return new ModelAndView("redirect:/register");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return new ModelAndView("redirect:/login");
    }

    @PostMapping("/fullyLogin")
    @ResponseBody public ModelAndView fullyLogin(@RequestParam String email, @RequestParam String password, HttpSession session, Model model){
        boolean exists = false;
        if (UserUtil.isPasswordValid(password)){
            try {
                exists = UserDAO.getInstance().checkIfUserExists(email, password);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
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

    @GetMapping("/register")
    @ResponseBody public ResponseEntity<UserResponse> register(){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        UserResponse response = new UserResponse();
        status = HttpStatus.OK;
        return new ResponseEntity<>(response, status);
    }

}
