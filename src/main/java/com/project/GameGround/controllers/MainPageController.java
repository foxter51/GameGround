package com.project.GameGround.controllers;

import com.project.GameGround.entities.Review;
import com.project.GameGround.service.ReviewDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MainPageController {

    @Autowired
    private ReviewDetailsService reviewDetailsService;

    @GetMapping("/")
    public String mainPage(){
        return "redirect:/sort=dateDSC";
    }

    @RequestMapping("/{filter}")
    public String mainPageSort(@PathVariable("filter")String filter, Model model){
        model.addAttribute("reviews", reviewDetailsService.loadReviews(filter));
        model.addAttribute("last5tags", reviewDetailsService.getLast5Tags());
        return "main";
    }

    @GetMapping("/search")
    public String mainPageSearch(@Param("request")String request, Model model){
        List<Review> reviews = reviewDetailsService.loadReviewBySearch(request);
        model.addAttribute("searchResult", reviews.size() > 0);
        model.addAttribute("searchRequest", request);
        model.addAttribute("reviews", reviews.size()>0 ? reviews : null);
        model.addAttribute("last5tags", reviewDetailsService.getLast5Tags());
        return "main";
    }

    @GetMapping("/read_also")
    public String readAlso(@ModelAttribute("reviews")List<Review> reviews, Model model){
        model.addAttribute("last5tags", reviewDetailsService.getLast5Tags());
        return "main";
    }
}
