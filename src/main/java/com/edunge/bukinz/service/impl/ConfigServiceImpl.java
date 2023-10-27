package com.edunge.bukinz.service.impl;

import com.edunge.bukinz.dto.ConfigDto;
import com.edunge.bukinz.exceptions.NotFoundException;
import com.edunge.bukinz.jwt.JwtTokenUtil;
import com.edunge.bukinz.model.Config;
import com.edunge.bukinz.repository.AuthorityRepository;
import com.edunge.bukinz.repository.ConfigRepository;
import com.edunge.bukinz.response.ConfigResponse;
import com.edunge.bukinz.service.ConfigService;
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
