package com.project.GameGround.controllers;

import com.project.GameGround.Tags;
import com.project.GameGround.dto.CommentDTO;
import com.project.GameGround.dto.ReviewDTO;
import com.project.GameGround.entities.Comment;
import com.project.GameGround.entities.Review;
import com.project.GameGround.service.CustomUserDetailsService;
import com.project.GameGround.service.RatingDetailsService;
import com.project.GameGround.service.ReviewDetailsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewDetailsService reviewDetailsService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private RatingDetailsService ratingDetailsService;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping("/{id}")
    public String reviewPage(@PathVariable("id") String reviewID, Model model, Authentication auth){
        Review review = reviewDetailsService.getReviewByID(reviewID);
        model.addAttribute("review", review);
        model.addAttribute("newComment", new CommentDTO());
        Long currentUserID = userDetailsService.getCurrentUserID(auth);
        model.addAttribute("oldRating", ratingDetailsService.getRateIfRated(reviewID, currentUserID));
        model.addAttribute("liked", ratingDetailsService.isUserLiked(reviewID, currentUserID));
        model.addAttribute("lastGenres", reviewDetailsService.getLastGenres(review.getGroupName()));
        model.addAttribute("prevReview", reviewDetailsService.getPrevReviewID(review.getId()));
        model.addAttribute("nextReview", reviewDetailsService.getNextReviewID(review.getId()));
        return "review";
    }

    @PostMapping("/{id}/save")
    public String saveReview(@PathVariable ("id") String userID, @Valid ReviewDTO review, @RequestParam("rStar")Integer starValue, @ModelAttribute("Tags") Tags tags){
        reviewDetailsService.saveReview(userID, modelMapper.map(review, Review.class), tags, starValue);
        return "redirect:/profile/{id}";
    }

    @PostMapping("/{userID}/remove/{reviewID}")
    public String reviewRemove(@PathVariable("userID") String userID, @PathVariable("reviewID") String reviewID){
        reviewDetailsService.removeReviewByID(reviewID);
        return "redirect:/profile/{userID}";
    }

    @PostMapping("/edit/{reviewID}")
    public String reviewUpdate(@PathVariable("reviewID") String reviewID, RedirectAttributes ra){
        ra.addFlashAttribute("updateReview", reviewDetailsService.getReviewToUpdate(reviewID));
        ra.addFlashAttribute("Tags", reviewDetailsService.getOldTags(reviewID));
        return "redirect:/review/edit_page";
    }

    @GetMapping("/edit_page")
    public String editPage(){
        return "editPage";
    }

    @PostMapping("/{id}/edit/save")
    public String saveEditedReview(@PathVariable ("id")String userID, Review review, @RequestParam("rStar")Integer starValue, @ModelAttribute("Tags") Tags tags){
        reviewDetailsService.saveReview(userID, review, tags, starValue);
        return "redirect:/profile/{id}";
    }

    @PostMapping("/{reviewID}/add_comment/{userID}")
    public String commentCreation(@PathVariable("reviewID")String reviewID, @PathVariable("userID")String userID, @Valid CommentDTO comment, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "redirect:/review/{reviewID}";
        }
        reviewDetailsService.addCommentToReview(reviewID, userID, modelMapper.map(comment, Comment.class));
        return "redirect:/review/{reviewID}";
    }

    @PostMapping("/{reviewID}/add_rate/{userID}")
    public String addRating(@PathVariable("reviewID")String reviewID, @PathVariable("userID")String userID, @RequestParam(name = "rStar")String starValue){
        reviewDetailsService.addRate(reviewID, userID, starValue);
        return "redirect:/review/{reviewID}";
    }

    @RequestMapping("/{reviewID}/change_rate/{userID}")
    public String changeRating(@PathVariable("reviewID")String reviewID, @PathVariable("userID")String userID){
        reviewDetailsService.changeRate(reviewID, userID);
        return "redirect:/review/{reviewID}";
    }

    @PostMapping("/{reviewID}/like/{userID}")
    public String likeReview(@PathVariable("reviewID")String reviewID, @PathVariable("userID")String userID){
        reviewDetailsService.likeReview(reviewID, userID);
        return "redirect:/review/{reviewID}";
    }
}
