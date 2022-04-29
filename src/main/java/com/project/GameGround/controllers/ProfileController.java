package com.project.GameGround.controllers;

import com.project.GameGround.Tags;
import com.project.GameGround.dto.ReviewDTO;
import com.project.GameGround.service.CustomUserDetailsService;
import com.project.GameGround.service.ReviewDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private ReviewDetailsService reviewDetailsService;

    @RequestMapping("/{id}")
    public String profilePage(@PathVariable("id")String userID){
        return "redirect:/profile/{id}/sort=dateDSC";
    }

    @RequestMapping("/{id}/{filter}")
    public String profilePageSort(@PathVariable("id") String userID, @PathVariable("filter") String filter, Model model, Authentication auth){
        model.addAttribute("userProfile", userDetailsService.getProfileByID(Long.parseLong(userID)));
        model.addAttribute("isAdmin", userDetailsService.getCurrentUserAuthorities(auth));
        model.addAttribute("reviews", reviewDetailsService.loadReviewsByID(userID, filter));
        model.addAttribute("createReview", new ReviewDTO());
        model.addAttribute("Tags", new Tags());
        return "profile";
    }
}
