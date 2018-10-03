package com.zoombank.wallet_api;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class BaseController {

    public boolean checkAuthentication(String customerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth instanceof AnonymousAuthenticationToken){
            return false;
        }
        User currentUser = (User) auth.getPrincipal();
        if(!currentUser.getUsername().equals(customerId)){
            return true;
        }
        return false;
    }
}
