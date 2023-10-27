package com.edunge.bukinz.service;

import com.edunge.bukinz.dto.ConfigDto;
import com.edunge.bukinz.exceptions.NotFoundException;
import com.edunge.bukinz.response.ConfigResponse;

public interface ConfigService {
    ConfigResponse saveConfig(ConfigDto configDto);
    ConfigResponse getConfigById(long configId) throws NotFoundException;
}
