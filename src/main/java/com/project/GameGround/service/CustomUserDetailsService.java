package com.project.GameGround.service;

import com.project.GameGround.Constants;
import com.project.GameGround.details.CustomOAuth2UserDetails;
import com.project.GameGround.repositories.UserRepository;
import com.project.GameGround.entities.User;
import com.project.GameGround.security.AuthProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected UserRepository repo;

    @Autowired
    private RoleDetailsService roleDetailsService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repo.getByEmail(email);  //searching for user in database
        if(user == null){
            LOG.error("User {} not found", email);
            throw new UsernameNotFoundException("User not found!");
        }
        LOG.info("User {} authorized", email);
        repo.updateLoginDate(email, LocalDateTime.now().format(Constants.dateTimeFormatter));  //update user last login date
        return user;  //found user
    }

    public boolean saveUser(User user){
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setStatus("Unblocked");
        user.setAuthProvider(AuthProvider.LOCAL);
        user.addRole(roleDetailsService.repo.getRoleByName("USER"));
        if(repo.getByEmail(user.getEmail()) == null){
            LOG.info("Saving user {}", user.getEmail());
            repo.save(user);
            return true;
        }
        LOG.error("User {} registration failed (already exists)", user.getEmail());
        return false;
    }

    public User getProfileByID(Long userID){
        return userID != null ? repo.getById(userID) : null;
    }

    public void changeProfilePicture(Long profileID, MultipartFile profilePicture){
        User user = getProfileByID(profileID);
        try{
            user.setProfilePicture(profilePicture.getBytes());
        }
        catch (IOException e){
            e.printStackTrace();
        }
        repo.save(user);
    }

    public void changeUserFirstname(Long profileID, String firstname){
        User user = repo.getById(profileID);
        user.setFirstName(firstname);
        repo.save(user);
    }

    public void changeUserLastname(Long profileID, String lastname){
        User user = repo.getById(profileID);
        user.setLastName(lastname);
        repo.save(user);
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
