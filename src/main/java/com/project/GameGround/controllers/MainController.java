package com.project.GameGround.controllers;

import com.project.GameGround.security.AuthProvider;
import com.project.GameGround.details.CustomOAuth2User;
import com.project.GameGround.details.CustomUserDetails;
import com.project.GameGround.UserRepository;
import com.project.GameGround.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class MainController {

    @Autowired  //object loads automatically(doesn't need initializing)
    private UserRepository repo;

    @GetMapping("/")
    public String mainPage(){
        return "main";
    }

    @GetMapping("/login")
    public String loginForm(){
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/process_register")
    public String processRegistration(User user, Model model){
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRegistrationDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        user.setLastLoginDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        user.setStatus("Unblocked");
        user.setAuthProvider(AuthProvider.LOCAL);
        if(repo.findByEmail(user.getEmail()) == null){
            repo.save(user);
            model.addAttribute("register", "Successful registration!");
        }
        else model.addAttribute("register", "User with this e-mail has already been registered!");
        return "registration_success";
    }

    @GetMapping("/users_list")
    public String userList(Model model, Authentication auth){
        List<User> users = repo.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/403")
    public String error(){
        return "403";
    }

    @RequestMapping("/user_control")
    public String checkboxActions(@RequestParam(name = "checkbox") List<Long> ID, @RequestParam(name = "button") String button, Authentication auth) {
        switch (button) {
            case "Block":
                return blockAction(ID, auth);
            case "Unblock":
                unblockAction(ID);
                break;
            case "Delete":
                return deleteAction(ID, auth);
        }
        return "redirect:/users_list";
    }

    public String blockAction(List<Long> ID, Authentication auth){
        ID.forEach(x -> repo.blockById(x));
        if (isCustomUserDetails(auth)) {
            return ID.contains(repo.findByEmail(auth.getName()).getId()) ? "redirect:/" : "redirect:/users_list";
        }
        return ID.contains(repo.findByEmail(((CustomOAuth2User) auth.getPrincipal()).getAttribute("email")).getId()) ? "redirect:/" : "redirect:/users_list";
    }

    public void unblockAction(List<Long> ID){
        ID.forEach(x -> repo.unblockById(x));
    }

    public String deleteAction(List<Long> ID, Authentication auth){
        ID.forEach(x -> repo.deleteById(x));
        if (isCustomUserDetails(auth)) {
            return (repo.findByEmail(auth.getName()) == null) ? "redirect:/" : "redirect:/users_list";
        }
        return (repo.findByEmail(((CustomOAuth2User) auth.getPrincipal()).getAttribute("email")) == null) ? "redirect:/" : "redirect:/users_list";
    }

    public boolean isCustomUserDetails(Authentication auth){
        return auth.getPrincipal() instanceof CustomUserDetails;
    }
}
