package com.edunge.srtool.service.impl;

import com.edunge.srtool.dto.UserDto;
import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.jwt.JwtTokenUtil;
import com.edunge.srtool.jwt.JwtUser;
import com.edunge.srtool.jwt.JwtUserFactory;
import com.edunge.srtool.model.Authority;
import com.edunge.srtool.model.AuthorityName;
import com.edunge.srtool.model.User;
import com.edunge.srtool.repository.AuthorityRepository;
import com.edunge.srtool.repository.UserRepository;
import com.edunge.srtool.response.UserResponse;
import com.edunge.srtool.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final JwtTokenUtil jwtTokenUtil;

    private final AuthorityRepository authorityRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JwtTokenUtil jwtTokenUtil, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public UserResponse saveUser(UserDto userDto) {
        User existingUser = userRepository.findByEmail(userDto.getEmail());
        if(existingUser!=null){
            throw new DuplicateException(String.format("%s already exists.",userDto.getEmail()));
        }
        User user = new User();
        user.setFirstname(userDto.getFirstName());
        user.setLastname(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setPhone(userDto.getPhone());
        user.setRole(userDto.getRole());

        //Default User Role
        Authority authority  = authorityRepository.findByName(AuthorityName.ROLE_USER);
        user.setAuthorities(Arrays.asList(authority));

        userRepository.save(user);
        JwtUser userDetails = JwtUserFactory.create(user);
        String token = jwtTokenUtil.generateUserToken(userDetails, user);
        return new UserResponse("00", "User Registered Successfully.",token, user);
    }

    @Override
    public UserResponse getAllUser() throws NotFoundException {
        List<User> users = userRepository.findAll();
        return new UserResponse("00", "List of users", users);
    }

    @Override
    public UserResponse getUserById(Long userId) throws NotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()){
            throw new NotFoundException("Users not found");
        }
        return new UserResponse("00","User retrieved", user.get());
    }

    //@Todo Update user profile or change password
    //@Todo Extract response messages into a template string.
}
