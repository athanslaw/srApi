package com.edunge.srtool.service.impl;

import com.edunge.srtool.config.FileConfigurationProperties;
import com.edunge.srtool.dto.PollingUnitDto;
import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.FileNotFoundException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.*;
import com.edunge.srtool.repository.*;
import com.edunge.srtool.response.PollingUnitResponse;
import com.edunge.srtool.service.PollingUnitService;
import com.edunge.srtool.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class PollingUnitServiceImpl implements PollingUnitService {

    private final LgaRepository lgaRepository;
    private final StateRepository stateRepository;
    private final SenatorialDistrictRepository senatorialDistrictRepository;
    private final WardRepository wardRepository;
    private final PollingUnitRepository pollingUnitRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(PollingUnitService.class);

    private final Path fileStorageLocation;
    private static final String SERVICE_NAME = "Polling Unit";

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
    public PollingUnitServiceImpl(LgaRepository lgaRepository, StateRepository stateRepository, SenatorialDistrictRepository senatorialDistrictRepository, WardRepository wardRepository, PollingUnitRepository pollingUnitRepository, FileConfigurationProperties fileConfigurationProperties) {
        this.lgaRepository = lgaRepository;
        this.stateRepository = stateRepository;
        this.senatorialDistrictRepository = senatorialDistrictRepository;
        this.wardRepository = wardRepository;
        this.pollingUnitRepository = pollingUnitRepository;
        this.fileStorageLocation = Paths.get(fileConfigurationProperties.getSvgDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileNotFoundException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public PollingUnitResponse savePollingUnit(PollingUnitDto pollingUnitDto) throws NotFoundException {
        State state = getState(pollingUnitDto.getStateId());
        SenatorialDistrict senatorialDistrict = getSenatorialDistrict(pollingUnitDto.getSenatorialDistrictId());
        Lga lga = getLga(pollingUnitDto.getLgaId());
       Ward ward = getWard(pollingUnitDto.getWardId());
        PollingUnit pollingUnit = pollingUnitRepository.findByCode(pollingUnitDto.getCode());


        if(pollingUnit==null){
            pollingUnit = new PollingUnit();
            pollingUnit.setSenatorialDistrict(senatorialDistrict);
            pollingUnit.setState(state);
            pollingUnit.setCode(pollingUnitDto.getCode());
            pollingUnit.setName(pollingUnitDto.getName());
            pollingUnit.setLga(lga);
            pollingUnit.setWard(ward);
            pollingUnitRepository.save(pollingUnit);
            return new PollingUnitResponse("00", String.format(successTemplate,SERVICE_NAME), pollingUnit);
        }
        throw new DuplicateException(String.format(duplicateTemplate, pollingUnitDto.getCode()));
    }

    @Override
    public PollingUnitResponse findPollingUnitById(Long id) throws NotFoundException {
        PollingUnit currentPollingUnit = getPollingUnit(id);
        return new PollingUnitResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), currentPollingUnit);
    }

    @Override
    public PollingUnitResponse findPollingUnitByCode(String code) throws NotFoundException {
        PollingUnit currentPollingUnit = pollingUnitRepository.findByCode(code);
        if(currentPollingUnit==null){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return new PollingUnitResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), currentPollingUnit);
    }

    @Override
    public PollingUnitResponse updatePollingUnit(Long id, PollingUnitDto pollingUnitDto) throws NotFoundException {
        State state = getState(pollingUnitDto.getStateId());
        SenatorialDistrict senatorialDistrict = getSenatorialDistrict(pollingUnitDto.getSenatorialDistrictId());
        Lga lga = getLga(pollingUnitDto.getLgaId());
        Ward ward = getWard(pollingUnitDto.getLgaId());
        PollingUnit currentPollingUnit = getPollingUnit(id);
        currentPollingUnit.setId(id);
        currentPollingUnit.setCode(pollingUnitDto.getCode());
        currentPollingUnit.setName(pollingUnitDto.getName());
        currentPollingUnit.setState(state);
        currentPollingUnit.setSenatorialDistrict(senatorialDistrict);
        currentPollingUnit.setLga(lga);
        currentPollingUnit.setWard(ward);
        pollingUnitRepository.save(currentPollingUnit);
        return new PollingUnitResponse("00", String.format(successTemplate, SERVICE_NAME), currentPollingUnit);
    }

    @Override
    public PollingUnitResponse filterByName(String name) throws NotFoundException {
        List<PollingUnit> pollingUnit = pollingUnitRepository.findByNameStartingWith(name);
        if(pollingUnit!=null){
            return new PollingUnitResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), pollingUnit);
        }
        throw new NotFoundException(String.format(notFoundTemplate, SERVICE_NAME));
    }

    @Override
    public PollingUnitResponse findByWardCode(Long wardCode) throws NotFoundException {
        Ward ward = getWard(wardCode);
        List<PollingUnit> pollingUnit = pollingUnitRepository.findByWard(ward);
        if(pollingUnit!=null){
            return new PollingUnitResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), pollingUnit);
        }
        throw new NotFoundException(String.format(notFoundTemplate, SERVICE_NAME));
    }

    @Override
    public PollingUnitResponse findByLga(Long lgaCode) throws NotFoundException {
        Lga lga = getLga(lgaCode);
        List<PollingUnit> pollingUnit = pollingUnitRepository.findByLga(lga);
        if(pollingUnit!=null){
            return new PollingUnitResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), pollingUnit);
        }
        throw new NotFoundException(String.format(notFoundTemplate, SERVICE_NAME));
    }

    @Override
    public PollingUnitResponse findByState(Long stateCode) throws NotFoundException {
        State state = getState(stateCode);
        List<PollingUnit> pollingUnit = pollingUnitRepository.findByState(state);
        if(pollingUnit!=null){
            return new PollingUnitResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), pollingUnit);
        }
        throw new NotFoundException(String.format(notFoundTemplate, SERVICE_NAME));
    }

    @Override
    public PollingUnitResponse findBySenatorialDistrict(Long senatorialDistrictCode) throws NotFoundException {
        SenatorialDistrict senatorialDistrict = getSenatorialDistrict(senatorialDistrictCode);
        List<PollingUnit> pollingUnit = pollingUnitRepository.findBySenatorialDistrict(senatorialDistrict);
        if(pollingUnit!=null){
            return new PollingUnitResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), pollingUnit);
        }
        throw new NotFoundException(String.format(notFoundTemplate, SERVICE_NAME));
    }

    @Override
    public PollingUnitResponse deletePollingUnitById(Long id) throws NotFoundException {
        PollingUnit currentPollingUnit = getPollingUnit(id);
        pollingUnitRepository.deleteById(id);
        return new PollingUnitResponse("00",String.format(deleteTemplate,currentPollingUnit.getCode()));
    }

    @Override
    public PollingUnitResponse findAll() {
        List<PollingUnit> wards = pollingUnitRepository.findAll();
        return new PollingUnitResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), wards);
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

    private PollingUnit getPollingUnit(Long id) throws NotFoundException {
        Optional<PollingUnit> currentPollingUnit = pollingUnitRepository.findById(id);
        if(!currentPollingUnit.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return currentPollingUnit.get();
    }

    private void savePollingUnit(String stateCode, String senatorialDistrictCode, String lgaCode, String wardCode, String code, String name)  {
        State state = stateRepository.findByCode(stateCode);
        SenatorialDistrict senatorialDistrict = senatorialDistrictRepository.findByCode(senatorialDistrictCode);
        Lga lga = lgaRepository .findByCode(lgaCode);
        Ward ward = wardRepository.findByCode(wardCode);
        PollingUnit pollingUnit = pollingUnitRepository.findByCode(code);
        try{
            if(pollingUnit==null){
                pollingUnit = new PollingUnit();
                pollingUnit.setState(state);
                pollingUnit.setLga(lga);
                pollingUnit.setSenatorialDistrict(senatorialDistrict);
                pollingUnit.setCode(code);
                pollingUnit.setWard(ward);
                pollingUnit.setName(name);
                pollingUnitRepository.save(pollingUnit);
            }
        }
        catch (Exception ex){
            LOGGER.info("Polling unit could not be saved");
        }
    }

    @Override
    public PollingUnitResponse uploadPollingUnit(MultipartFile file){
        List<String> csvLines = FileUtil.getCsvLines(file, this.fileStorageLocation);
        return processUpload(csvLines);
    }


    private PollingUnitResponse processUpload(List<String> lines){
        for (String line:lines) {
            String[] state = line.split(",");
            savePollingUnit(state[0].trim(), state[1].trim(), state[2].trim(), state[3].trim(),state[4].trim(),state[5].trim());
        }
        return new PollingUnitResponse("00", "File Uploaded.");
    }
}
