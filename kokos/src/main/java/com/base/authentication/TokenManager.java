package com.base.authentication;


import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.base.model.User;

@Component
public interface TokenManager {

    Optional<UserDetails> getUserFromToken(String token);
    
    String setTokenForUser(User user);
   
}
