package com.project.GameGround.service;

import com.project.GameGround.details.CustomOAuth2UserDetails;
import com.project.GameGround.details.CustomUserDetails;
import com.project.GameGround.entities.Review;
import com.project.GameGround.repositories.ReviewRepository;
import com.project.GameGround.repositories.RoleRepository;
import com.project.GameGround.repositories.UserRepository;
import com.project.GameGround.entities.User;
import com.project.GameGround.security.AuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {  //implements getting user information from database

    @Autowired  //object loads automatically(doesn't need initializing)
    private UserRepository repo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private ReviewRepository reviewRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repo.findByEmail(email);  //searching for user in database
        if(user == null){
            throw new UsernameNotFoundException("User not found!");
        }
        repo.updateLoginDate(email, new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));  //update user last login date
        return new CustomUserDetails(user);  //found user
    }

    public void loadReviews(Model model){
        model.addAttribute("reviews", reviewRepo.findAll());
    }

    public void saveUser(User user, Model model){
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRegistrationDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        user.setLastLoginDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        user.setStatus("Unblocked");
        user.setAuthProvider(AuthProvider.LOCAL);
        user.addRole(roleRepo.findRoleByName("USER"));
        if(repo.findByEmail(user.getEmail()) == null){
            repo.save(user);
            model.addAttribute("register", "Successful registration!");
        }
        else model.addAttribute("register", "User with this e-mail has already been registered!");
    }

    public void getProfileByID(String id, Model model){
        model.addAttribute("userProfile", repo.getById(Long.parseLong(id)));
        model.addAttribute("reviews", reviewRepo.getReviewsByUserID(Long.parseLong(id)));
    }

    public void getReviewByID(String id, Model model){
        Review review = reviewRepo.getById(Long.parseLong(id));
        model.addAttribute("review", review);
        model.addAttribute("img1", Base64.getEncoder().encodeToString(review.getImg1()));
        model.addAttribute("img2", Base64.getEncoder().encodeToString(review.getImg2()));
        model.addAttribute("img3", Base64.getEncoder().encodeToString(review.getImg3()));
    }

    public void sendUsersList(Model model){
        List<User> users = repo.findAll();
        model.addAttribute("users", users);
    }

    public String doCheckboxAction(List<Long> ID, String button, Authentication auth){
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
        ID.forEach(id -> repo.blockById(id));
        String email = getUserEmail(auth);
        if(ID.contains(repo.findByEmail(email).getId())){
            SecurityContextHolder.getContext().setAuthentication(null);
            return "redirect:/";
        } else return "redirect:/users_list";
    }

    public void unblockAction(List<Long> ID){
        ID.forEach(id -> repo.unblockById(id));
    }

    public String deleteAction(List<Long> ID, Authentication auth){
        ID.forEach(id -> repo.deleteById(id));
        String email = getUserEmail(auth);
        if(ID.contains(repo.findByEmail(email).getId())){
            SecurityContextHolder.getContext().setAuthentication(null);
            return "redirect:/";
        } else return "redirect:/users_list";
    }

    public String getUserEmail(Authentication auth){
        return isCustomUserDetails(auth) ? auth.getName() : ((CustomOAuth2UserDetails)auth.getPrincipal()).getAttribute("email");
    }

    public boolean isCustomUserDetails(Authentication auth){
        return auth.getPrincipal() instanceof CustomUserDetails;
    }
}
