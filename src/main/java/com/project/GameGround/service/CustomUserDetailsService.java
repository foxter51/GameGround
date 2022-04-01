package com.project.GameGround.service;

import com.project.GameGround.Constants;
import com.project.GameGround.details.CustomOAuth2UserDetails;
import com.project.GameGround.details.CustomUserDetails;
import com.project.GameGround.repositories.UserRepository;
import com.project.GameGround.entities.User;
import com.project.GameGround.security.AuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    protected UserRepository repo;

    @Autowired
    private RoleDetailsService roleDetailsService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repo.getByEmail(email);  //searching for user in database
        if(user == null){
            throw new UsernameNotFoundException("User not found!");
        }
        repo.updateLoginDate(email, new SimpleDateFormat(Constants.dateTimeFormat).format(new Date()));  //update user last login date
        return new CustomUserDetails(user);  //found user
    }

    public void saveUser(User user, RedirectAttributes ra){
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(Constants.dateTimeFormat);
        user.setRegistrationDate(dateTimeFormat.format(new Date()));
        user.setLastLoginDate(dateTimeFormat.format(new Date()));
        user.setStatus("Unblocked");
        user.setAuthProvider(AuthProvider.LOCAL);
        user.addRole(roleDetailsService.repo.getRoleByName("USER"));
        if(repo.getByEmail(user.getEmail()) == null){
            repo.save(user);
            ra.addFlashAttribute("register", true);  //send message if register success
        }
        else ra.addFlashAttribute("register", false);  //send message if register failed
    }

    public void getProfileByID(String userID, Model model){
        model.addAttribute("userProfile", repo.getById(Long.parseLong(userID)));
    }

    public void sendCurrentUserAuthorities(Model model, Authentication currentUser){
        if(currentUser != null){
            String email = getUserEmail(currentUser);
            model.addAttribute("isAdmin", isAdmin(new CustomUserDetails(repo.getByEmail(email)).getAuthorities()));
        }
    }

    public boolean isAdmin(Collection<? extends GrantedAuthority> roles){
        for(GrantedAuthority role : roles){
            if(role.getAuthority().equals("ADMIN")){
                return true;
            }
        }
        return false;
    }

    public void sendUsersList(Model model){
        model.addAttribute("users", repo.findAll());
    }

    public Long getCurrentUserID(Authentication currentUser){
        if(currentUser != null){
            String email = getUserEmail(currentUser);
            return repo.getByEmail(email).getId();
        }
        return null;
    }

    public String getUserEmail(Authentication auth){
        return isCustomUserDetails(auth) ? auth.getName() : ((CustomOAuth2UserDetails)auth.getPrincipal()).getAttribute("email");  //get user email depending on the login type
    }

    public boolean isCustomUserDetails(Authentication auth){
        return auth.getPrincipal() instanceof CustomUserDetails;
    }
}
