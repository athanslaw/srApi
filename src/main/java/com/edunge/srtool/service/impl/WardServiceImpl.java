package com.edunge.srtool.service.impl;

import com.edunge.srtool.dto.WardDto;
import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.*;
import com.edunge.srtool.repository.LgaRepository;
import com.edunge.srtool.repository.SenatorialDistrictRepository;
import com.edunge.srtool.repository.StateRepository;
import com.edunge.srtool.repository.WardRepository;
import com.edunge.srtool.response.WardResponse;
import com.edunge.srtool.service.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WardServiceImpl implements WardService {

    private final LgaRepository lgaRepository;
    private final StateRepository stateRepository;
    private final SenatorialDistrictRepository senatorialDistrictRepository;
    private final WardRepository wardRepository;

    private static final String SERVICE_NAME = "Ward";

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
    public WardServiceImpl(LgaRepository lgaRepository, StateRepository stateRepository, SenatorialDistrictRepository senatorialDistrictRepository, WardRepository wardRepository) {
        this.lgaRepository = lgaRepository;
        this.stateRepository = stateRepository;
        this.senatorialDistrictRepository = senatorialDistrictRepository;
        this.wardRepository = wardRepository;
    }

    @Override
    public WardResponse saveWard(WardDto wardDto) throws NotFoundException {
        State state = getState(wardDto.getStateId());
        SenatorialDistrict senatorialDistrict = getSenatorialDistrict(wardDto.getSenatorialDistrictId());
        Lga lga = getLga(wardDto.getLgaId());
        Ward ward = wardRepository.findByCode(wardDto.getCode());
        if(ward==null){
            ward = new Ward();
            ward.setSenatorialDistrict(senatorialDistrict);
            ward.setState(state);
            ward.setCode(wardDto.getCode());
            ward.setName(wardDto.getName());
            ward.setLga(lga);
            wardRepository.save(ward);
            return new WardResponse("00", String.format(successTemplate,SERVICE_NAME), ward);
        }
        throw new DuplicateException(String.format(duplicateTemplate, wardDto.getCode()));
    }

    @Override
    public WardResponse findWardById(Long id) throws NotFoundException {
        Ward currentWard = getWard(id);
        return new WardResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), currentWard);
    }

    @Override
    public WardResponse findWardByCode(String code) throws NotFoundException {
        Ward currentWard = wardRepository.findByCode(code);
        if(currentWard==null){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return new WardResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), currentWard);
    }

    @Override
    public WardResponse updateWard(Long id, WardDto wardDto) throws NotFoundException {
        State state = getState(wardDto.getStateId());
        SenatorialDistrict senatorialDistrict = getSenatorialDistrict(wardDto.getSenatorialDistrictId());
        Lga lga = getLga(wardDto.getLgaId());
        Ward currentWard = getWard(id);
        currentWard.setId(id);
        currentWard.setCode(wardDto.getCode());
        currentWard.setName(wardDto.getName());
        currentWard.setState(state);
        currentWard.setSenatorialDistrict(senatorialDistrict);
        currentWard.setLga(lga);
        wardRepository.save(currentWard);
        return new WardResponse("00", String.format(successTemplate, SERVICE_NAME), currentWard);
    }

    @Override
    public WardResponse deleteWardById(Long id) throws NotFoundException {
        Ward currentWard = getWard(id);
        return new WardResponse("00",String.format(deleteTemplate,currentWard.getCode()));
    }

    @Override
    public WardResponse findAll() {
        List<Ward> wards = wardRepository.findAll();
        return new WardResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), wards);
    }

    @Override
    public WardResponse filterByName(String name) throws NotFoundException {
        List<Ward> ward = wardRepository.findByNameStartingWith(name);
        if(ward!=null){
            return new WardResponse("00", String.format(successTemplate,SERVICE_NAME), ward);
        }
        throw new NotFoundException("Ward not found.");
    }


    private Lga getLga(Long id) throws NotFoundException {
        Optional<Lga> currentLga = lgaRepository.findById(id);
        if(!currentLga.isPresent()){
            throw new NotFoundException("Lga not found.");
        }
        return currentLga.get();
    }

    private State getState(Long id) throws NotFoundException {
        Optional<State> currentState = stateRepository.findById(id);
        if(!currentState.isPresent()){
            throw new NotFoundException("State not found.");
        }
        return currentState.get();
    }

    private SenatorialDistrict getSenatorialDistrict(Long id) throws NotFoundException {
        Optional<SenatorialDistrict> senatorialDistrict = senatorialDistrictRepository.findById(id);
        if(!senatorialDistrict.isPresent()){
            throw new NotFoundException("Senatorial District not found.");
        }
        return senatorialDistrict.get();
    }

    private Ward getWard(Long id) throws NotFoundException {
        Optional<Ward> currentWard = wardRepository.findById(id);
        if(!currentWard.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return currentWard.get();
    }
}
