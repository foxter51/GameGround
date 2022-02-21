package com.project.GameGround.service;

import com.project.GameGround.UserRepository;
import com.project.GameGround.details.CustomOAuth2User;
import com.project.GameGround.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private UserRepository repo;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2User oathUser = super.loadUser(userRequest);
        User user = repo.findByEmail(oathUser.getAttribute("email"));  //searching for user in database
        return new CustomOAuth2User(oathUser, user);  //returns new user after successful auth
    }
}
