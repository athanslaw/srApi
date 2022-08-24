package com.edunge.srtool.service.impl;

import com.edunge.srtool.dto.UserDto;
import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.jwt.JwtTokenUtil;
import com.edunge.srtool.model.*;
import com.edunge.srtool.repository.*;
import com.edunge.srtool.response.LocationResponse;
import com.edunge.srtool.response.UserResponse;
import com.edunge.srtool.service.FileProcessingService;
import com.edunge.srtool.service.StateService;
import com.edunge.srtool.service.UserService;
import com.edunge.srtool.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final LgaRepository lgaRepository;

    private final StateService stateService;
    private final SenatorialDistrictRepository senatorialDistrictRepository;

    private final JwtTokenUtil jwtTokenUtil;

    private final AuthorityRepository authorityRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    FileProcessingService fileProcessingService;

    LgaServiceImpl lgaService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, LgaRepository lgaRepository, JwtTokenUtil jwtTokenUtil,
                           AuthorityRepository authorityRepository, LgaServiceImpl lgaService,
                           StateService stateService, SenatorialDistrictRepository senatorialDistrictRepository) {
        this.userRepository = userRepository;
        this.lgaRepository = lgaRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authorityRepository = authorityRepository;
        this.lgaService = lgaService;
        this.stateService = stateService;
        this.senatorialDistrictRepository = senatorialDistrictRepository;
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
        user.setRole(userDto.getRole().toLowerCase());
        user.setLgaId(userDto.getLgaId());
        user.setStateId(lgaRepository.findById(Long.valueOf(userDto.getLgaId())).get().getState().getId());
        userRepository.save(user);
        return new UserResponse("00", "User Registered Successfully.",null, user);
    }

    @Override
    public UserResponse uploadUsers(MultipartFile file){
        List<String> csvLines = FileUtil.getCsvLines(file, fileProcessingService.getFileStorageLocation());
        return processUpload(csvLines);
    }

    private UserResponse processUpload(List<String> lines){
        UserDto userDto;
        for (String line:lines) {
            String[] user = line.split(",");
            userDto = new UserDto();
            userDto.setFirstname(user[0].trim());
            userDto.setLastname(user[1].trim());
            userDto.setEmail(user[2].trim());
            userDto.setPassword(user[3].trim());
            userDto.setPhone(user[4].trim());
            userDto.setRole(user[5].trim());
            userDto.setLgaId(user[6].trim());
            saveUser(userDto);
        }

        // format = firstname, lastname, email, password, phone, role, lgaId
        return new UserResponse("00", "File Uploaded.");
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
        user.setStateId(existingUser.getStateId());

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
        user.setStateId(lgaRepository.findById(Long.valueOf(userDto.getLgaId())).get().getState().getId());

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
        user.setStateId(existingUser.getStateId());

        userRepository.save(user);
        return new UserResponse("00", "LGA Updated Successfully.",null, user);
    }

    @Override
    public UserResponse getAllUser(Long stateId) {
        List<User> users = userRepository.findByRole("Administrator");
        users.addAll(userRepository.findByStateId(stateId));
        return new UserResponse("00", "List of users", users);
    }

    private String getLgaById(String id){
        try {
            return lgaService.findLgaById(new Long(id)).getLga().getName();
        }catch (Exception ne){
            return "";
        }
    }

    @Override
    public UserResponse getAllUser() {
        List<User> users = userRepository.findAll();
        users.forEach(user -> user.setLgaId(getLgaById(user.getLgaId())));
        return new UserResponse("00", "List of users", users);
    }


    @Override
    public LocationResponse getUserLgaById(String id){
        try {
            String lga = userRepository.findByEmail(id).getLgaId();

            Lga lgaData = lgaService.findLgaById(Long.valueOf(lga)).getLga();
            return new LocationResponse("00", "Assigned location", lga, lgaData.getCode(),
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

    private Optional<SenatorialDistrict> getSenatorialDistrict(long id){
        return senatorialDistrictRepository.findById(id);
    }

    @Override
    public UserResponse getUserByState(Long id) throws NotFoundException {
        State state = stateService.findStateById(id).getState();
        List<User> users = new ArrayList();
        List<Lga> lgas = lgaRepository.findByState(state);
        lgas.forEach(lga -> {
            List<User> userList = userRepository.findByLgaId(lga.getId()+"");
            users.addAll(userList);
        });
        users.forEach(user -> user.setLgaId(getLgaById(user.getLgaId())));
        return new UserResponse("00","User retrieved", users);
    }

    @Override
    public UserResponse getUserByDistrict(Long id) throws NotFoundException {
        Optional<SenatorialDistrict> senatorialDistrict = this.getSenatorialDistrict(id);
        List<User> users = new ArrayList();
        if(senatorialDistrict.isPresent()) {
            List<Lga> lgas = lgaRepository.findBySenatorialDistrict(senatorialDistrict.get());
            lgas.forEach(lga -> {
                List<User> userList = userRepository.findByLgaId(lga.getId()+"");
                users.addAll(userList);
            });
        }
        users.forEach(user -> user.setLgaId(getLgaById(user.getLgaId())));
        return new UserResponse("00","User retrieved", users);
    }

    @Override
    public UserResponse getUserByLga(String id) throws NotFoundException {
        List<User> users = userRepository.findByLgaId(id);
        users.forEach(user -> user.setLgaId(getLgaById(user.getLgaId())));
        return new UserResponse("00","User retrieved", users);
    }

    public UserResponse findUsersAgentByName(String name)throws NotFoundException{
        List<User> users = userRepository.findByFirstnameOrLastnameOrPhone(name, name, name);
        users.forEach(user -> user.setLgaId(getLgaById(user.getLgaId())));
        return new UserResponse("00", "List of users", users);
    }

    @Override
    public UserResponse deleteUserById(Long userId) throws NotFoundException {
        userRepository.deleteById(userId);
        return new UserResponse("00","User information safely deleted", "");
    }

    //@Todo Update user profile or change password
    //@Todo Extract response messages into a template string.
}
