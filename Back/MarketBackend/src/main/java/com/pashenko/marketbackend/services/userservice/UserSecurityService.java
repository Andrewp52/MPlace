package com.pashenko.marketbackend.services.userservice;

import com.pashenko.marketbackend.entities.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserSecurityService {
    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication instanceof AnonymousAuthenticationToken? null : (User) authentication.getPrincipal();
    }

}
