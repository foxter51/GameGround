package com.project.GameGround.controllers;

import com.project.GameGround.Tags;
import com.project.GameGround.entities.Review;
import com.project.GameGround.service.CustomUserDetailsService;
import com.project.GameGround.service.ReviewDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private ReviewDetailsService reviewDetailsService;

    @RequestMapping("/profile/{id}")
    public String profilePage(@PathVariable("id")String userID){
        return "redirect:/profile/{id}/sort=dateDSC";
    }

    @RequestMapping("/profile/{id}/{filter}")
    public String profilePageSort(@PathVariable("id") String userID, @PathVariable("filter") String filter, Model model, Authentication auth){
        userDetailsService.getProfileByID(userID, model);
        userDetailsService.sendCurrentUserID(model, auth);
        userDetailsService.sendCurrentUserAuthorities(model, auth);
        reviewDetailsService.loadReviewsByID(userID, model, filter);
        reviewDetailsService.createReview(model);
        return "profile";
    }

    @PostMapping("/profile/{id}/save")
    public String updProfilePage(@PathVariable ("id") String userID, Review review, @RequestParam("rStar")Integer starValue, @ModelAttribute("Tags") Tags tags){
        reviewDetailsService.saveReview(userID, review, tags, starValue);
        return "redirect:/profile/{id}";
    }

    @PostMapping("/profile/{userID}/review_remove/{reviewID}")
    public String reviewRemove(@PathVariable("userID") String userID, @PathVariable("reviewID") String reviewID){
        reviewDetailsService.removeReviewByID(reviewID);
        return "redirect:/profile/{userID}";
    }

    @PostMapping("/profile/{userID}/review_edit/{reviewID}")
    public String reviewUpdate(@PathVariable("userID") String userID, @PathVariable("reviewID") String reviewID, RedirectAttributes ra){
        reviewDetailsService.sendReviewToUpdate(reviewID, ra, userID);
        return "redirect:/edit_page";
    }
}
