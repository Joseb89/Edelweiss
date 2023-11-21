package com.jaab.edelweiss.utils;

import com.jaab.edelweiss.dto.LoginDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {

    /**
     * Retrieves the UserDetails information of an authenticated user
     *
     * @return - the UserDetails stored within the LoginDTO object
     */
    public LoginDTO getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (LoginDTO) authentication.getPrincipal();
    }
}
