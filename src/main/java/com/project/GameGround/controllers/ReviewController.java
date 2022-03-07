package com.project.GameGround.controllers;

import com.project.GameGround.entities.Comment;
import com.project.GameGround.service.CustomUserDetailsService;
import com.project.GameGround.service.ReviewDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReviewController {
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private ReviewDetailsService reviewDetailsService;

    @GetMapping("/review/{id}")
    public String reviewPage(@PathVariable("id") String reviewID, Model model, Authentication auth){
        userDetailsService.sendCurrentUserID(model, auth);
        reviewDetailsService.getReviewByID(reviewID, model);
        reviewDetailsService.addComment(model);
        reviewDetailsService.addRatingPossibility(reviewID, model);
        return "review";
    }

    @PostMapping("/review/add_comment/{reviewID}/{userID}")
    public String commentCreation(@PathVariable("reviewID")String reviewID, @PathVariable("userID")String userID, Comment comment){
        reviewDetailsService.saveComment(reviewID, userID, comment);
        return "redirect:/review/{reviewID}";
    }

    @RequestMapping("/review/review/{reviewID}/add_rate/{userID}")
    public String addRating(@PathVariable("reviewID")String reviewID, @PathVariable("userID")String userID, @RequestParam(name = "rStar")String starValue){
        reviewDetailsService.changeRate(reviewID, userID, starValue);
        return "redirect:/review/{reviewID}";
    }
}
