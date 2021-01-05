package com.edunge.srtool.service;

import com.edunge.srtool.dto.WardDto;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.WardResponse;

public interface WardService {
    WardResponse saveWard(WardDto ward) throws NotFoundException;
    WardResponse findWardById(Long id) throws NotFoundException;
    WardResponse findWardByCode(String code) throws NotFoundException;
    WardResponse updateWard(Long id, WardDto ward) throws NotFoundException;
    WardResponse deleteWardById(Long id) throws NotFoundException;
    WardResponse findAll() ;
    WardResponse filterByName(String name) throws NotFoundException;
    WardResponse findByLga(Long lgaCode) throws NotFoundException;
    WardResponse searchWardByFilter(Long stateId, Long senatorialDistrictId, Long lgaId);
}
