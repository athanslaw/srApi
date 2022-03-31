package com.edunge.srtool.service;

import com.edunge.srtool.dto.PartyAgentDto;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.PartyAgentResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PartyAgentService {
    PartyAgentResponse savePartyAgent(PartyAgentDto partyAgentDto) throws NotFoundException;
    PartyAgentResponse findPartyAgentById(Long id) throws NotFoundException;
    PartyAgentResponse updatePartyAgent(Long id, PartyAgentDto partyAgentDto) throws NotFoundException;
    PartyAgentResponse deletePartyAgentById(Long id) throws NotFoundException;
    PartyAgentResponse findPartyAgentByName(String firstName, String lastname)throws NotFoundException;
    PartyAgentResponse findPartyAgentByLga(Long lgaId) throws NotFoundException;
    PartyAgentResponse findPartyAgentBySenatorialDistrict(Long lgaId) throws NotFoundException;
    PartyAgentResponse findPartyAgentByWard(Long wardId) throws NotFoundException;
    PartyAgentResponse findPartyAgentByPollingUnit(Long pollingUnitId) throws NotFoundException;
    PartyAgentResponse findPartyAgentByPhone(String phone)throws NotFoundException;
    PartyAgentResponse findAll() ;
    PartyAgentResponse uploadPartyAgent(MultipartFile file);
}
