package com.edunge.bukinz.response;

import com.edunge.bukinz.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse extends BaseResponse {
    private User userDetails;
    private String token;

    public LoginResponse(String code, String message, String token) {
        super(code, message);
        this.token = token;
    }

    public LoginResponse(String code, String message, String token, User user) {
        super(code,message);
        this.token = token;
        this.userDetails = user;
    }

    public User getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(User userDetails) {
        this.userDetails = userDetails;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
