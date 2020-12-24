package com.edunge.srtool.jwt;

import com.edunge.srtool.exceptions.InvalidCredentialsException;
import com.edunge.srtool.model.Login;
import com.edunge.srtool.model.User;
import com.edunge.srtool.repository.UserRepository;
import com.edunge.srtool.response.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by adewale adeleye on 01/10/2019
 **/
@Service
@Primary
public class JwtUserDetailsServiceImpl implements JwtUserDetailsService {

    private UserRepository userRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public JwtUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if(user==null){
            throw new UsernameNotFoundException("The provided username does not exist.");
        }
        return JwtUserFactory.create(user);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("INVALID_CREDENTIALS");
        }
    }

    private LoginResponse loginUser(Login login) throws Exception {
        UserDetails details  = loadUserByUsername(login.getUsername());
        final String token = jwtTokenUtil.generateToken(details);
        authenticate(login.getUsername(), login.getPassword());

        User user = userRepository.findByEmail(login.getUsername());

        return new LoginResponse("00","Account created successfully.", token, user);
    }

    private User getUser(User user){
        return user;
    }

    @Override
    public LoginResponse login(Login login) throws Exception {
        UserDetails details  = loadUserByUsername(login.getUsername());
        final String token = jwtTokenUtil.generateToken(details);
        authenticate(login.getUsername(), login.getPassword());
        User user = userRepository.findByEmail(login.getUsername());
        return new LoginResponse("00","Login Successful.", token, user);
    }
}
