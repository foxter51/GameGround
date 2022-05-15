package com.project.GameGround.security;

import com.project.GameGround.Constants;
import com.project.GameGround.repositories.UserRepository;
import com.project.GameGround.details.CustomOAuth2UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserRepository repo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {  //on success auth
        CustomOAuth2UserDetails oAuth2User = (CustomOAuth2UserDetails)authentication.getPrincipal();
        if(!repo.getByEmail(oAuth2User.getEmail()).isAccountNonLocked()){
            SecurityContextHolder.getContext().setAuthentication(null);  //if user blocked - logout
        }
        else repo.updateLoginDate(oAuth2User.getEmail(), LocalDateTime.now().format(Constants.dateTimeFormatter));  //update login date
        response.sendRedirect("/");
    }
}
