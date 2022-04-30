package com.project.GameGround.service;

import com.project.GameGround.Constants;
import com.project.GameGround.details.CustomOAuth2UserDetails;
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

import java.time.LocalDateTime;
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
        repo.updateLoginDate(email, LocalDateTime.now().format(Constants.dateTimeFormatter));  //update user last login date
        return user;  //found user
    }

    public boolean saveUser(User user){
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setStatus("Unblocked");
        user.setAuthProvider(AuthProvider.LOCAL);
        user.addRole(roleDetailsService.repo.getRoleByName("USER"));
        if(repo.getByEmail(user.getEmail()) == null){
            repo.save(user);
            return true;
        }
        return false;
    }

    public User getProfileByID(Long userID){
        return userID != null ? repo.getById(userID) : null;
    }

    public boolean getCurrentUserAuthorities(Authentication currentUser){
        if(currentUser != null){
            String email = getUserEmail(currentUser);
            return isAdmin(repo.getByEmail(email).getAuthorities());
        }
        return false;
    }

    public boolean isAdmin(Collection<? extends GrantedAuthority> roles){
        for(GrantedAuthority role : roles){
            if(role.getAuthority().equals("ADMIN")){
                return true;
            }
        }
        return false;
    }

    public List<User> getUsersList(){
        return repo.findAll();
    }

    public Long getCurrentUserID(Authentication currentUser){
        if(currentUser != null){
            String email = getUserEmail(currentUser);
            return repo.getByEmail(email).getId();
        }
        return null;
    }

    public String getUserEmail(Authentication auth){
        return isUserDetails(auth) ? auth.getName() : ((CustomOAuth2UserDetails)auth.getPrincipal()).getAttribute("email");  //get user email depending on the login type
    }

    public boolean isUserDetails(Authentication auth){
        return auth.getPrincipal() instanceof UserDetails;
    }
}
