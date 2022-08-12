package com.edunge.srtool.service;

import com.edunge.srtool.dto.ResultDto;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.Election;
import com.edunge.srtool.response.ResultRealTimeResponse;
import com.edunge.srtool.response.ResultResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ResultService {
    ResultResponse saveResult(ResultDto ward) throws NotFoundException;
    ResultResponse findResultById(Long id) throws NotFoundException;
    ResultResponse updateResult(Long id, ResultDto ward) throws NotFoundException;
    ResultResponse deleteResultById(Long id) throws NotFoundException;
    ResultResponse findAll() ;
    ResultResponse findByStateId(Long stateId) ;
    Election getElection() throws NotFoundException;
    ResultResponse filterByLga(Long lgaId) throws NotFoundException;

    ResultResponse filterBySenatorialDistrict(Long id) throws NotFoundException;

    ResultResponse filterByWard(Long wardId) throws NotFoundException;

    ResultResponse filterByPollingUnit(Long pollingUnitId) throws NotFoundException;

    ResultResponse uploadResult(MultipartFile file);
}
