package com.project.GameGround.service;

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
        User user = repo.findByEmail(OAuthUser.getAttribute("email"));  //searching for user in database
        return new CustomOAuth2UserDetails(OAuthUser, user);  //returns new user after successful auth
    }

    public void createUserAfterOAuth(String email, String name, AuthProvider provider){
        User user = new User();
        user.setEmail(email);
        if(name.contains(" ")){
            user.setFirstName(name.split(" ", 2)[0]);
            user.setLastName(name.split(" ", 2)[1]);
        }
        else user.setFirstName(name);
        user.setRegistrationDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        user.setLastLoginDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        user.setStatus("Unblocked");
        user.setAuthProvider(provider);
        user.addRole(roleRepo.findRoleByName("USER"));
        repo.save(user);
    }
}
