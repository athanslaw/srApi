package com.edunge.srtool.jwt;


import com.edunge.srtool.exceptions.AuthException;
import com.edunge.srtool.model.User;
import com.edunge.srtool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Created by adewale adeleye on 12/10/2019
 **/
@Component
public class DefaultAuthProvider implements AuthenticationProvider {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user = userRepository.findByEmail(username);
        if(user !=null && password !=null){
            throw new AuthException("Authentication Failed");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthException("Biometric Authentication failed");
        } else {
            return new UsernamePasswordAuthenticationToken(username,password);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
