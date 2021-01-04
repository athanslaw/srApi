package com.edunge.srtool.service;

import com.edunge.srtool.dto.PoliticalPartyDto;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.PoliticalPartyResponse;

public interface PoliticalPartyService {
    PoliticalPartyResponse savePoliticalParty(PoliticalPartyDto politicalParty) throws NotFoundException;
    PoliticalPartyResponse findPoliticalPartyById(Long id) throws NotFoundException;
    PoliticalPartyResponse findPoliticalPartyByCode(String code) throws NotFoundException;
    PoliticalPartyResponse editPoliticalParty(Long id, PoliticalPartyDto politicalParty) throws NotFoundException;
    PoliticalPartyResponse deletePoliticalPartyById(Long id) throws NotFoundException;
    PoliticalPartyResponse findAll() ;
    PoliticalPartyResponse filterByName(String name) throws NotFoundException;
}
