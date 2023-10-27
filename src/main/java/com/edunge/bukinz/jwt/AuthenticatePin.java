package com.edunge.bukinz.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class AuthenticatePin extends AbstractAuthenticationToken {
    private UserDetails user;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public AuthenticatePin(JwtUser user, String pin, Collection<? extends GrantedAuthority> authorities) {
        super(null);
        this.user = user;
        this.password = pin;
        this.authorities= authorities;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return user.getUsername();
    }

}
