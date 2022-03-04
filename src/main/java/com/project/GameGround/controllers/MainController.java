package com.project.GameGround.controllers;

import com.project.GameGround.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @GetMapping("/")
    public String mainPage(Model model, Authentication currentUser){
        userDetailsService.sendCurrentUserID(model, currentUser);
        userDetailsService.loadReviews(model);
        return "main";
    }
}
