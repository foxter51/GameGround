package com.project.GameGround.service;

import com.project.GameGround.details.CustomOAuth2UserDetails;
import com.project.GameGround.details.CustomUserDetails;
import com.project.GameGround.entities.Role;
import com.project.GameGround.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckboxesService {

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
        ID.forEach(id -> userDetailsService.repo.blockById(id));
        return isOnMyselfAction(ID, auth);
    }

    public void unblockAction(List<Long> ID){
        ID.forEach(id -> userDetailsService.repo.unblockById(id));
    }

    public String deleteAction(List<Long> ID, Authentication auth){
        ID.forEach(id -> userDetailsService.repo.deleteById(id));
        return isOnMyselfAction(ID, auth);
    }

    public String setAdminAction(List<Long> ID, Authentication auth){
        User user = null;
        for(Long userID : ID){
            user = userDetailsService.repo.getById(userID);
            if(isContainsRoleAdmin(user)){  //if user is admin -> remove admin role
                user.removeAdminRole();
            }
            else user.addRole(roleDetailsService.repo.getRoleByName("ADMIN"));  //if user is not admin -> add admin role
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
        String email = getUserEmail(auth);
        User currentUser = userDetailsService.repo.getByEmail(email);
        if(currentUser == null || ID.contains(currentUser.getId())){
            SecurityContextHolder.getContext().setAuthentication(null);
            return "redirect:/";
        } else return "redirect:/users/list";
    }

    public String getUserEmail(Authentication auth){
        return isCustomUserDetails(auth) ? auth.getName() : ((CustomOAuth2UserDetails)auth.getPrincipal()).getAttribute("email");
    }

    public boolean isCustomUserDetails(Authentication auth){
        return auth.getPrincipal() instanceof CustomUserDetails;
    }
}
