package com.project.GameGround.security;

import com.project.GameGround.UserRepository;
import com.project.GameGround.details.CustomOAuth2User;
import com.project.GameGround.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
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
    private CustomUserDetailsService userDetailsService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {  //on success auth
        CustomOAuth2User oAuth2User = (CustomOAuth2User)authentication.getPrincipal();
        if(repo.findByEmail(oAuth2User.getEmail()) == null){  //if user doesn't exist
            userDetailsService.createCustomerAfterOAuth(oAuth2User.getEmail(), oAuth2User.getAttribute("name"), AuthProvider.OTHERS);  //add him to DB
        }
        else{  //if exists
            repo.updateLoginDate(oAuth2User.getEmail(), new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));  //update login date
        }
        response.sendRedirect("/");
    }
}
