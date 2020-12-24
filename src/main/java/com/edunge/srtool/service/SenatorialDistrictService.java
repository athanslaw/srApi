package com.edunge.srtool.service;

import com.edunge.srtool.dto.SenatorialDistrictDto;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.SenatorialDistrict;
import com.edunge.srtool.response.SenatorialDistrictResponse;

public interface SenatorialDistrictService {
    SenatorialDistrictResponse saveSenatorialDistrict(SenatorialDistrictDto senatorialDistrictDto) throws NotFoundException;
    SenatorialDistrictResponse findSenatorialDistrictById(Long id) throws NotFoundException;
    SenatorialDistrictResponse findSenatorialDistrictByCode(String code) throws NotFoundException;
    SenatorialDistrictResponse updateSenatorialDistrict(Long id, SenatorialDistrictDto senatorialDistrict) throws NotFoundException;
    SenatorialDistrictResponse deleteSenatorialDistrictById(Long id) throws NotFoundException;
    SenatorialDistrictResponse findAll() ;
}
