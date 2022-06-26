package com.project.GameGround.controllers;

import com.project.GameGround.Tags;
import com.project.GameGround.dto.ReviewDTO;
import com.project.GameGround.service.CustomUserDetailsService;
import com.project.GameGround.service.ReviewDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

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
        model.addAttribute("credentialsChangeAccess", userDetailsService.isLocalRegistered(userID));
        model.addAttribute("reviews", reviewDetailsService.loadReviewsByID(userID, filter));
        model.addAttribute("createReview", new ReviewDTO());
        model.addAttribute("Tags", new Tags());
        return "profile";
    }

    @PostMapping("/{id}/change_profile_picture")
    public String changeProfilePicture(@PathVariable("id")Long profileID, @RequestParam("profilePicture")MultipartFile profilePicture, RedirectAttributes ra){
        ra.addFlashAttribute("photoChange", userDetailsService.changeProfilePicture(profileID, profilePicture));
        return "redirect:/profile/{id}/sort=dateDSC";
    }

    @PostMapping("/{id}/change_firstname")
    public String changeFirstname(@PathVariable("id")Long profileID, @RequestParam("changeFirstname")String firstname, RedirectAttributes ra){
        userDetailsService.changeUserFirstname(profileID, firstname);
        ra.addFlashAttribute("nameChange", true);
        return "redirect:/profile/{id}/sort=dateDSC";
    }

    @PostMapping("/{id}/change_lastname")
    public String changeLastname(@PathVariable("id")Long profileID, @RequestParam("changeLastname")String lastname, RedirectAttributes ra){
        userDetailsService.changeUserLastname(profileID, lastname);
        ra.addFlashAttribute("lastnameChange", true);
        return "redirect:/profile/{id}/sort=dateDSC";
    }

    @PostMapping("/{id}/change_email")
    public String changeEmail(@PathVariable("id")Long profileID, @RequestParam("changeEmail")String email, HttpServletRequest request, RedirectAttributes ra) throws MessagingException, UnsupportedEncodingException {
        ra.addFlashAttribute("emailChange", userDetailsService.sendVerificationNewEmail(userDetailsService.getProfileByID(profileID), getSiteURL(request), email));
        return "redirect:/profile/{id}/sort=dateDSC";
    }

    @GetMapping("/mail/verify")
    public String verifyNewMail(@Param("code")String code, @Param("email")String email, RedirectAttributes ra){
        ra.addFlashAttribute("confirmation", userDetailsService.verifyNewEmail(code, email));
        return "redirect:/sort=dateDSC";
    }

    @PostMapping("/{id}/change_password")
    public String changePassword(@PathVariable("id")Long profileID, @RequestParam("changePassword")String password, RedirectAttributes ra){
        userDetailsService.changeUserPassword(profileID, password);
        ra.addFlashAttribute("passwordChange", true);
        return "redirect:/profile/{id}/sort=dateDSC";
    }

    @PostMapping("/{id}/account_remove")
    public String removeAccount(@PathVariable("id")Long userID, Authentication auth){
        userDetailsService.deleteAccount(userID, auth);
        return "redirect:/";
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}
