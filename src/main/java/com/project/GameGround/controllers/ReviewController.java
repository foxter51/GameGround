package com.project.GameGround.controllers;

import com.project.GameGround.Tags;
import com.project.GameGround.dto.CommentDTO;
import com.project.GameGround.dto.ReviewDTO;
import com.project.GameGround.entities.Comment;
import com.project.GameGround.entities.Review;
import com.project.GameGround.entities.User;
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
import org.springframework.web.multipart.MultipartFile;
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

    @GetMapping("/{id}")
    public String reviewPage(@PathVariable("id")Long reviewID, Model model, Authentication auth){
        Review review = reviewDetailsService.getReviewByID(reviewID);
        model.addAttribute("review", review);
        model.addAttribute("newComment", new CommentDTO());
        User currentUser = userDetailsService.getProfileByID(userDetailsService.getCurrentUserID(auth));
        model.addAttribute("oldRating", ratingDetailsService.getRateIfRated(review, currentUser));
        model.addAttribute("liked", ratingDetailsService.isUserLiked(review, currentUser));
        model.addAttribute("lastGenres", reviewDetailsService.getLastGenres(review.getGroupName()));
        model.addAttribute("prevReview", reviewDetailsService.getPrevReviewID(reviewID));
        model.addAttribute("nextReview", reviewDetailsService.getNextReviewID(reviewID));
        return "review";
    }

    @PostMapping("/{id}/save")
    public String saveReview(@PathVariable ("id")Long userID, @Valid ReviewDTO review, @RequestParam("rStar")Integer starValue, @RequestParam("image")MultipartFile image, @ModelAttribute("Tags")Tags tags){
        reviewDetailsService.saveReview(userID, modelMapper.map(review, Review.class), tags, starValue, image);
        return "redirect:/profile/{id}";
    }

    @PostMapping("/{userID}/remove/{reviewID}")
    public String reviewRemove(@PathVariable("userID")Long userID, @PathVariable("reviewID")Long reviewID){
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
    public String saveEditedReview(@PathVariable ("id")Long userID, Review review, @RequestParam("rStar")Integer starValue, @RequestParam("image") MultipartFile image, @ModelAttribute("Tags") Tags tags){
        reviewDetailsService.saveReview(userID, review, tags, starValue, image);
        return "redirect:/profile/{id}";
    }

    @PostMapping("/{reviewID}/add_comment/{userID}")
    public String commentCreation(@PathVariable("reviewID")Long reviewID, @PathVariable("userID")Long userID, @Valid CommentDTO comment, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "redirect:/review/{reviewID}";
        }
        reviewDetailsService.addCommentToReview(reviewID, userID, modelMapper.map(comment, Comment.class));
        return "redirect:/review/{reviewID}";
    }

    @PostMapping("/{reviewID}/add_rate/{userID}")
    public String addRating(@PathVariable("reviewID")Long reviewID, @PathVariable("userID")Long userID, @RequestParam("rStar")Integer starValue){
        reviewDetailsService.addRate(reviewID, userID, starValue);
        return "redirect:/review/{reviewID}";
    }

    @PostMapping("/{reviewID}/change_rate/{userID}")
    public String changeRating(@PathVariable("reviewID")Long reviewID, @PathVariable("userID")Long userID){
        reviewDetailsService.changeRate(reviewID, userID);
        return "redirect:/review/{reviewID}";
    }

    @PostMapping("/{reviewID}/like/{userID}")
    public String likeReview(@PathVariable("reviewID")Long reviewID, @PathVariable("userID")Long userID){
        reviewDetailsService.likeReview(reviewID, userID);
        return "redirect:/review/{reviewID}";
    }
}
