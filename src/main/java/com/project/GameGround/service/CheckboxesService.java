package com.project.GameGround.service;

import com.project.GameGround.entities.Role;
import com.project.GameGround.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckboxesService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private RoleDetailsService roleDetailsService;

    public String doCheckboxAction(List<Long> ID, String button, Authentication auth){
        switch (button) {
            case "Block":
                return blockAction(ID, auth);
            case "Unblock":
                unblockAction(ID);
                break;
            case "Delete":
                return deleteAction(ID, auth);
            case "SetAdmin":
                return setAdminAction(ID, auth);
        }
        return "redirect:/users/list";
    }

    public String blockAction(List<Long> ID, Authentication auth){
        ID.forEach(id -> {
            userDetailsService.repo.blockById(id);
            LOG.info("User with ID: {} has been blocked", id);
        });
        return isOnMyselfAction(ID, auth);
    }

    public void unblockAction(List<Long> ID){
        ID.forEach(id -> {
            userDetailsService.repo.unblockById(id);
            LOG.info("User with ID: {} has been unblocked", id);
        });
    }

    public String deleteAction(List<Long> ID, Authentication auth){
        ID.forEach(id -> {
            userDetailsService.repo.deleteById(id);
            LOG.info("User with ID: {} has been deleted", id);
        });
        return isOnMyselfAction(ID, auth);
    }

    public String setAdminAction(List<Long> ID, Authentication auth){
        User user = null;
        for(Long userID : ID){
            user = userDetailsService.repo.getById(userID);
            if(isContainsRoleAdmin(user)){  //if user is admin -> remove admin role
                user.removeAdminRole();
                LOG.info("User with ID: {} has been removed admin role", userID);
            }
            else user.addRole(roleDetailsService.repo.getRoleByName("ADMIN"));  //if user is not admin -> add admin role
            LOG.info("User with ID: {} has been given admin role", userID);
        }
        assert user != null;
        userDetailsService.repo.save(user);
        return isOnMyselfAction(ID, auth);
    }

    public boolean isContainsRoleAdmin(User user){
        for(Role role : user.getRoles()){
            if(role.getName().equals("ADMIN")){
                return true;
            }
        }
        return false;
    }

    public String isOnMyselfAction(List<Long> ID, Authentication auth){  //check if user blocked or deleted himself to logout
        String email = userDetailsService.getUserEmail(auth);
        User currentUser = userDetailsService.repo.getByEmail(email);
        if(currentUser == null || ID.contains(currentUser.getId())){
            SecurityContextHolder.getContext().setAuthentication(null);
            return "redirect:/";
        } else return "redirect:/users/list";
    }
}
