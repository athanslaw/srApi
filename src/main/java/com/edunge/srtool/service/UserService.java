package com.edunge.srtool.service;

import com.edunge.srtool.dto.UserDto;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.LocationResponse;
import com.edunge.srtool.response.UserResponse;

public interface UserService {
    UserResponse saveUser(UserDto userDto);
    UserResponse updateUser(UserDto userDto);
    UserResponse updateLga(UserDto userDto);
    UserResponse changePassword(UserDto userDto);
    UserResponse getAllUser() throws NotFoundException;
    LocationResponse getUserLgaById(String id);
    UserResponse getUserById(Long userId) throws NotFoundException;
    UserResponse deleteUserById(Long userId) throws NotFoundException;
}
