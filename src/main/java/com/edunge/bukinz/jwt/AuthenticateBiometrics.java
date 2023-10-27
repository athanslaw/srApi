package com.edunge.bukinz.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Created by adewale adeleye on 12/10/2020
 **/
public class AuthenticateBiometrics extends AbstractAuthenticationToken implements AuthenticationProvider {
    private UserDetails user;
    private boolean biometricAuthentication;
    private Collection<? extends GrantedAuthority> authorities;

    public AuthenticateBiometrics(JwtUser user, boolean biometricAuthentication, Collection<? extends GrantedAuthority> authorities) {
        super(null);
        this.user = user;
        this.biometricAuthentication = biometricAuthentication;
        this.authorities= authorities;
    }

    public boolean isBiometricAuthentication() {
        return biometricAuthentication;
    }

    public void setBiometricAuthentication(boolean biometricAuthentication) {
        this.biometricAuthentication = biometricAuthentication;
    }

    public UserDetails getUser() {
        return user;
    }

    public void setUser(JwtUser user) {
        this.user = user;
    }

    @Override
    public Object getCredentials() {
        return user;
    }

    @Override
    public Object getDetails() {
        return user;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return biometricAuthentication;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return user.getUsername();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (AuthenticateBiometrics.class.isAssignableFrom(authentication));
    }
}
