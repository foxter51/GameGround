package com.project.GameGround.service;

import com.project.GameGround.Constants;
import com.project.GameGround.repositories.RoleRepository;
import com.project.GameGround.repositories.UserRepository;
import com.project.GameGround.details.CustomOAuth2UserDetails;
import com.project.GameGround.entities.User;
import com.project.GameGround.security.AuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class CustomOAuth2UserDetailsService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private RoleRepository roleRepo;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2User OAuthUser = super.loadUser(userRequest);
        User user = repo.getByEmail(OAuthUser.getAttribute("email"));  //searching for user in database
        return new CustomOAuth2UserDetails(OAuthUser, user);  //returns new user after successful auth
    }

    public void createUserAfterOAuth(String email, String name, AuthProvider provider){
        User user = new User();
        user.setEmail(email);
        if(name.contains(" ")){  //if oauth2 user has last name -> get it
            user.setFirstName(name.split(" ", 2)[Constants.firstNamePart]);
            user.setLastName(name.split(" ", 2)[Constants.secondNamePart]);
        }
        else user.setFirstName(name);  //else set only name
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(Constants.dateTimeFormat);
        user.setRegistrationDate(dateTimeFormat.format(new Date()));
        user.setLastLoginDate(dateTimeFormat.format(new Date()));
        user.setStatus("Unblocked");
        user.setAuthProvider(provider);
        user.addRole(roleRepo.getRoleByName("USER"));
        repo.save(user);
    }
}
