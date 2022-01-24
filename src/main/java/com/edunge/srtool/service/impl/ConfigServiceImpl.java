package com.edunge.srtool.service.impl;

import com.edunge.srtool.dto.ConfigDto;
import com.edunge.srtool.dto.UserDto;
import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.jwt.JwtTokenUtil;
import com.edunge.srtool.model.Config;
import com.edunge.srtool.repository.AuthorityRepository;
import com.edunge.srtool.repository.ConfigRepository;
import com.edunge.srtool.response.ConfigResponse;
import com.edunge.srtool.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConfigServiceImpl implements ConfigService {

    private final ConfigRepository configRepository;

    private final JwtTokenUtil jwtTokenUtil;

    private final AuthorityRepository authorityRepository;

    @Autowired
    public ConfigServiceImpl(ConfigRepository configRepository, JwtTokenUtil jwtTokenUtil, AuthorityRepository authorityRepository) {
        this.configRepository = configRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public ConfigResponse saveConfig(ConfigDto configDto) {
        Config config = new Config();
        config.setId(1L);
        config.setActiveState(configDto.getActiveState());
        config.setActiveYear(configDto.getActiveYear());
        config.setElectionLevel(configDto.getElectionLevel());

        configRepository.save(config);
        return new ConfigResponse("00", "Config setup Successfully.", config);
    }

    @Override
    public ConfigResponse getConfigById(long configId) throws NotFoundException {
        Optional<Config> config = configRepository.findById(configId);
        if(!config.isPresent()){
            throw new NotFoundException("Config not found");
        }
        return new ConfigResponse("00","Config data retrieved", config.get());
    }

}
