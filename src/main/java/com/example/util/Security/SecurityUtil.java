package com.example.util.Security;

import java.util.Optional;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    public boolean isLogin() {
        Authentication auth = getAuthentication();
        return !(auth instanceof AnonymousAuthenticationToken);
        
    }
    
    public Optional<String> getLoginUserId(){
        if (!isLogin()) {
            return Optional.empty();
        }
        Authentication auth = getAuthentication();
        return Optional.of(auth.getName());
    }
    
    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
