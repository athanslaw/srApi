package com.edunge.srtool.service.impl;

import com.edunge.srtool.dto.PoliticalPartyDto;
import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.PoliticalParty;
import com.edunge.srtool.model.State;
import com.edunge.srtool.repository.PoliticalPartyRepository;
import com.edunge.srtool.repository.StateRepository;
import com.edunge.srtool.response.PoliticalPartyResponse;
import com.edunge.srtool.service.PoliticalPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PoliticalPartyServiceImpl implements PoliticalPartyService {
    private final PoliticalPartyRepository politicalPartyRepository;
    private final StateRepository stateRepository;

    private static final String SERVICE_NAME = "Political Party";

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
    public PoliticalPartyServiceImpl(PoliticalPartyRepository politicalPartyRepository, StateRepository stateRepository) {
        this.politicalPartyRepository = politicalPartyRepository;
        this.stateRepository = stateRepository;
    }

    @Override
    public PoliticalPartyResponse savePoliticalParty(PoliticalPartyDto politicalParty) throws NotFoundException {
        PoliticalParty existingPoliticalParty = politicalPartyRepository.findByCode(politicalParty.getCode());
        if(existingPoliticalParty==null){
//            State state = getState(politicalParty.getStateId());
            existingPoliticalParty = new PoliticalParty();
//            existingPoliticalParty.setState(state);
            existingPoliticalParty.setCode(politicalParty.getCode());
            existingPoliticalParty.setName(politicalParty.getName());
            politicalPartyRepository.save(existingPoliticalParty);
            return new PoliticalPartyResponse("00", String.format(successTemplate,SERVICE_NAME), existingPoliticalParty);
        }
        throw new DuplicateException(String.format(duplicateTemplate, politicalParty.getCode()));
    }

    @Override
    public PoliticalPartyResponse findPoliticalPartyById(Long id) throws NotFoundException {
        PoliticalParty currentPoliticalParty = getPoliticalParty(id);
        return new PoliticalPartyResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), currentPoliticalParty);
    }

    @Override
    public PoliticalPartyResponse findPoliticalPartyByCode(String code) throws NotFoundException {
        PoliticalParty currentPoliticalParty = politicalPartyRepository.findByCode(code);
        if(currentPoliticalParty==null){
            throw new NotFoundException(String.format(notFoundTemplate, SERVICE_NAME + " " +code));
        }
        return new PoliticalPartyResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), currentPoliticalParty);
    }

    @Override
    public PoliticalPartyResponse editPoliticalParty(Long id, PoliticalPartyDto politicalParty) throws NotFoundException {
        PoliticalParty currentPoliticalParty =  getPoliticalParty(id);
        currentPoliticalParty.setId(id);
        currentPoliticalParty.setName(politicalParty.getName());
        politicalPartyRepository.save(currentPoliticalParty);
        return new PoliticalPartyResponse("00", String.format(updateTemplate, SERVICE_NAME), currentPoliticalParty);
    }


    @Override
    public PoliticalPartyResponse filterByName(String name) throws NotFoundException {
        List<PoliticalParty> politicalParty = politicalPartyRepository.findByNameStartingWith(name);
        if(politicalParty!=null){
            return new PoliticalPartyResponse("00", String.format(successTemplate,SERVICE_NAME), politicalParty);
        }
        throw new NotFoundException(String.format(notFoundTemplate, SERVICE_NAME));
    }

    @Override
    public PoliticalPartyResponse deletePoliticalPartyById(Long id) throws NotFoundException {
        PoliticalParty currentPoliticalParty = getPoliticalParty(id);
        politicalPartyRepository.deleteById(id);
        return new PoliticalPartyResponse("00",String.format(deleteTemplate,currentPoliticalParty.getCode()));
    }

    @Override
    public PoliticalPartyResponse findAll() {
        List<PoliticalParty> politicalParties = politicalPartyRepository.findAll();
        return new PoliticalPartyResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), politicalParties);
    }

    private PoliticalParty getPoliticalParty(Long id) throws NotFoundException {
        Optional<PoliticalParty> currentState = politicalPartyRepository.findById(id);
        if(!currentState.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate, SERVICE_NAME));
        }
        return currentState.get();
    }

    private State getState(Long id) throws NotFoundException {
        Optional<State> currentState = stateRepository.findById(id);
        if(!currentState.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate, "State"));
        }
        return currentState.get();
    }
}
