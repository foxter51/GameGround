package com.project.GameGround.controllers;

import com.project.GameGround.Tags;
import com.project.GameGround.entities.Review;
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
        userDetailsService.loadReviews(model);
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
    public String profilePage(@PathVariable ("id") String id, Model model, Authentication auth){
        userDetailsService.getProfileByID(id, model);
        userDetailsService.sendID(model, auth);
        userDetailsService.loadReviewsByID(id, model);
        userDetailsService.createReview(model);
        return "profile";
    }

    @PostMapping("/profile/{id}")
    public String updProfilePage(@PathVariable ("id") String id, Review review, @ModelAttribute("Tags") Tags tags){
        userDetailsService.saveReview(id, review, tags);
        return "redirect:/profile/"+id;
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
