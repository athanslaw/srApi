package com.edunge.srtool.service.impl;

import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.PoliticalParty;
import com.edunge.srtool.model.State;
import com.edunge.srtool.repository.PoliticalPartyRepository;
import com.edunge.srtool.repository.StateRepository;
import com.edunge.srtool.response.PoliticalPartyResponse;
import com.edunge.srtool.response.StateResponse;
import com.edunge.srtool.service.PoliticalPartyService;
import com.edunge.srtool.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PoliticalPartyServiceImpl implements PoliticalPartyService {
    private final PoliticalPartyRepository politicalPartyRepository;

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
    public PoliticalPartyServiceImpl(PoliticalPartyRepository politicalPartyRepository) {
        this.politicalPartyRepository = politicalPartyRepository;
    }

    @Override
    public PoliticalPartyResponse savePoliticalParty(PoliticalParty politicalParty) throws NotFoundException {
        PoliticalParty existingPoliticalParty = politicalPartyRepository.findByCode(politicalParty.getCode());
        if(existingPoliticalParty==null){
            politicalPartyRepository.save(politicalParty);
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
    public PoliticalPartyResponse editPoliticalParty(Long id, PoliticalParty politicalParty) throws NotFoundException {
        PoliticalParty currentPoliticalParty =  getPoliticalParty(id);
        currentPoliticalParty.setId(id);
        currentPoliticalParty.setCode(politicalParty.getCode());
        currentPoliticalParty.setName(politicalParty.getName());
        politicalPartyRepository.save(currentPoliticalParty);
        return new PoliticalPartyResponse("00", String.format(updateTemplate, SERVICE_NAME), currentPoliticalParty);
    }

    @Override
    public PoliticalPartyResponse deletePoliticalPartyById(Long id) throws NotFoundException {
        PoliticalParty currentPoliticalParty = getPoliticalParty(id);
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
}
