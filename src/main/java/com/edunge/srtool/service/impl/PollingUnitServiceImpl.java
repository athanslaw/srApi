package com.edunge.srtool.service.impl;

import com.edunge.srtool.dto.PollingUnitDto;
import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.*;
import com.edunge.srtool.repository.*;
import com.edunge.srtool.response.PollingUnitResponse;
import com.edunge.srtool.service.*;
import com.edunge.srtool.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class PollingUnitServiceImpl implements PollingUnitService {

    private final LgaService lgaService;
    private final StateService stateService;
    private final SenatorialDistrictService senatorialDistrictService;
    private final WardService wardService;
    private final PollingUnitRepository pollingUnitRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(PollingUnitService.class);

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
    FileProcessingService fileProcessingService;

    @Autowired
    public PollingUnitServiceImpl(LgaService lgaService, StateService stateService, SenatorialDistrictService senatorialDistrictService, WardService wardService, PollingUnitRepository pollingUnitRepository) {
        this.lgaService = lgaService;
        this.stateService = stateService;
        this.senatorialDistrictService = senatorialDistrictService;
        this.wardService = wardService;
        this.pollingUnitRepository = pollingUnitRepository;
    }

    @Override
    public PollingUnitResponse savePollingUnit(PollingUnitDto pollingUnitDto) throws NotFoundException {
        State state = getState(pollingUnitDto.getStateId());
        SenatorialDistrict senatorialDistrict = getSenatorialDistrict(pollingUnitDto.getSenatorialDistrictId());
        Lga lga = getLga(pollingUnitDto.getLgaId());
        Ward ward = getWard(pollingUnitDto.getWardId());

        PollingUnit pollingUnit1 = new PollingUnit();
        pollingUnit1.setSenatorialDistrict(senatorialDistrict);
        pollingUnit1.setState(state);
        pollingUnit1.setCode(pollingUnitDto.getCode());
        pollingUnit1.setName(pollingUnitDto.getName());
        pollingUnit1.setLga(lga);
        pollingUnit1.setWard(ward);
        pollingUnitRepository.save(pollingUnit1);
        return new PollingUnitResponse("00", String.format(successTemplate,SERVICE_NAME), pollingUnit1);
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
    public void updatePollingUnitWard(Long wardIdOld, Ward ward) {
        List<PollingUnit> pollingUnits = pollingUnitRepository.findByWard(new Ward(){{setId(wardIdOld);}});
        pollingUnits.forEach(pollingUnit -> {
            pollingUnit.setWard(ward);
            pollingUnit.setLga(ward.getLga());
            pollingUnit.setSenatorialDistrict(ward.getSenatorialDistrict());
            pollingUnit.setState(ward.getState());
            pollingUnitRepository.save(pollingUnit);
        });
    }

    @Override
    public void updatePollingUnitLga(Long lgaOld, Lga lga) {
        List<PollingUnit> pollingUnits = pollingUnitRepository.findByLga(new Lga(){{setId(lgaOld);}});
        pollingUnits.forEach(pollingUnit -> {
            pollingUnit.setLga(lga);
            pollingUnit.setSenatorialDistrict(lga.getSenatorialDistrict());
            pollingUnit.setState(lga.getState());
            pollingUnitRepository.save(pollingUnit);
        });
    }

    @Override
    public void updatePollingUnitDistrict(Long districtOld, SenatorialDistrict senatorialDistrict){
        List<PollingUnit> pollingUnits = pollingUnitRepository.findBySenatorialDistrict(new SenatorialDistrict(){{setId(districtOld);}});
        pollingUnits.forEach(pollingUnit -> {
            pollingUnit.setSenatorialDistrict(senatorialDistrict);
            pollingUnit.setState(senatorialDistrict.getState());
            pollingUnitRepository.save(pollingUnit);
        });
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
        List<PollingUnit> pollingUnit = pollingUnitRepository.findByWardOrderByCodeAsc(ward);
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
    public long findCountByLga(Long lgaCode) {
        Lga lga = new Lga(){{setId(lgaCode);}};
        long pollingUnit = pollingUnitRepository.countByLga(lga);
        return pollingUnit;

    }

    @Override
    public long findCountByWard(Long wardCode) {
        Ward ward = new Ward(){{setId(wardCode);}};
        return pollingUnitRepository.countByWard(ward);
    }

    @Override
    public long findCountBySenatorialDistrict(Long districtCode) {
        SenatorialDistrict senatorialDistrict = new SenatorialDistrict(){{setId(districtCode);}};
        long pollingUnit = pollingUnitRepository.countBySenatorialDistrict(senatorialDistrict);
        return pollingUnit;
    }

    @Override
    public long findCountByState(Long stateId) {
        State state = new State(){{setId(stateId);}};
        long pollingUnit = pollingUnitRepository.countByState(state);
        return pollingUnit;
    }

    @Override
    public PollingUnitResponse findByState(Long stateCode) throws NotFoundException {
        State state = new State(){{setId(stateCode);}};
        List<PollingUnit> pollingUnit = pollingUnitRepository.findByState(state);
        if(pollingUnit!=null){
            return new PollingUnitResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), pollingUnit);
        }
        throw new NotFoundException(String.format(notFoundTemplate, SERVICE_NAME));
    }

    @Override
    public long countByState(State state){
        return pollingUnitRepository.countByState(state);
    }

    @Override
    public long countBySenatorialDistrict(SenatorialDistrict senatorialDistrict){
        return pollingUnitRepository.countBySenatorialDistrict(senatorialDistrict);
    }
    @Override
    public long countByLga(Lga lga){
        return pollingUnitRepository.countByLga(lga);
    }
    @Override
    public long countByWard(Ward ward){
        return pollingUnitRepository.countByWard(ward);
    }
    @Override
    public long countPollingUnit(){
        return pollingUnitRepository.count();
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
        Lga currentLga = lgaService.findLgaById(id).getLga();
        if(currentLga == null){
            throw new NotFoundException("Lga not found.");
        }
        return currentLga;
    }

    private State getState(Long id) throws NotFoundException {
        State currentState = stateService.findStateById(id).getState();
        if(currentState == null){
            throw new NotFoundException("State not found.");
        }
        return currentState;
    }

    private SenatorialDistrict getSenatorialDistrict(Long id) throws NotFoundException {
        SenatorialDistrict senatorialDistrict = senatorialDistrictService.findSenatorialDistrictById(id).getSenatorialDistrict();
        if(senatorialDistrict == null){
            throw new NotFoundException("Senatorial District not found.");
        }
        return senatorialDistrict;
    }

    private Ward getWard(Long id) throws NotFoundException {
        Ward currentWard = wardService.findWardById(id).getWard();
        if(currentWard == null){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return currentWard;
    }

    private PollingUnit getPollingUnit(Long id) throws NotFoundException {
        Optional<PollingUnit> currentPollingUnit = pollingUnitRepository.findById(id);
        if(!currentPollingUnit.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return currentPollingUnit.get();
    }

    private void savePollingUnit(String stateCode, String senatorialDistrictCode, String lgaCode, String wardCode, String code, String name)  {
        try{
            Lga lga = lgaService.findLgaById(Long.valueOf(lgaCode)).getLga();
            Ward ward = wardService.findWardById(Long.valueOf(wardCode)).getWard();
            Optional<PollingUnit> pollingUnit = pollingUnitRepository.findById(Long.valueOf(code));
            if(!pollingUnit.isPresent()){
                PollingUnit pollingUnit1 = new PollingUnit();
                pollingUnit1.setState(lga.getState());
                pollingUnit1.setLga(lga);
                pollingUnit1.setSenatorialDistrict(lga.getSenatorialDistrict());
                pollingUnit1.setCode(code);
                pollingUnit1.setWard(ward);
                pollingUnit1.setName(name);
                pollingUnitRepository.save(pollingUnit1);
            }
        }
        catch (Exception ex){
            LOGGER.info("Polling unit could not be saved");
        }
    }

    @Override
    public PollingUnitResponse uploadPollingUnit(MultipartFile file){
        List<String> csvLines = FileUtil.getCsvLines(file, fileProcessingService.getFileStorageLocation());
        return processUpload(csvLines);
    }


    private PollingUnitResponse processUpload(List<String> lines){
        for (String line:lines) {
            String[] state = line.split(",");
            savePollingUnit(state[0].trim(), state[1].trim(), state[2].trim(), state[3].trim(),state[4].trim(),state[5].trim());
        }
        return new PollingUnitResponse("00", "File Uploaded.");
    }

    public void updatePollingUnitWard(Ward oldWard, Ward newWard)  {
        List<PollingUnit> pollingUnits = pollingUnitRepository.findByWard(oldWard);
        pollingUnits.forEach(pollingUnit -> {
            try {
                pollingUnit.setLga(newWard.getLga());
                pollingUnit.setWard(newWard);
                pollingUnit.setSenatorialDistrict(newWard.getSenatorialDistrict());
                pollingUnit.setState(newWard.getState());
                pollingUnitRepository.save(pollingUnit);
            }catch (Exception e){}
        });

    }

}
