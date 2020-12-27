package com.edunge.srtool.service.impl;

import com.edunge.srtool.dto.PartyAgentDto;
import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.*;
import com.edunge.srtool.repository.*;
import com.edunge.srtool.response.PartyAgentResponse;
import com.edunge.srtool.service.PartyAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartyAgentServiceImpl implements PartyAgentService {

    private final LgaRepository lgaRepository;
    private final PartyAgentRepository partyAgentRepository;
    private final PollingUnitRepository pollingUnitRepository;
    private final WardRepository wardRepository;
    private final PoliticalPartyRepository politicalPartyRepository;

    private static final String SERVICE_NAME = "Party Agent";

    @Value("${notfound.message.template}")
    private String notFoundTemplate;

    @Value("${success.message.template}")
    private String successTemplate;

    @Value("${duplicate.message.template}")
    private String duplicateTemplate;

    @Value("${update.message.template}")
    private String updateTemplate;

    @Value("${delete.message.template}")
    private String deleteTemplate;

    @Value("${fetch.message.template}")
    private String fetchRecordTemplate;

    @Autowired
    public PartyAgentServiceImpl(LgaRepository lgaRepository, PartyAgentRepository partyAgentRepository, PollingUnitRepository pollingUnitRepository, WardRepository wardRepository, PoliticalPartyRepository politicalPartyRepository) {
        this.lgaRepository = lgaRepository;
        this.partyAgentRepository = partyAgentRepository;
        this.pollingUnitRepository = pollingUnitRepository;
        this.wardRepository = wardRepository;
        this.politicalPartyRepository = politicalPartyRepository;
    }

    @Override
    public PartyAgentResponse savePartyAgent(PartyAgentDto partyAgentDto) throws NotFoundException {
        PollingUnit pollingUnit = getPollingUnit(partyAgentDto.getPollingUnitId());
        PoliticalParty politicalParty = getPoliticalParty(partyAgentDto.getPollingUnitId());
        Lga lga = getLga(partyAgentDto.getLgaId());
        Ward ward = wardRepository.findByCode(partyAgentDto.getCode());
        PartyAgent partyAgent = partyAgentRepository.findByEmail(partyAgentDto.getEmail());
        if(partyAgent==null){
            partyAgent  = new PartyAgent();
            partyAgent.setFirstname(partyAgentDto.getFirstname());
            partyAgent.setLastname(partyAgentDto.getLastname());
            partyAgent.setAddress(partyAgentDto.getAddress());
            partyAgent.setEmail(partyAgentDto.getEmail());
            partyAgent.setPhone(partyAgentDto.getPhone());
            partyAgent.setCode(partyAgentDto.getCode());
            partyAgent.setName(partyAgentDto.getName());
            partyAgent.setPollingUnit(pollingUnit);
            partyAgent.setWard(ward);
            partyAgent.setPoliticalParty(politicalParty);
            partyAgent.setLga(lga);
            partyAgentRepository.save(partyAgent);
            return new PartyAgentResponse("00", String.format(successTemplate, SERVICE_NAME), partyAgent);
        }
        throw new DuplicateException(String.format(duplicateTemplate, partyAgent.getCode()));
    }

    @Override
    public PartyAgentResponse findPartyAgentById(Long id) throws NotFoundException {
        PartyAgent partyAgent = getPartyAgent(id);
        return new PartyAgentResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), partyAgent);
    }

    @Override
    public PartyAgentResponse findPartyAgentByName(String name) throws NotFoundException {
        PartyAgent partyAgent = partyAgentRepository.findByFirstnameOrLastName(name);
        if(partyAgent==null){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return new PartyAgentResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), partyAgent);
    }

    @Override
    public PartyAgentResponse findPartyAgentByPhone(String phone) throws NotFoundException {
        PartyAgent partyAgent = partyAgentRepository.findByPhone(phone);
        if(partyAgent==null){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return new PartyAgentResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), partyAgent);
    }

    @Override
    public PartyAgentResponse updatePartyAgent(Long id, PartyAgentDto partyAgentDto) throws NotFoundException {

        PollingUnit pollingUnit = getPollingUnit(partyAgentDto.getPollingUnitId());
        PoliticalParty politicalParty = getPoliticalParty(partyAgentDto.getPollingUnitId());
        Lga lga = getLga(partyAgentDto.getLgaId());
        Ward ward = wardRepository.findByCode(partyAgentDto.getCode());
        PartyAgent partyAgent = getPartyAgent(id);
        partyAgent.setFirstname(partyAgentDto.getFirstname());
        partyAgent.setLastname(partyAgentDto.getLastname());
        partyAgent.setAddress(partyAgentDto.getAddress());
        partyAgent.setEmail(partyAgentDto.getEmail());
        partyAgent.setPhone(partyAgentDto.getPhone());
        partyAgent.setCode(partyAgentDto.getCode());
        partyAgent.setName(partyAgentDto.getName());
        partyAgent.setPollingUnit(pollingUnit);
        partyAgent.setWard(ward);
        partyAgent.setPoliticalParty(politicalParty);
        partyAgent.setLga(lga);
        partyAgentRepository.save(partyAgent);
        return new PartyAgentResponse("00", String.format(successTemplate, SERVICE_NAME), partyAgent);
    }

    @Override
    public PartyAgentResponse deletePartyAgentById(Long id) throws NotFoundException {
        PartyAgent partyAgent = getPartyAgent(id);
        partyAgentRepository.deleteById(id);
        return new PartyAgentResponse("00",String.format(deleteTemplate,partyAgent.getCode()));
    }

    @Override
    public PartyAgentResponse findAll() {
        List<PartyAgent> partyAgents = partyAgentRepository.findAll();
        return new PartyAgentResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), partyAgents);
    }


    private Lga getLga(Long id) throws NotFoundException {
        Optional<Lga> currentLga = lgaRepository.findById(id);
        if(!currentLga.isPresent()){
            throw new NotFoundException("Lga not found.");
        }
        return currentLga.get();
    }

    private PartyAgent getPartyAgent(Long id) throws NotFoundException {
        Optional<PartyAgent> partyAgent = partyAgentRepository.findById(id);
        if(!partyAgent.isPresent()){
            throw new NotFoundException("State not found.");
        }
        return partyAgent.get();
    }


    private PollingUnit getPollingUnit(Long id) throws NotFoundException {
        Optional<PollingUnit> pollingUnit = pollingUnitRepository.findById(id);
        if(!pollingUnit.isPresent()){
            throw new NotFoundException("Senatorial District not found.");
        }
        return pollingUnit.get();
    }

    private PoliticalParty getPoliticalParty(Long id) throws NotFoundException {
        Optional<PoliticalParty> politicalParty = politicalPartyRepository.findById(id);
        if(!politicalParty.isPresent()){
            throw new NotFoundException(String.format(fetchRecordTemplate, SERVICE_NAME));
        }
        return politicalParty.get();
    }

    private Ward getWard(Long id) throws NotFoundException {
        Optional<Ward> currentWard = wardRepository.findById(id);
        if(!currentWard.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return currentWard.get();
    }
}
