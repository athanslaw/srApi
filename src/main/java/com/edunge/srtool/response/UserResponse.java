package com.edunge.srtool.response;

import com.edunge.srtool.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse extends BaseResponse {
    private User user;
    private String token;
    private List<User> users;

    public UserResponse(String code, String message, String token) {
        super(code, message);
        this.token = token;
    }

    public UserResponse(String code, String message, String token, User user) {
        super(code,message);
        this.token = token;
        this.user = user;
    }

    public UserResponse(String code, String message, List<User> users) {
        super(code, message);
        this.users = users;
    }

    public UserResponse(String code, String user_retrieved, User user) {
        super(code, user_retrieved);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
