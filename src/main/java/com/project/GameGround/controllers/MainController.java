package com.project.GameGround.controllers;

import com.project.GameGround.service.CustomUserDetailsService;
import com.project.GameGround.service.ReviewDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private ReviewDetailsService reviewDetailsService;

    @GetMapping("/")
    public String mainPage(){
        return "redirect:/sort=dateASC";
    }

    @RequestMapping("/{filter}")
    public String mainPageSort(@PathVariable("filter")String filter, Model model, Authentication currentUser){
        userDetailsService.sendCurrentUserID(model, currentUser);
        reviewDetailsService.loadReviews(model, filter);
        reviewDetailsService.getLast5Tags(model);
        return "main";
    }
}
