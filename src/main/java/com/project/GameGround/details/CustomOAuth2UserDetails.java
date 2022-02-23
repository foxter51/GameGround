package com.project.GameGround.details;

import com.project.GameGround.entities.Role;
import com.project.GameGround.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

public class CustomOAuth2UserDetails implements OAuth2User {  //implement authentication feature

    private final OAuth2User oAuth2User;
    private final User user;

    public CustomOAuth2UserDetails(OAuth2User oAuth2User, User user) {
        this.oAuth2User = oAuth2User;
        this.user = user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = new HashSet<>();
        if(user != null){
            roles = user.getRoles();  //if user exists - get his roles
        }
        else roles.add(new Role(1, "USER"));  //else give role "USER"
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return authorities;
    }

    @Override
    public String getName() {
        return oAuth2User.getAttribute("name");
    }

    public String getFullName() {
        return oAuth2User.getAttribute("name");
    }

    public String getEmail(){
        return oAuth2User.getAttribute("email");
    }
}
