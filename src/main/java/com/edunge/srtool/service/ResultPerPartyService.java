package com.edunge.srtool.service;

import com.edunge.srtool.dto.ResultPerPartyDto;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.ResultPerPartyResponse;

public interface ResultPerPartyService {
    ResultPerPartyResponse saveResult(ResultPerPartyDto resultPerPartyDto) throws NotFoundException;
    ResultPerPartyResponse findResultById(Long id) throws NotFoundException;
    ResultPerPartyResponse updateResult(Long id, ResultPerPartyDto resultPerPartyDto) throws NotFoundException;
    ResultPerPartyResponse deleteResultById(Long id) throws NotFoundException;
    ResultPerPartyResponse findAll() ;
}
