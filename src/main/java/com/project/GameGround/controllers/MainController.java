package com.project.GameGround.controllers;

import com.project.GameGround.entities.User;
import com.project.GameGround.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @GetMapping("/")
    public String mainPage(Model model, Authentication currentUser){
        userDetailsService.sendID(model, currentUser);
        return "main";
    }

    @GetMapping("/login")
    public String loginForm(){
        return "login";
    }

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

    @GetMapping("/profile/{id}")
    public String profilePage(@PathVariable ("id") String id, Model model){
        userDetailsService.getProfileByID(id, model);
        return "profile";
    }

    @GetMapping("/review/{id}")
    public String reviewPage(@PathVariable ("id") String id, Model model){
        userDetailsService.getReviewByID(id, model);
        return "review";
    }

    @GetMapping("/users_list")
    public String userList(Model model){
        userDetailsService.sendUsersList(model);
        return "users";
    }

    @RequestMapping("/user_control")
    public String checkboxActions(@RequestParam(name = "checkbox") List<Long> ID, @RequestParam(name = "button") String button, Authentication auth) {
        return userDetailsService.doCheckboxAction(ID, button, auth);
    }

    @GetMapping("/403")
    public String error(){
        return "403";
    }
}
