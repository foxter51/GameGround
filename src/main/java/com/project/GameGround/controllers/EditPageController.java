package com.project.GameGround.controllers;

import com.project.GameGround.entities.Review;
import com.project.GameGround.service.ReviewDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.Valid;

@Controller
public class EditPageController {

    @Autowired
    private ReviewDetailsService reviewDetailsService;

    @GetMapping("/edit_page")
    public String editPage(@Valid @ModelAttribute("updateReview") Review review, BindingResult bindingResult, @ModelAttribute("profileID")String profileID, Model model){
        if(bindingResult.hasErrors()){
            return "redirect:/profile/"+profileID;
        }
        reviewDetailsService.loadReviewToUpdate(review, model);
        return "editPage";
    }
}
