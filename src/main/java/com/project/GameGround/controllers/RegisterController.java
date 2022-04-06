package com.project.GameGround.controllers;

import com.project.GameGround.dto.UserDTO;
import com.project.GameGround.entities.User;
import com.project.GameGround.service.CustomUserDetailsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

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
    public String processRegistration(@Valid UserDTO user, BindingResult bindingResult, RedirectAttributes ra){
        if(bindingResult.hasErrors()){
            return "redirect:/register";
        }
        ra.addFlashAttribute("register", userDetailsService.saveUser(modelMapper.map(user, User.class))); //send message if register success
        return "redirect:/sort=dateDSC";
    }
}
