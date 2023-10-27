package com.edunge.bukinz.service;

import com.edunge.bukinz.dto.WardDto;
import com.edunge.bukinz.exceptions.NotFoundException;
import com.edunge.bukinz.model.Lga;
import com.edunge.bukinz.model.SenatorialDistrict;
import com.edunge.bukinz.model.State;
import com.edunge.bukinz.model.Ward;
import com.edunge.bukinz.response.WardResponse;
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
