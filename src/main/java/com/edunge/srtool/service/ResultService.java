package com.edunge.srtool.service;

import com.edunge.srtool.dto.ResultDto;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.Election;
import com.edunge.srtool.response.ResultRealTimeResponse;
import com.edunge.srtool.response.ResultResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ResultService {
    ResultResponse saveResult(ResultDto resultDto) throws NotFoundException;
    ResultResponse findResultById(Long id) throws NotFoundException;
    ResultResponse updateResult(Long id, ResultDto resultDto) throws NotFoundException;
    ResultResponse deleteResultById(Long id) throws NotFoundException;
    ResultResponse findAll() ;
    ResultResponse findByStateId(Long stateId, Long electionType) throws NotFoundException;
    ResultResponse findByZoneId(Long zoneId, Long electionType) throws NotFoundException;
    Election getElection() throws NotFoundException;
    ResultResponse filterByLga(Long lgaId, Long electionType) throws NotFoundException;

    ResultResponse filterBySenatorialDistrict(Long id, Long electionType) throws NotFoundException;

    ResultResponse filterByWard(Long wardId, Long electionType) throws NotFoundException;

    ResultResponse filterByPollingUnit(Long pollingUnitId, Long electionType) throws NotFoundException;

    ResultResponse uploadResult(MultipartFile file);
}
