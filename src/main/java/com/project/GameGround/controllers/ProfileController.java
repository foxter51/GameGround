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

@Controller
public class ProfileController {
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private ReviewDetailsService reviewDetailsService;

    @GetMapping("/profile/{id}")
    public String profilePage(@PathVariable("id") String id, Model model, Authentication auth){
        userDetailsService.getProfileByID(id, model);
        userDetailsService.sendCurrentUserID(model, auth);
        reviewDetailsService.loadReviewsByID(id, model);
        userDetailsService.sendProfileUserID(id, model);
        reviewDetailsService.createReview(model);
        return "profile";
    }

    @PostMapping("/profile/save/{id}")
    public String updProfilePage(@PathVariable ("id") String id, Review review, @ModelAttribute("Tags") Tags tags){
        reviewDetailsService.saveReview(id, review, tags);
        return "redirect:/profile/{id}";
    }

    @RequestMapping("/profile/review_remove/{id}")
    public String reviewRemove(@RequestParam(name="button") String reviewID, @PathVariable("id") String profileID){
        reviewDetailsService.removeReviewByID(reviewID);
        return "redirect:/profile/{id}";
    }

    @RequestMapping("/profile/review_edit/{id}")
    public String reviewEdit(@RequestParam(name="button") String reviewID, @PathVariable("id") String profileID, Model model, RedirectAttributes ra){
        reviewDetailsService.updateReviewByID(reviewID, ra, profileID);
        return "redirect:/edit_page";
    }

    @GetMapping("/edit_page")
    public String editPage(@ModelAttribute("updateReview") Review review, @ModelAttribute("profileID")String profileID, Model model, Authentication auth){
        userDetailsService.getProfileByID(profileID, model);
        userDetailsService.sendCurrentUserID(model, auth);
        StringBuilder tags = new StringBuilder();
        review.getTags().forEach(tag -> tags.append(tag.getTagName()).append(" "));
        model.addAttribute("Tags", new Tags(tags.toString()));
        model.addAttribute("updateReview", review);
        return "editPage";
    }
}
