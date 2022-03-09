package com.project.GameGround.service;

import com.project.GameGround.details.CustomOAuth2UserDetails;
import com.project.GameGround.details.CustomUserDetails;
import com.project.GameGround.entities.Role;
import com.project.GameGround.entities.User;
import com.project.GameGround.repositories.RoleRepository;
import com.project.GameGround.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckboxesService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private RoleRepository roleRepo;

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
        return "redirect:/users_list";
    }

    public String blockAction(List<Long> ID, Authentication auth){
        ID.forEach(id -> repo.blockById(id));
        return isOnMyselfAction(ID, auth);
    }

    public void unblockAction(List<Long> ID){
        ID.forEach(id -> repo.unblockById(id));
    }

    public String deleteAction(List<Long> ID, Authentication auth){
        ID.forEach(id -> repo.deleteById(id));
        return isOnMyselfAction(ID, auth);
    }

    public String setAdminAction(List<Long> ID, Authentication auth){
        User user = null;
        for(Long userID : ID){
            user = repo.getById(userID);
            if(isContainsRoleAdmin(user)){
                user.removeAdminRole();
            }
            else user.addRole(roleRepo.findRoleByName("ADMIN"));
        }
        assert user != null;
        repo.save(user);
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

    public String isOnMyselfAction(List<Long> ID, Authentication auth){
        String email = getUserEmail(auth);
        if(ID.contains(repo.findByEmail(email).getId())){
            SecurityContextHolder.getContext().setAuthentication(null);
            return "redirect:/";
        } else return "redirect:/users_list";
    }

    public String getUserEmail(Authentication auth){
        return isCustomUserDetails(auth) ? auth.getName() : ((CustomOAuth2UserDetails)auth.getPrincipal()).getAttribute("email");
    }

    public boolean isCustomUserDetails(Authentication auth){
        return auth.getPrincipal() instanceof CustomUserDetails;
    }
}
