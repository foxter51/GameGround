package com.project.GameGround.service;

import com.project.GameGround.RoleRepository;
import com.project.GameGround.entities.Role;
import com.project.GameGround.security.AuthProvider;
import com.project.GameGround.details.CustomUserDetails;
import com.project.GameGround.UserRepository;
import com.project.GameGround.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class CustomUserDetailsService implements UserDetailsService {  //implements getting user information from database

    @Autowired
    private UserRepository repo;

    @Autowired
    private RoleRepository roleRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repo.findByEmail(email);  //searching for user in database
        if(user == null){
            throw new UsernameNotFoundException("User not found!");
        }
        repo.updateLoginDate(email, new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));  //update user last login date
        return new CustomUserDetails(user);  //found user
    }

    public void createCustomerAfterOAuth(String email, String name, AuthProvider provider){
        User user = new User();
        user.setEmail(email);
        user.setFirstName(name);
        user.setRegistrationDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        user.setLastLoginDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        user.setStatus("Unblocked");
        user.setAuthProvider(provider);
        user.addRole(roleRepo.findRoleByName("USER"));
        repo.save(user);
    }
}
