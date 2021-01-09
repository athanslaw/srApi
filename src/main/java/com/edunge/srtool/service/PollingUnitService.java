package com.edunge.srtool.service;

import com.edunge.srtool.dto.PollingUnitDto;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.PollingUnitResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PollingUnitService {
    PollingUnitResponse savePollingUnit(PollingUnitDto pollingUnitDto) throws NotFoundException;
    PollingUnitResponse findPollingUnitById(Long id) throws NotFoundException;
    PollingUnitResponse findPollingUnitByCode(String code) throws NotFoundException;
    PollingUnitResponse updatePollingUnit(Long id, PollingUnitDto pollingUnitDto) throws NotFoundException;
    PollingUnitResponse deletePollingUnitById(Long id) throws NotFoundException;
    PollingUnitResponse findAll() ;
    PollingUnitResponse filterByName(String name) throws NotFoundException;
    PollingUnitResponse findByWardCode(Long wardCode) throws NotFoundException;

    PollingUnitResponse uploadPollingUnit(MultipartFile file);
}
