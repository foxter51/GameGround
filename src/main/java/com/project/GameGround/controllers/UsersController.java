package com.project.GameGround.controllers;

import com.project.GameGround.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UsersController {
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @GetMapping("/users_list")
    public String userList(Model model){
        userDetailsService.sendUsersList(model);
        return "users";
    }

    @RequestMapping("/user_control")
    public String checkboxActions(@RequestParam(name = "checkbox") List<Long> ID, @RequestParam(name = "button") String button, Authentication auth) {
        return userDetailsService.doCheckboxAction(ID, button, auth);
    }
}
