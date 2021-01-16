package com.edunge.srtool.controller;

import com.edunge.srtool.dto.UserDto;
import com.edunge.srtool.exceptions.BadRequestException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.exceptions.NotificationException;
import com.edunge.srtool.exceptions.UserException;
import com.edunge.srtool.jwt.JwtTokenUtil;
import com.edunge.srtool.jwt.JwtUserDetailsService;
import com.edunge.srtool.model.Login;
import com.edunge.srtool.response.LoginResponse;
import com.edunge.srtool.response.UserResponse;
import com.edunge.srtool.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/api/v1")
@Api(value="User Registration", description="New user registration endpoint.")
@CrossOrigin(maxAge = 3600, allowedHeaders = "*")
public class UserController {
    private final JwtUserDetailsService userDetailsService;
    private final UserService userService;

    @Autowired
    public UserController(JwtTokenUtil jwtTokenUtil, JwtUserDetailsService userDetailsService, UserService userService) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserDto userDto) throws ParseException, UserException, NotFoundException, IOException, NotificationException, BadRequestException {
        return new ResponseEntity<>(userService.saveUser(userDto), HttpStatus.OK);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "Login with username and password")
    public ResponseEntity<LoginResponse> createAuthenticationToken(@RequestBody Login authenticationRequest) throws Exception {
        return ResponseEntity.ok(userDetailsService.login(authenticationRequest));
    }

    @RequestMapping(value = "/users/all", method = RequestMethod.POST)
    @PreAuthorize("hasRole=ADMIN")
    @ApiOperation(value = "This method fetches all registered users. This can only be accessed by users with administrative priviledge.")
    public ResponseEntity<UserResponse> getAllUsers() throws Exception {
        return ResponseEntity.ok(userService.getAllUser());
    }


    @RequestMapping(value = "/user/id/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasRole=ADMIN")
    @ApiOperation(value = "Fetch the user details by Id. Admin view")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) throws Exception {
        return ResponseEntity.ok(userService.getUserById(userId));
    }
}
