package com.edunge.bukinz.jwt;

import com.edunge.bukinz.model.Login;
import com.edunge.bukinz.response.LoginResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by adewale adeleye on 01/10/2019
 **/
public interface JwtUserDetailsService extends UserDetailsService {
    LoginResponse login(Login login) throws Exception;
}
