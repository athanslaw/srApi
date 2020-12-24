package com.edunge.srtool.jwt;

import com.edunge.srtool.model.Login;
import com.edunge.srtool.response.LoginResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by adewale adeleye on 01/10/2019
 **/
public interface JwtUserDetailsService extends UserDetailsService {
    LoginResponse login(Login login) throws Exception;
}
