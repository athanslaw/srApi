package com.edunge.srtool.controller;

import com.edunge.srtool.dto.UserDto;
import com.edunge.srtool.exceptions.BadRequestException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.exceptions.NotificationException;
import com.edunge.srtool.exceptions.UserException;
import com.edunge.srtool.jwt.JwtTokenUtil;
import com.edunge.srtool.jwt.JwtUserDetailsService;
import com.edunge.srtool.model.Login;
import com.edunge.srtool.response.*;
import com.edunge.srtool.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/api/v1")
@Api(value="User Registration", description="New user registration endpoint.")
@CrossOrigin(maxAge = 3600)
public class UserController {
    private final JwtUserDetailsService userDetailsService;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserController(JwtTokenUtil jwtTokenUtil, JwtUserDetailsService userDetailsService, UserService userService) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserDto userDto) throws ParseException, UserException, NotFoundException, IOException, NotificationException, BadRequestException {
        return new ResponseEntity<>(userService.saveUser(userDto), HttpStatus.OK);
    }

    @PostMapping(value="/user/upload")
    public ResponseEntity<UserResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        System.out.println("Got here");
        return new ResponseEntity<>(userService.uploadUsers(file), HttpStatus.OK);
    }

    @PutMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserDto userDto) throws ParseException, UserException, NotFoundException, IOException, NotificationException, BadRequestException {
        return new ResponseEntity<>(userService.updateUser(userDto), HttpStatus.OK);
    }

    @PutMapping(value = "/users/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> changePassword(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.changePassword(userDto), HttpStatus.OK);
    }

    @PutMapping(value = "/users/lga", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> updateLga(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.updateLga(userDto), HttpStatus.OK);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "Login with username and password")
    public ResponseEntity<LoginResponse> createAuthenticationToken(@RequestBody Login authenticationRequest) throws Exception {
        return ResponseEntity.ok(userDetailsService.login(authenticationRequest));
    }

    @RequestMapping(value = "/users/all", method = RequestMethod.POST)
    @PreAuthorize("hasRole=ADMIN")
    @ApiOperation(value = "This method fetches all registered users. This can only be accessed by users with administrative privilege.")
    public ResponseEntity<UserResponse> getAllUsers() throws Exception {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ApiOperation(value = "This method fetches all registered users. This can only be accessed by users with administrative privilege.")
    public ResponseEntity<UserResponse> getUsers() throws Exception {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping(value = "/users/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find users by name.")
    public ResponseEntity<UserResponse> filterUsersByName(@RequestParam(required = false) String name) throws Exception {
        return ResponseEntity.ok(userService.findUsersAgentByName(name));
    }

    @RequestMapping(value = "/users/location", method = RequestMethod.GET)
    @ApiOperation(value = "This method fetches registered user's location.")
    public ResponseEntity<LocationResponse> getUserLocation(@RequestHeader("Authorization") String token) throws Exception {
        return ResponseEntity.ok(userService.getUserLgaById(jwtTokenUtil.getUsernameFromToken(token.split(" ")[1])));
    }

    @RequestMapping(value = "/user/id/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasRole=ADMIN")
    @ApiOperation(value = "Fetch the user details by Id. Admin view")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) throws Exception {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @RequestMapping(value = "/user/state/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Fetch the user details by State. Admin view")
    public ResponseEntity<UserResponse> getUserByState(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(userService.getUserByState(id));
    }

    @RequestMapping(value = "/user/senatorial-district/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Fetch the user details by District. Admin view")
    public ResponseEntity<UserResponse> getUserByDistrict(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(userService.getUserByDistrict(id));
    }

    @RequestMapping(value = "/user/lga/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Fetch the user details by LGA. Admin view")
    public ResponseEntity<UserResponse> getUserByLga(@PathVariable String id) throws Exception {
        return ResponseEntity.ok(userService.getUserByLga(id));
    }

    @RequestMapping(value = "/user/id/{id}", method = RequestMethod.DELETE)
//    @PreAuthorize("hasRole=ADMIN")
    @ApiOperation(value = "Delete user details by Id. Admin view")
    public ResponseEntity<UserResponse> deleteUserById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(userService.deleteUserById(id));
    }
}
