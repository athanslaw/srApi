package com.edunge.srtool.service;

import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.PoliticalParty;
import com.edunge.srtool.response.PoliticalPartyResponse;

public interface PoliticalPartyService {
    PoliticalPartyResponse savePoliticalParty(PoliticalParty politicalParty) throws NotFoundException;
    PoliticalPartyResponse findPoliticalPartyById(Long id) throws NotFoundException;
    PoliticalPartyResponse findPoliticalPartyByCode(String code) throws NotFoundException;
    PoliticalPartyResponse editPoliticalParty(Long id, PoliticalParty politicalParty) throws NotFoundException;
    PoliticalPartyResponse deletePoliticalPartyById(Long id) throws NotFoundException;
    PoliticalPartyResponse findAll() ;
    PoliticalPartyResponse filterByName(String name) throws NotFoundException;
}
