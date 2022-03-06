package com.project.GameGround.controllers;

import com.project.GameGround.service.CustomUserDetailsService;
import com.project.GameGround.service.ReviewDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ReviewController {
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private ReviewDetailsService reviewDetailsService;

    @GetMapping("/review/{id}")
    public String reviewPage(@PathVariable("id") String id, Model model, Authentication auth){
        userDetailsService.sendCurrentUserID(model, auth);
        reviewDetailsService.getReviewByID(id, model);
        return "review";
    }
}
