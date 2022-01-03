package com.edunge.srtool.service;

import com.edunge.srtool.dto.ConfigDto;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.Config;
import com.edunge.srtool.response.ConfigResponse;

public interface ConfigService {
    ConfigResponse saveConfig(ConfigDto configDto);
    ConfigResponse getConfigById(long configId) throws NotFoundException;
}
