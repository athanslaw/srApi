package com.edunge.srtool.service.impl;

import com.edunge.srtool.dto.UserDto;
import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.jwt.JwtTokenUtil;
import com.edunge.srtool.jwt.JwtUser;
import com.edunge.srtool.jwt.JwtUserFactory;
import com.edunge.srtool.model.*;
import com.edunge.srtool.repository.AuthorityRepository;
import com.edunge.srtool.repository.UserRepository;
import com.edunge.srtool.response.LocationResponse;
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

    LgaServiceImpl lgaService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JwtTokenUtil jwtTokenUtil,
                           AuthorityRepository authorityRepository, LgaServiceImpl lgaService) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authorityRepository = authorityRepository;
        this.lgaService = lgaService;
    }

    @Override
    public UserResponse saveUser(UserDto userDto) {
        User existingUser = userRepository.findByEmail(userDto.getEmail());
        if(existingUser!=null){
            throw new DuplicateException(String.format("%s already exists.",userDto.getEmail()));
        }
        User user = new User();
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setPhone(userDto.getPhone());
        user.setRole(userDto.getRole());
        user.setLgaId(userDto.getLgaId());
        userRepository.save(user);
        return new UserResponse("00", "User Registered Successfully.",null, user);
    }

    @Override
    public UserResponse updateUser(UserDto userDto) {
        User existingUser = userRepository.findByEmail(userDto.getEmail());
        if(existingUser ==null){
            throw new DuplicateException(String.format("%s does not exist.",userDto.getEmail()));
        }
        User user = new User();
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        user.setId(userDto.getId());
        user.setPassword(existingUser.getPassword());
        user.setPhone(userDto.getPhone());
        user.setRole(userDto.getRole());
        user.setLgaId(existingUser.getLgaId());

        userRepository.save(user);
        return new UserResponse("00", "User Updated Successfully.",null, user);
    }

    @Override
    public UserResponse updateLga(UserDto userDto) {
        User existingUser = userRepository.findByEmail(userDto.getEmail());
        if(existingUser ==null){
            throw new DuplicateException(String.format("%s does not exist.",userDto.getEmail()));
        }
        User user = new User();
        user.setFirstname(existingUser.getFirstname());
        user.setLastname(existingUser.getLastname());
        user.setEmail(existingUser.getEmail());
        user.setId(existingUser.getId());
        user.setPassword(existingUser.getPassword());
        user.setPhone(existingUser.getPhone());
        user.setRole(existingUser.getRole());
        user.setLgaId(userDto.getLgaId());

        userRepository.save(user);
        return new UserResponse("00", "LGA Updated Successfully.",null, user);
    }

    @Override
    public UserResponse changePassword(UserDto userDto) {
        User existingUser = userRepository.findByEmail(userDto.getEmail());
        if(existingUser ==null){
            throw new DuplicateException(String.format("%s does not exist.",userDto.getEmail()));
        }
        User user = new User();
        user.setFirstname(existingUser.getFirstname());
        user.setLastname(existingUser.getLastname());
        user.setEmail(existingUser.getEmail());
        user.setId(existingUser.getId());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setPhone(existingUser.getPhone());
        user.setRole(existingUser.getRole());
        user.setLgaId(existingUser.getLgaId());

        userRepository.save(user);
        return new UserResponse("00", "LGA Updated Successfully.",null, user);
    }

    @Override
    public UserResponse getAllUser() throws NotFoundException {
        List<User> users = userRepository.findAll();
        users.stream().forEach(user -> user.setLgaId(getLgaById(user.getLgaId())));
        return new UserResponse("00", "List of users", users);
    }

    private String getLgaById(String id){
        try {
            System.out.println("LGA id: "+id);
            return lgaService.findLgaById(new Long(id)).getLga().getName();
        }catch (Exception ne){
            return "";
        }
    }

    @Override
    public LocationResponse getUserLgaById(String id){
        System.out.println("UserId: "+id);
        try {
            String lga = userRepository.findByEmail(id).getLgaId();

            Lga lgaData = lgaService.findLgaById(new Long(lga)).getLga();
            return new LocationResponse("00", "Assigned location", lga,
                    lgaData.getSenatorialDistrict().getId(), lgaData.getName(),
                    lgaData.getSenatorialDistrict().getName()
                    );
        }catch (Exception ne){
            return new LocationResponse("02", "Cant retrieve record at the moment");
        }
    }

    @Override
    public UserResponse getUserById(Long userId) throws NotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()){
            throw new NotFoundException("Users not found");
        }
        return new UserResponse("00","User retrieved", user.get());
    }

    @Override
    public UserResponse deleteUserById(Long userId) throws NotFoundException {
        userRepository.deleteById(userId);
        return new UserResponse("00","User information safely deleted", "");
    }

    //@Todo Update user profile or change password
    //@Todo Extract response messages into a template string.
}
