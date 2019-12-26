package com.example.emag.controller;

import com.example.emag.responses.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @GetMapping("/login")
    @ResponseBody public ModelAndView login(@RequestParam String email, @RequestParam String password, HttpSession session) {
        String a = "a";
        String b = "a";
        String c = "a";
        return new ModelAndView("redirect:/register");
    }

    @GetMapping("/register")
    @ResponseBody public ResponseEntity<UserResponse> register(){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        UserResponse response = new UserResponse();
        status = HttpStatus.OK;
        return new ResponseEntity<>(response, status);
    }

}
