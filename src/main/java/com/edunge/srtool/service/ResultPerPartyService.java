package com.edunge.srtool.service;

import com.edunge.srtool.dto.ResultPerPartyDto;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.ResultPerPartyResponse;

public interface ResultPerPartyService {
    ResultPerPartyResponse saveResultPerParty(ResultPerPartyDto resultPerPartyDto) throws NotFoundException;
    ResultPerPartyResponse findResultPerPartyById(Long id) throws NotFoundException;
    ResultPerPartyResponse updateResultPerParty(Long id, ResultPerPartyDto resultPerPartyDto) throws NotFoundException;
    ResultPerPartyResponse deleteResultPerPartyById(Long id) throws NotFoundException;
    ResultPerPartyResponse findAll() ;
}
