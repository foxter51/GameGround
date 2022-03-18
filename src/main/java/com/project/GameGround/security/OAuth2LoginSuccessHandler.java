package com.project.GameGround.security;

import com.project.GameGround.Constants;
import com.project.GameGround.repositories.UserRepository;
import com.project.GameGround.details.CustomOAuth2UserDetails;
import com.project.GameGround.service.CustomOAuth2UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserRepository repo;

    @Autowired
    private CustomOAuth2UserDetailsService userDetailsService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {  //on success auth
        CustomOAuth2UserDetails oAuth2User = (CustomOAuth2UserDetails)authentication.getPrincipal();
        if(repo.findByEmail(oAuth2User.getEmail()) == null){  //if user doesn't exist
            userDetailsService.createUserAfterOAuth(oAuth2User.getEmail(), oAuth2User.getAttribute("name"), AuthProvider.OTHERS);  //add him to DB
        }
        else{  //if exists
            if(repo.findByEmail(oAuth2User.getEmail()).getStatus().equals("Blocked")) SecurityContextHolder.getContext().setAuthentication(null);  //if user blocked - logout
            repo.updateLoginDate(oAuth2User.getEmail(), new SimpleDateFormat(Constants.dateTimeFormat).format(new Date()));  //update login date
        }
        response.sendRedirect("/");
    }
}
