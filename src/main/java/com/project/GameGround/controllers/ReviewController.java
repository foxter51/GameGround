package com.project.GameGround.controllers;

import com.project.GameGround.entities.Comment;
import com.project.GameGround.service.CustomUserDetailsService;
import com.project.GameGround.service.ReviewDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewDetailsService reviewDetailsService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @RequestMapping("/{id}")
    public String reviewPage(@PathVariable("id") String reviewID, Model model, Authentication auth){
        model.addAttribute("review", reviewDetailsService.getReviewByID(reviewID));
        model.addAttribute("newComment", new Comment());
        Long currentUserID = userDetailsService.getCurrentUserID(auth);
        model.addAttribute("ratePossibility", reviewDetailsService.isUserNotRated(reviewID, currentUserID, "RATING"));
        model.addAttribute("likePossibility", reviewDetailsService.isUserNotRated(reviewID, currentUserID, "LIKE"));
        return "review";
    }

    @PostMapping("/{reviewID}/add_comment/{userID}")
    public String commentCreation(@PathVariable("reviewID")String reviewID, @PathVariable("userID")String userID, @Valid Comment comment, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "redirect:/review/{reviewID}";
        }
        reviewDetailsService.saveComment(reviewID, userID, comment);
        return "redirect:/review/{reviewID}";
    }

    @PostMapping("/review/{reviewID}/add_rate/{userID}")
    public String addRating(@PathVariable("reviewID")String reviewID, @PathVariable("userID")String userID, @RequestParam(name = "rStar")String starValue){
        reviewDetailsService.changeRate(reviewID, userID, starValue);
        return "redirect:/review/{reviewID}";
    }

    @PostMapping("/review/{reviewID}/like/{userID}")
    public String likeReview(@PathVariable("reviewID")String reviewID, @PathVariable("userID")String userID){
        reviewDetailsService.likeReview(reviewID, userID);
        return "redirect:/review/{reviewID}";
    }
}
