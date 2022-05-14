package com.project.GameGround.controllers;

import com.project.GameGround.dto.UserDTO;
import com.project.GameGround.entities.User;
import com.project.GameGround.service.CustomUserDetailsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@Controller
public class RegisterController {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/register")
    public String registerForm(Model model){
        model.addAttribute("user", new UserDTO());
        return "registration";
    }

    @PostMapping("/register")
    public String processRegistration(@Valid UserDTO user, BindingResult bindingResult, RedirectAttributes ra, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        if(bindingResult.hasErrors()){
            return "redirect:/register";
        }
        ra.addFlashAttribute("register", userDetailsService.saveUser(modelMapper.map(user, User.class), getSiteURL(request))); //send message if register success
        return "redirect:/sort=dateDSC";
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code, RedirectAttributes ra) {
        if (userDetailsService.verify(code)) {
            ra.addFlashAttribute("confirmation", true);
        }
        else {
            ra.addFlashAttribute("confirmation", false);
        }
        return "redirect:/sort=dateDSC";
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}
