package com.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.base.authentication.TokenManager;
import com.base.service.SecurityContextService;

import lombok.Value;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationManager  authenticationManager;
    private final TokenManager 			 tokenManager;
    private final SecurityContextService securityContextService;

    @Autowired
    AuthenticationController(AuthenticationManager 	authenticationManager,
                   			 TokenManager 			tokenManager,
                   			 SecurityContextService securityContextService) {

        this.authenticationManager 	= authenticationManager;
        this.tokenManager 			= tokenManager;
        this.securityContextService = securityContextService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public AuthResponse auth(@RequestBody AuthParams params) throws AuthenticationException {
        System.out.println("username : " + params.getUsername());
        System.out.println("password : " + params.getPassword());        
        final UsernamePasswordAuthenticationToken 	authenticationToken = params.toAuthenticationToken();
        final Authentication 						authentication 		= authenticationManager.authenticate(authenticationToken);
        
        System.out.println("authentication : " + authentication);
        
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return securityContextService.currentUser().map(user -> {

            final String token = tokenManager.setTokenForUser(user);
            
            System.out.println("token : " + token);
            
            return new AuthResponse(token);

        }).orElseThrow(() -> {
        	System.out.println("orElseThrow");
        	return new RuntimeException();
        }); // it does not happen. RuntimeException::new

    }

    @Value
    private static final class AuthParams {
        private final String username;
        private final String password;

        UsernamePasswordAuthenticationToken toAuthenticationToken() {
            return new UsernamePasswordAuthenticationToken(username, password);
        }
    }

    @Value
    private static final class AuthResponse {
        private final String token;
    }
}