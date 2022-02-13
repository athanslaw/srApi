package com.edunge.srtool.service.impl;

import com.edunge.srtool.config.FileConfigurationProperties;
import com.edunge.srtool.dto.WardDto;
import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.FileNotFoundException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.Lga;
import com.edunge.srtool.model.SenatorialDistrict;
import com.edunge.srtool.model.State;
import com.edunge.srtool.model.Ward;
import com.edunge.srtool.repository.LgaRepository;
import com.edunge.srtool.repository.SenatorialDistrictRepository;
import com.edunge.srtool.repository.StateRepository;
import com.edunge.srtool.repository.WardRepository;
import com.edunge.srtool.response.WardResponse;
import com.edunge.srtool.service.WardService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WardServiceImpl implements WardService {

    private final LgaRepository lgaRepository;
    private final StateRepository stateRepository;
    private final SenatorialDistrictRepository senatorialDistrictRepository;
    private final WardRepository wardRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(WardService.class);

    private final Path fileStorageLocation;
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
    public WardServiceImpl(LgaRepository lgaRepository, StateRepository stateRepository, SenatorialDistrictRepository senatorialDistrictRepository, WardRepository wardRepository, FileConfigurationProperties fileConfigurationProperties) {
        this.lgaRepository = lgaRepository;
        this.stateRepository = stateRepository;
        this.senatorialDistrictRepository = senatorialDistrictRepository;
        this.wardRepository = wardRepository;
        try {
            this.fileStorageLocation = Paths.get(fileConfigurationProperties.getSvgDir())
                .toAbsolutePath().normalize();
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileNotFoundException("Could not create the directory where the uploaded files will be stored.", ex);
        }
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
        wardRepository.delete(currentWard);
        return new WardResponse("00",String.format(deleteTemplate,currentWard.getCode()));
    }

    @Override
    public WardResponse findAll() {
        List<Ward> wards = wardRepository.findAll();
        return new WardResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), wards);
    }

    public WardResponse searchWardByFilter(Long stateId, Long senatorialDistrictId, Long lgaId) {
        List<Ward> wards = wardRepository.findAll();
        List<Ward> filter = new ArrayList<>();

        if(stateId>0 && senatorialDistrictId > 0 && lgaId>0){
            filter = wards.stream()
                    .filter(ward -> ward.getState().getId().equals(stateId))
                    .filter(ward -> ward.getSenatorialDistrict().getId().equals(senatorialDistrictId))
                    .filter(ward -> ward.getLga().getId().equals(lgaId))
                    .collect(Collectors.toList());
        }
        else if(stateId>0 && senatorialDistrictId >0){
            filter = wards.stream()
                    .filter(ward -> ward.getState().getId().equals(stateId))
                    .filter(ward -> ward.getSenatorialDistrict().getId().equals(senatorialDistrictId))
                    .collect(Collectors.toList());
        }
        else if(stateId>0){
            filter = wards.stream()
                    .filter(ward -> ward.getState().getId().equals(stateId))
                    .collect(Collectors.toList());
        }
        return new WardResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), filter);
    }

    @Override
    public WardResponse filterByName(String name) throws NotFoundException {
        List<Ward> ward = wardRepository.findByNameStartingWith(name);
        if(ward!=null){
            return new WardResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), ward);
        }
        throw new NotFoundException("Ward not found.");
    }

    @Override
    public WardResponse findByLga(Long lgaCode) throws NotFoundException {
        Lga lga = getLga(lgaCode);
        List<Ward> ward = wardRepository.findByLga(lga);
        if(ward!=null){
            return new WardResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), ward);
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

    private void saveWard(String stateCode,String senatorialDistrictCode,String lgaCode, String code, String name)  {
        State state = stateRepository.findByCode(stateCode);
        SenatorialDistrict senatorialDistrict = senatorialDistrictRepository.findByCode(senatorialDistrictCode);
        Lga lga = lgaRepository.findByCode(lgaCode);
        Ward ward = wardRepository.findByCode(code);
        try{
            if(ward==null){
                ward = new Ward();
                ward.setState(state);
                ward.setLga(lga);
                ward.setSenatorialDistrict(senatorialDistrict);
                ward.setCode(code);
                ward.setName(name);
                wardRepository.save(ward);
            }
        }
        catch (Exception ex){
            LOGGER.info("State could not be saved");
        }
    }

    @Override
    public WardResponse uploadWard(MultipartFile file){
        List<String> csvLines = FileUtil.getCsvLines(file, this.fileStorageLocation);
        return processUpload(csvLines);
    }


    private WardResponse processUpload(List<String> lines){
        for (String line:lines) {
            String[] state = line.split(",");
            saveWard(state[0].trim(), state[1].trim(), state[2].trim(), state[3].trim(),state[4].trim());
        }
        return new WardResponse("00", "File Uploaded.");
    }
}
