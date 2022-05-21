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
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private ReviewDetailsService reviewDetailsService;

    @GetMapping("/{id}")
    public String profilePage(@PathVariable("id")Long userID){
        return "redirect:/profile/{id}/sort=dateDSC";
    }

    @GetMapping("/{id}/{filter}")
    public String profilePageSort(@PathVariable("id")Long userID, @PathVariable("filter")String filter, Model model, Authentication auth){
        model.addAttribute("userProfile", userDetailsService.getProfileByID(userID));
        model.addAttribute("isAdmin", userDetailsService.getCurrentUserAuthorities(auth));
        model.addAttribute("reviews", reviewDetailsService.loadReviewsByID(userID, filter));
        model.addAttribute("createReview", new ReviewDTO());
        model.addAttribute("Tags", new Tags());
        return "profile";
    }

    @PostMapping("/{id}/change_profile_picture")
    public String changeProfilePicture(@PathVariable("id")Long profileID, @RequestParam("profilePicture")MultipartFile profilePicture){
        userDetailsService.changeProfilePicture(profileID, profilePicture);
        return "redirect:/profile/{id}";
    }

    @PostMapping("/{id}/change_firstname")
    public String changeFirstname(@PathVariable("id")Long profileID, @RequestParam("changeFirstname")String firstname){
        userDetailsService.changeUserFirstname(profileID, firstname);
        return "redirect:/profile/{id}";
    }

    @PostMapping("/{id}/change_lastname")
    public String changeLastname(@PathVariable("id")Long profileID, @RequestParam("changeLastname")String lastname){
        userDetailsService.changeUserLastname(profileID, lastname);
        return "redirect:/profile/{id}";
    }

    @PostMapping("/{id}/account_remove")
    public String removeAccount(@PathVariable("id")Long userID, Authentication auth){
        userDetailsService.deleteAccount(userID, auth);
        return "redirect:/";
    }
}
