package com.project.GameGround.controllers;

import com.project.GameGround.entities.User;
import com.project.GameGround.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @GetMapping("/register")
    public String registerForm(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/")
    public String processRegistration(User user, Model model){
        userDetailsService.saveUser(user, model);
        return "main";
    }
}
