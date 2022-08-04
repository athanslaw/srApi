package com.edunge.srtool.service.impl;

import com.edunge.srtool.dto.LgaDto;
import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.*;
import com.edunge.srtool.repository.LgaRepository;
import com.edunge.srtool.response.LgaResponse;
import com.edunge.srtool.service.*;
import com.edunge.srtool.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LgaServiceImpl implements LgaService {
    private static final String SERVICE_NAME = "LGA";
    private static final Logger LOGGER = LoggerFactory.getLogger(LgaService.class);

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
    private final StateService stateService;

    private final SenatorialDistrictService senatorialDistrictService;

    @Autowired
    FileProcessingService fileProcessingService;
    @Autowired
    public LgaServiceImpl(LgaRepository lgaRepository, StateService stateService, SenatorialDistrictService senatorialDistrictService) {
        this.lgaRepository = lgaRepository;
        this.stateService = stateService;
        this.senatorialDistrictService = senatorialDistrictService;
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
        SenatorialDistrict senatorialDistrict = getSenatorialDistrict(lgaDto.getSenatorialDistrictId());

        Lga currentLga = getLga(id);
        currentLga.setId(id);
        currentLga.setCode(lgaDto.getCode());
        currentLga.setName(lgaDto.getName());
        currentLga.setState(senatorialDistrict.getState());
        currentLga.setSenatorialDistrict(senatorialDistrict);
        lgaRepository.save(currentLga);

        return new LgaResponse("00", String.format(updateTemplate, lgaDto.getCode()), currentLga);
    }

    @Override
    public LgaResponse deleteLgaById(Long id) throws NotFoundException {
        Lga currentLga = getLga(id);
        lgaRepository.delete(currentLga);
        return new LgaResponse("00",String.format(deleteTemplate,currentLga.getCode()));
    }

    @Override
    public LgaResponse findAll() {
        List<Lga> lgas = lgaRepository.findAll();
        return new LgaResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), lgas);
    }

    public LgaResponse findLgaFilter(Long stateId, Long senatorialDistrictId) throws NotFoundException {
        List<Lga> lgas = lgaRepository.findAll();

        List<Lga> filter = new ArrayList<>();
        if(stateId>0 && senatorialDistrictId>0 ){
            filter = lgas.stream()
                    .filter(lga -> lga.getState().getId().equals(stateId))
                    .filter(lga -> lga.getSenatorialDistrict().getId().equals(senatorialDistrictId))
                    .collect(Collectors.toList());
        }else if(stateId>0){
            filter = lgas.stream()
                    .filter(lga -> lga.getState().getId().equals(stateId))
                    .collect(Collectors.toList());
        }else if(senatorialDistrictId>0){
            filter = lgas.stream()
                    .filter(lga -> lga.getSenatorialDistrict().getId().equals(senatorialDistrictId))
                    .collect(Collectors.toList());
        }

        return new LgaResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), filter);
    }

    @Override
    public LgaResponse filterByName(String name) throws NotFoundException {
        List<Lga> lga = lgaRepository.findByNameStartingWith(name);
        if(lga!=null){
            return new LgaResponse("00", String.format(successTemplate,SERVICE_NAME), lga);
        }
        throw new NotFoundException(String.format(notFoundTemplate, SERVICE_NAME));
    }

    @Override
    public LgaResponse findLgaByStateCode() throws NotFoundException {
        State state = getDefaultState();
        List<Lga> lgaByState = lgaRepository.findByState(state);
        return new LgaResponse("00", String.format(successTemplate,SERVICE_NAME), lgaByState);
    }

    @Override
    public LgaResponse findLgaByStateCode(Long stateCode) throws NotFoundException {
        State state = new State(){{setId(stateCode);}};
        List<Lga> lgaByState = lgaRepository.findByState(state);
        return new LgaResponse("00", String.format(successTemplate,SERVICE_NAME), lgaByState);
    }

    @Override
    public long countLgaByStateCode(Long stateCode) {
        State state = new State(){{setId(stateCode);}};
        return lgaRepository.countByState(state);
    }

    @Override
    public long countLga() {
        return lgaRepository.count();
    }

    @Override
    public long countLgaBySenatorialDistrict(Long districtCode) {
        SenatorialDistrict senatorialDistrict = new SenatorialDistrict(){{setId(districtCode);}};
        return lgaRepository.countBySenatorialDistrict(senatorialDistrict);
    }

    @Override
    public void updateLgaDistrict(Long districtOld, SenatorialDistrict senatorialDistrict) throws NotFoundException {
        List<Lga> lgas = lgaRepository.findBySenatorialDistrict(new SenatorialDistrict(){{setId(districtOld);}});
        lgas.forEach(lga -> {
            lga.setSenatorialDistrict(senatorialDistrict);
            lga.setState(senatorialDistrict.getState());
            lgaRepository.save(lga);
        });
    }

    @Override
    public LgaResponse findLgaBySenatorialDistrictCode(Long senatorialDistrictId) throws NotFoundException {
        SenatorialDistrict senatorialDistrict = getSenatorialDistrict(senatorialDistrictId);
        List<Lga> lgaByState = lgaRepository.findBySenatorialDistrict(senatorialDistrict);
        return new LgaResponse("00", String.format(successTemplate,SERVICE_NAME), lgaByState);
    }

    private Lga getLga(Long id) throws NotFoundException {
        Optional<Lga> currentLga = lgaRepository.findById(id);
        if(!currentLga.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate, SERVICE_NAME));
        }
        return currentLga.get();
    }

    private State getState(Long id) throws NotFoundException {
        State currentState = stateService.findStateById(id).getState();
        if(currentState == null){
            throw new NotFoundException(String.format(notFoundTemplate, "State"));
        }
        return currentState;
    }

    private State getDefaultState() throws NotFoundException {
        State currentState = stateService.getDefaultState().getState();
        if(currentState == null){
            throw new NotFoundException(String.format(notFoundTemplate, "State"));
        }
        return currentState;
    }

    private SenatorialDistrict getSenatorialDistrict(Long id) throws NotFoundException {
        SenatorialDistrict senatorialDistrict = senatorialDistrictService.findSenatorialDistrictById(id).getSenatorialDistrict();
        if(senatorialDistrict == null){
            throw new NotFoundException(String.format(notFoundTemplate, "Senatorial District"));
        }
        return senatorialDistrict;
    }

    private void saveLga(String stateId,String senatorialDistrictCode, String code, String name)  {
        try{
            SenatorialDistrict senatorialDistrict = senatorialDistrictService.findSenatorialDistrictById(Long.parseLong(senatorialDistrictCode)).getSenatorialDistrict();
            Optional<Lga> lga = lgaRepository.findById(Long.parseLong(code));

            if(!lga.isPresent()){
                Lga lga1 = new Lga();
                lga1.setState(senatorialDistrict.getState());
                lga1.setSenatorialDistrict(senatorialDistrict);
                lga1.setCode(code);
                lga1.setName(name);
                lgaRepository.save(lga1);
            }
        }
        catch (Exception ex){
            LOGGER.info("Lga could not be saved");
        }
    }

    @Override
    public LgaResponse uploadLga(MultipartFile file){
        List<String> csvLines = FileUtil.getCsvLines(file, fileProcessingService.getFileStorageLocation());
        return processUpload(csvLines);
    }


    private LgaResponse processUpload(List<String> lines){
        for (String line:lines) {
            String[] state = line.split(",");
            saveLga(state[0].trim(), state[1].trim(), state[2].trim(), state[3].trim());
        }
        return new LgaResponse("00", "File Uploaded.");
    }
}
