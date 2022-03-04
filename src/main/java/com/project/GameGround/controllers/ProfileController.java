package com.project.GameGround.controllers;

import com.project.GameGround.Tags;
import com.project.GameGround.entities.Review;
import com.project.GameGround.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProfileController {
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @GetMapping("/profile/{id}")
    public String profilePage(@PathVariable("id") String id, Model model, Authentication auth){
        userDetailsService.getProfileByID(id, model);
        userDetailsService.sendCurrentUserID(model, auth);
        userDetailsService.loadReviewsByID(id, model);
        userDetailsService.sendProfileUserID(id, model);
        userDetailsService.createReview(model);
        return "profile";
    }

    @PostMapping("/profile/{id}")
    public String updProfilePage(@PathVariable ("id") String id, Review review, @ModelAttribute("Tags") Tags tags){
        userDetailsService.saveReview(id, review, tags);
        return "redirect:/profile/"+id;
    }
}
