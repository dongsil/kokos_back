package com.base.authentication;

import java.util.Optional;
import java.util.Date;
import java.time.ZonedDateTime;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import com.base.repository.UserRepository;
import com.base.model.User;

@Component
public class TokenManagerImpl implements TokenManager{

    private final String 			secret;
    private final UserRepository 	userRepository;    

    @Autowired              //${app.jwt.secret}
    public TokenManagerImpl(@Value("qwerty") String secret,
                            UserRepository userRepository) {
        this.secret 		= secret;
        this.userRepository = userRepository;
    }
    
    @Override
    public Optional<UserDetails> getUserFromToken(String token) {
    	
        final String 	subject = Jwts.parser()
                					  .setSigningKey(secret)
                					  .parseClaimsJws(token)
                					  .getBody()
                					  .getSubject();
        final Long 		userId 	= Long.valueOf(subject);

        Optional<User> 	user 	= userRepository.findOneById(userId);
        
    	return user.map(u -> (UserDetails)u);
    }
    

    @Override
    public String setTokenForUser(User user) {

        final ZonedDateTime afterOneWeek = ZonedDateTime.now().plusWeeks(1);
        
        return Jwts.builder()
                   .setSubject(user.getId().toString())
                   .signWith(SignatureAlgorithm.HS512, secret)
                   .setExpiration(Date.from(afterOneWeek.toInstant()))
                   .compact();        
    }
}
