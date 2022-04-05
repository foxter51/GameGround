package com.project.GameGround.controllers;

import com.project.GameGround.Tags;
import com.project.GameGround.entities.Review;
import com.project.GameGround.service.CustomUserDetailsService;
import com.project.GameGround.service.ReviewDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

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
        model.addAttribute("userProfile", userDetailsService.getProfileByID(userID));
        model.addAttribute("isAdmin", userDetailsService.getCurrentUserAuthorities(auth));
        model.addAttribute("reviews", reviewDetailsService.loadReviewsByID(userID, filter));
        model.addAttribute("createReview", new Review());
        model.addAttribute("Tags", new Tags());
        return "profile";
    }

    @PostMapping("/{id}/save")
    public String updProfilePage(@PathVariable ("id") String userID, @Valid Review review, @RequestParam("rStar")Integer starValue, @ModelAttribute("Tags") Tags tags){
        reviewDetailsService.saveReview(userID, review, tags, starValue);
        return "redirect:/profile/{id}";
    }

    @PostMapping("/{userID}/review_remove/{reviewID}")
    public String reviewRemove(@PathVariable("userID") String userID, @PathVariable("reviewID") String reviewID){
        reviewDetailsService.removeReviewByID(reviewID);
        return "redirect:/profile/{userID}";
    }

    @PostMapping("/{userID}/review_edit/{reviewID}")
    public String reviewUpdate(@PathVariable("userID") String userID, @PathVariable("reviewID") String reviewID, RedirectAttributes ra){
        ra.addFlashAttribute("updateReview", reviewDetailsService.getReviewToUpdate(reviewID));
        ra.addFlashAttribute("profileID", userID);
        return "redirect:/edit_page";
    }
}
