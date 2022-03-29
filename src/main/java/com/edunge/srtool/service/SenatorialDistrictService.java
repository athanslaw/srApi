package com.edunge.srtool.service;

import com.edunge.srtool.dto.SenatorialDistrictDto;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.SenatorialDistrictResponse;
import org.springframework.web.multipart.MultipartFile;

public interface SenatorialDistrictService {
    SenatorialDistrictResponse saveSenatorialDistrict(SenatorialDistrictDto senatorialDistrictDto) throws NotFoundException;
    SenatorialDistrictResponse findSenatorialDistrictById(Long id) throws NotFoundException;
    SenatorialDistrictResponse findSenatorialDistrictByCode(String code) throws NotFoundException;
    SenatorialDistrictResponse updateSenatorialDistrict(Long id, SenatorialDistrictDto senatorialDistrict) throws NotFoundException;
    SenatorialDistrictResponse deleteSenatorialDistrictById(Long id) throws NotFoundException;
    SenatorialDistrictResponse findAll() ;
    SenatorialDistrictResponse filterByName(String name) throws NotFoundException;
    SenatorialDistrictResponse findSenatorialDistrictByStateCode(Long stateCode) throws NotFoundException;
    SenatorialDistrictResponse findSenatorialDistrictForDefaultState() throws NotFoundException;

    SenatorialDistrictResponse uploadSenatorialDistrict(MultipartFile file);
}
