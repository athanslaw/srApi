package com.edunge.srtool.service;

import com.edunge.srtool.dto.LgaDto;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.Lga;
import com.edunge.srtool.response.LgaResponse;

public interface LgaService {
    LgaResponse saveLga(LgaDto lga) throws NotFoundException;
    LgaResponse findLgaById(Long id) throws NotFoundException;
    LgaResponse findLgaByCode(String code) throws NotFoundException;
    LgaResponse updateLga(Long id, LgaDto lga) throws NotFoundException;
    LgaResponse deleteLgaById(Long id) throws NotFoundException;
    LgaResponse findAll() ;
}
