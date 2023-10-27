package com.edunge.bukinz.service;

import com.edunge.bukinz.dto.PollingUnitDto;
import com.edunge.bukinz.exceptions.NotFoundException;
import com.edunge.bukinz.model.Lga;
import com.edunge.bukinz.model.SenatorialDistrict;
import com.edunge.bukinz.model.State;
import com.edunge.bukinz.model.Ward;
import com.edunge.bukinz.response.PollingUnitResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PollingUnitService {
    PollingUnitResponse savePollingUnit(PollingUnitDto pollingUnitDto) throws NotFoundException;
    PollingUnitResponse findPollingUnitById(Long id) throws NotFoundException;
    PollingUnitResponse findPollingUnitByCode(String code) throws NotFoundException;
    PollingUnitResponse updatePollingUnit(Long id, PollingUnitDto pollingUnitDto) throws NotFoundException;
    void updatePollingUnitWard(Long wardOld, Ward wardNew);
    void updatePollingUnitLga(Long lgaOld, Lga lgaNew);
    void updatePollingUnitDistrict(Long districtOld, SenatorialDistrict districtNew);

    PollingUnitResponse findByLga(Long lgaCode) throws NotFoundException;

    long findCountByLga(Long lgaCode);
    long findCountByWard(Long wardCode);
    long findCountBySenatorialDistrict(Long districtCode);
    long findCountByState(Long stateCode);
    PollingUnitResponse findByState(Long stateCode) throws NotFoundException;

    long countByState(State state);
    long countBySenatorialDistrict(SenatorialDistrict senatorialDistrict);
    long countByLga(Lga lga);
    long countByWard(Ward ward);
    long countPollingUnit();
    PollingUnitResponse findBySenatorialDistrict(Long senatorialDistrictCode) throws NotFoundException;

    PollingUnitResponse deletePollingUnitById(Long id) throws NotFoundException;
    PollingUnitResponse findAll() ;
    PollingUnitResponse filterByName(String name) throws NotFoundException;
    PollingUnitResponse findByWardCode(Long wardCode) throws NotFoundException;
    PollingUnitResponse uploadPollingUnit(MultipartFile file);
}
