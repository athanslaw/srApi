package com.edunge.srtool.service;

import com.edunge.srtool.dto.LgaDto;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.LgaResponse;
import org.springframework.web.multipart.MultipartFile;

public interface LgaService {
    LgaResponse saveLga(LgaDto lga) throws NotFoundException;
    LgaResponse findLgaById(Long id) throws NotFoundException;
    LgaResponse findLgaByCode(String code) throws NotFoundException;
    LgaResponse updateLga(Long id, LgaDto lga) throws NotFoundException;
    LgaResponse deleteLgaById(Long id) throws NotFoundException;
    LgaResponse findAll() ;
    LgaResponse filterByName(String name) throws NotFoundException;
    LgaResponse findLgaByStateCode(Long stateCode) throws NotFoundException;
    LgaResponse findLgaFilter(Long stateId, Long senatorialDistrictId) throws NotFoundException;
    LgaResponse findLgaBySenatorialDistrictCode(Long senatorialDistrictId) throws NotFoundException;

    LgaResponse uploadLga(MultipartFile file);
}
