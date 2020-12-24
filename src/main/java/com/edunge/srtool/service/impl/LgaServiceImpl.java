package com.edunge.srtool.service.impl;

import com.edunge.srtool.dto.LgaDto;
import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.Lga;
import com.edunge.srtool.model.SenatorialDistrict;
import com.edunge.srtool.model.State;
import com.edunge.srtool.repository.LgaRepository;
import com.edunge.srtool.repository.SenatorialDistrictRepository;
import com.edunge.srtool.repository.StateRepository;
import com.edunge.srtool.response.LgaResponse;
import com.edunge.srtool.service.LgaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LgaServiceImpl implements LgaService {

    private final LgaRepository lgaRepository;
    private final StateRepository stateRepository;
    private final SenatorialDistrictRepository senatorialDistrictRepository;

    @Autowired
    public LgaServiceImpl(LgaRepository lgaRepository, StateRepository stateRepository, SenatorialDistrictRepository senatorialDistrictRepository) {
        this.lgaRepository = lgaRepository;
        this.stateRepository = stateRepository;
        this.senatorialDistrictRepository = senatorialDistrictRepository;
    }

    @Override
    public LgaResponse saveLga(LgaDto lgaDto) throws NotFoundException {
        State state = getState(lgaDto.getStateId());
        SenatorialDistrict senatorialDistrict = getSenatorialDistrict(lgaDto.getSenatorialDistrictId());

        Lga lga = lgaRepository.findByCode(lgaDto.getCode());
        if(lga==null){
            lga = new Lga();
            lga.setSenatorialDistrict(senatorialDistrict);
            lga.setState(state);
            lga.setCode(lgaDto.getCode());
            lga.setName(lgaDto.getName());
            lgaRepository.save(lga);
            return new LgaResponse("00", "LGA saved successfully", lga);
        }
        throw new DuplicateException(String.format("%s already exist.", lgaDto.getCode()));
    }

    @Override
    public LgaResponse findLgaById(Long id) throws NotFoundException {
        Lga currentLga = getLga(id);
        return new LgaResponse("00", "State retrieved successfully", currentLga);
    }

    @Override
    public LgaResponse findLgaByCode(String code) throws NotFoundException {
        Lga currentLga = lgaRepository.findByCode(code);
        if(currentLga==null){
            throw new NotFoundException("State not found.");
        }
        return new LgaResponse("00", "LGA retrieved successfully", currentLga);
    }

    @Override
    public LgaResponse updateLga(Long id, LgaDto lgaDto) throws NotFoundException {
        State state = getState(lgaDto.getStateId());
        SenatorialDistrict senatorialDistrict = getSenatorialDistrict(lgaDto.getSenatorialDistrictId());

        Lga currentLga = lgaRepository.findByCode(lgaDto.getCode());
        if(currentLga!=null){
            currentLga.setId(id);
            currentLga.setCode(lgaDto.getCode());
            currentLga.setName(lgaDto.getName());
            currentLga.setState(state);
            currentLga.setSenatorialDistrict(senatorialDistrict);
            lgaRepository.save(currentLga);
            return new LgaResponse("00", "LGA updated successfully", currentLga);
        }
        throw new NotFoundException(String.format("%s not found.", lgaDto.getCode()));
    }

    @Override
    public LgaResponse deleteLgaById(Long id) throws NotFoundException {
        Lga currentLga = getLga(id);
        return new LgaResponse("00",String.format("%s deleted successfully.",currentLga.getCode()));
    }

    @Override
    public LgaResponse findAll() {
        List<Lga> lgas = lgaRepository.findAll();
        return new LgaResponse("00", "All LGAs retrieved.", lgas);
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
}
