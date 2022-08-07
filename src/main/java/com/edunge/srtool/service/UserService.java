package com.edunge.srtool.service;

import com.edunge.srtool.dto.UserDto;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.LocationResponse;
import com.edunge.srtool.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserResponse saveUser(UserDto userDto);
    UserResponse updateUser(UserDto userDto);
    UserResponse updateLga(UserDto userDto);
    UserResponse changePassword(UserDto userDto);
    UserResponse getAllUser(Long stateId);
    UserResponse getAllUser();
    LocationResponse getUserLgaById(String id);
    UserResponse getUserById(Long userId) throws NotFoundException;
    UserResponse getUserByState(Long id) throws NotFoundException;
    UserResponse getUserByDistrict(Long id) throws NotFoundException;
    UserResponse getUserByLga(String id) throws NotFoundException;
    UserResponse deleteUserById(Long userId) throws NotFoundException;
    UserResponse findUsersAgentByName(String firstName)throws NotFoundException;
    UserResponse uploadUsers(MultipartFile file);
}
