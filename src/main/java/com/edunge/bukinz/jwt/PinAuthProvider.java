package com.edunge.bukinz.jwt;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * Created by adewale adeleye on 12/10/2019
 **/
@Component
public class PinAuthProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        JwtUser user = (JwtUser) authentication.getCredentials();
        String password = user.getPassword();
        if (username!=null) {
            return new AuthenticatePin((JwtUser) authentication.getDetails(),password , authentication.getAuthorities());
        } else {
            throw new BadCredentialsException("Biometric Authentication failed");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (AuthenticatePin.class.isAssignableFrom(authentication));
    }
}
