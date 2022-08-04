package com.edunge.srtool.service;

import com.edunge.srtool.dto.WardDto;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.Lga;
import com.edunge.srtool.model.SenatorialDistrict;
import com.edunge.srtool.model.State;
import com.edunge.srtool.model.Ward;
import com.edunge.srtool.response.WardResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WardService {
    WardResponse saveWard(WardDto ward) throws NotFoundException;
    WardResponse findWardById(Long id) throws NotFoundException;
    List<Ward> findWardByState(Long id);
    long countWardByState(State state);
    long countWardBySenatorialDistrict(SenatorialDistrict senatorialDistrict);
    long countWardByLga(Lga lga);
    long countWard();
    void updateWardLga(Long lgaOld, Lga lgaNew) throws NotFoundException;
    void updateWardDistrict(Long districtOld, SenatorialDistrict districtNew) throws NotFoundException;

    WardResponse findWardByCode(String code) throws NotFoundException;
    WardResponse updateWard(Long id, WardDto ward) throws NotFoundException;
    WardResponse deleteWardById(Long id) throws NotFoundException;
    WardResponse findAll() ;
    WardResponse filterByName(String name) throws NotFoundException;
    WardResponse findByLga(Long lgaCode) throws NotFoundException;
    WardResponse searchWardByFilter(Long stateId, Long senatorialDistrictId, Long lgaId);

    WardResponse uploadWard(MultipartFile file);
}
