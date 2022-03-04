package com.project.GameGround.service;

import com.project.GameGround.details.CustomOAuth2UserDetails;
import com.project.GameGround.details.CustomUserDetails;
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

    public String doCheckboxAction(List<Long> ID, String button, Authentication auth){
        switch (button) {
            case "Block":
                return blockAction(ID, auth);
            case "Unblock":
                unblockAction(ID);
                break;
            case "Delete":
                return deleteAction(ID, auth);
        }
        return "redirect:/users_list";
    }

    public String blockAction(List<Long> ID, Authentication auth){
        ID.forEach(id -> repo.blockById(id));
        String email = getUserEmail(auth);
        if(ID.contains(repo.findByEmail(email).getId())){
            SecurityContextHolder.getContext().setAuthentication(null);
            return "redirect:/";
        } else return "redirect:/users_list";
    }

    public void unblockAction(List<Long> ID){
        ID.forEach(id -> repo.unblockById(id));
    }

    public String deleteAction(List<Long> ID, Authentication auth){
        ID.forEach(id -> repo.deleteById(id));
        String email = getUserEmail(auth);
        if(ID.contains(repo.findByEmail(email).getId())){
            SecurityContextHolder.getContext().setAuthentication(null);
            return "redirect:/";
        } else return "redirect:/users_list";
    }

    public boolean isCustomUserDetails(Authentication auth){
        return auth.getPrincipal() instanceof CustomUserDetails;
    }

    public String getUserEmail(Authentication auth){
        return isCustomUserDetails(auth) ? auth.getName() : ((CustomOAuth2UserDetails)auth.getPrincipal()).getAttribute("email");
    }
}
