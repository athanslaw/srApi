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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LgaServiceImpl implements LgaService {
    private static final String SERVICE_NAME = "LGA";

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
            return new LgaResponse("00", String.format(successTemplate, SERVICE_NAME), lga);
        }
        throw new DuplicateException(String.format(duplicateTemplate, lgaDto.getCode()));
    }

    @Override
    public LgaResponse findLgaById(Long id) throws NotFoundException {
        Lga currentLga = getLga(id);
        return new LgaResponse("00", String.format(successTemplate, SERVICE_NAME), currentLga);
    }

    @Override
    public LgaResponse findLgaByCode(String code) throws NotFoundException {
        Lga currentLga = lgaRepository.findByCode(code);
        if(currentLga==null){
            throw new NotFoundException("State not found.");
        }
        return new LgaResponse("00", String.format(fetchRecordTemplate, code), currentLga);
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
            return new LgaResponse("00", String.format(updateTemplate, lgaDto.getCode()), currentLga);
        }
        throw new NotFoundException(String.format(notFoundTemplate, lgaDto.getCode()));
    }

    @Override
    public LgaResponse deleteLgaById(Long id) throws NotFoundException {
        Lga currentLga = getLga(id);
        return new LgaResponse("00",String.format(deleteTemplate,currentLga.getCode()));
    }

    @Override
    public LgaResponse findAll() {
        List<Lga> lgas = lgaRepository.findAll();
        return new LgaResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), lgas);
    }


    @Override
    public LgaResponse filterByName(String name) throws NotFoundException {
        Lga lga = lgaRepository.findByNameLike(name);
        if(lga!=null){
            return new LgaResponse("00", String.format(successTemplate,SERVICE_NAME), lga);
        }
        throw new NotFoundException(String.format(notFoundTemplate, SERVICE_NAME));
    }


    private Lga getLga(Long id) throws NotFoundException {
        Optional<Lga> currentLga = lgaRepository.findById(id);
        if(!currentLga.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate, SERVICE_NAME));
        }
        return currentLga.get();
    }

    private State getState(Long id) throws NotFoundException {
        Optional<State> currentState = stateRepository.findById(id);
        if(!currentState.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate, "State"));
        }
        return currentState.get();
    }

    private SenatorialDistrict getSenatorialDistrict(Long id) throws NotFoundException {
        Optional<SenatorialDistrict> senatorialDistrict = senatorialDistrictRepository.findById(id);
        if(!senatorialDistrict.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate, "Senatorial District"));
        }
        return senatorialDistrict.get();
    }
}
