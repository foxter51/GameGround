package com.project.GameGround.controllers;

import com.project.GameGround.Tags;
import com.project.GameGround.entities.Review;
import com.project.GameGround.service.CustomUserDetailsService;
import com.project.GameGround.service.ReviewDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class EditPageController {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private ReviewDetailsService reviewDetailsService;

    @GetMapping("/edit_page")
    public String editPage(@ModelAttribute("updateReview") Review review, @ModelAttribute("profileID")String profileID, Model model, Authentication auth){
        userDetailsService.getProfileByID(profileID, model);
        userDetailsService.sendCurrentUserID(model, auth);
        reviewDetailsService.editReview(review, model);
        return "editPage";
    }
}
