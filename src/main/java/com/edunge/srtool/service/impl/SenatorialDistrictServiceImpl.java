package com.edunge.srtool.service.impl;

import com.edunge.srtool.dto.SenatorialDistrictDto;
import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.SenatorialDistrict;
import com.edunge.srtool.model.State;
import com.edunge.srtool.repository.SenatorialDistrictRepository;
import com.edunge.srtool.repository.StateRepository;
import com.edunge.srtool.response.SenatorialDistrictResponse;
import com.edunge.srtool.service.SenatorialDistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SenatorialDistrictServiceImpl implements SenatorialDistrictService {

    private final StateRepository stateRepository;
    private final SenatorialDistrictRepository senatorialDistrictRepository;
    private static final String SERVICE_NAME = "Senatorial District";

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
    public SenatorialDistrictServiceImpl(StateRepository stateRepository, SenatorialDistrictRepository senatorialDistrictRepository) {
        this.stateRepository = stateRepository;
        this.senatorialDistrictRepository = senatorialDistrictRepository;
    }

    @Override
    public SenatorialDistrictResponse saveSenatorialDistrict(SenatorialDistrictDto senatorialDistrictDto) throws NotFoundException {
        State state = getState(senatorialDistrictDto.getStateId());
        SenatorialDistrict senatorialDistrict = senatorialDistrictRepository.findByCode(senatorialDistrictDto.getCode());
        if(senatorialDistrict==null){
            senatorialDistrict = new SenatorialDistrict();
            senatorialDistrict.setState(state);
            senatorialDistrict.setCode(senatorialDistrictDto.getCode());
            senatorialDistrict.setName(senatorialDistrictDto.getName());
            senatorialDistrictRepository.save(senatorialDistrict);
            return new SenatorialDistrictResponse("00", "Senatorial District saved successfully", senatorialDistrict);
        }
        throw new DuplicateException(String.format("%s already exist.", senatorialDistrictDto.getCode()));
    }

    @Override
    public SenatorialDistrictResponse findSenatorialDistrictById(Long id) throws NotFoundException {
        SenatorialDistrict senatorialDistrict = getSenatorialDistrict(id);
        return new SenatorialDistrictResponse("00", "Senatorial District retrieved successfully", senatorialDistrict);
    }

    @Override
    public SenatorialDistrictResponse findSenatorialDistrictByCode(String code) throws NotFoundException {
        SenatorialDistrict senatorialDistrict = senatorialDistrictRepository.findByCode(code);
        if(senatorialDistrict==null){
            throw new NotFoundException("Senatorial District not found.");
        }
        return new SenatorialDistrictResponse("00", "Senatorial District retrieved successfully", senatorialDistrict);
    }

    @Override
    public SenatorialDistrictResponse updateSenatorialDistrict(Long id, SenatorialDistrictDto senatorialDistrictDto) throws NotFoundException {
        SenatorialDistrict currentSenatorialDistrict = getSenatorialDistrict(id);
        State state = getState(senatorialDistrictDto.getStateId());
        currentSenatorialDistrict.setId(id);
        currentSenatorialDistrict.setCode(senatorialDistrictDto.getCode());
        currentSenatorialDistrict.setName(senatorialDistrictDto.getName());
        currentSenatorialDistrict.setState(state);
        senatorialDistrictRepository.save(currentSenatorialDistrict);
        return new SenatorialDistrictResponse("00", "Senatorial District updated successfully", currentSenatorialDistrict);
    }

    @Override
    public SenatorialDistrictResponse deleteSenatorialDistrictById(Long id) throws NotFoundException {
        SenatorialDistrict currentLga = getSenatorialDistrict(id);
        return new SenatorialDistrictResponse("00",String.format("%s deleted successfully.",currentLga.getCode()));
    }

    @Override
    public SenatorialDistrictResponse findAll() {
        List<SenatorialDistrict> senatorialDistricts = senatorialDistrictRepository.findAll();
        return new SenatorialDistrictResponse("00", "All Senatorial District retrieved.", senatorialDistricts);
    }

    @Override
    public SenatorialDistrictResponse filterByName(String name) throws NotFoundException {
        SenatorialDistrict senatorialDistrict = senatorialDistrictRepository.findByNameStartingWith(name);
        if(senatorialDistrict!=null){
            return new SenatorialDistrictResponse("00", String.format(successTemplate,SERVICE_NAME), senatorialDistrict);
        }
        throw new NotFoundException(String.format(notFoundTemplate, SERVICE_NAME));
    }

    private SenatorialDistrict getSenatorialDistrict(Long id) throws NotFoundException {
        Optional<SenatorialDistrict> senatorialDistrict = senatorialDistrictRepository.findById(id);
        if(!senatorialDistrict.isPresent()){
            throw new NotFoundException("Senatorial District not found.");
        }
        return senatorialDistrict.get();
    }

    private State getState(Long id) throws NotFoundException {
        Optional<State> currentState = stateRepository.findById(id);
        if(!currentState.isPresent()){
            throw new NotFoundException("State not found.");
        }
        return currentState.get();
    }
}
