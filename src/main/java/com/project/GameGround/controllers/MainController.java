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
    public String mainPage(Model model, Authentication currentUser){
        userDetailsService.sendCurrentUserID(model, currentUser);
        reviewDetailsService.loadReviews(model, "dateASC");
        return "main";
    }

    @GetMapping("/sort=dateDSC")
    public String sortByDateDSC(Model model, Authentication currentUser){
        userDetailsService.sendCurrentUserID(model, currentUser);
        reviewDetailsService.loadReviews(model, "dateDSC");
        return "main";
    }

    @GetMapping("/sort=ratingASC")
    public String sortByRatingASC(Model model, Authentication currentUser){
        userDetailsService.sendCurrentUserID(model, currentUser);
        reviewDetailsService.loadReviews(model, "ratingASC");
        return "main";
    }

    @GetMapping("/sort=ratingDSC")
    public String sortByRatingDSC(Model model, Authentication currentUser){
        userDetailsService.sendCurrentUserID(model, currentUser);
        reviewDetailsService.loadReviews(model, "ratingDSC");
        return "main";
    }

    @GetMapping("/filter=rating_GE4")
    public String filterByRatingGE4(Model model, Authentication currentUser){
        userDetailsService.sendCurrentUserID(model, currentUser);
        reviewDetailsService.loadReviews(model, "ratingGE4");
        return "main";
    }
}
