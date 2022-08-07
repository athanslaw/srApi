package com.edunge.srtool.service.impl;

import com.edunge.srtool.dto.SenatorialDistrictDto;
import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.SenatorialDistrict;
import com.edunge.srtool.model.State;
import com.edunge.srtool.repository.SenatorialDistrictRepository;
import com.edunge.srtool.repository.StateRepository;
import com.edunge.srtool.response.SenatorialDistrictResponse;
import com.edunge.srtool.service.FileProcessingService;
import com.edunge.srtool.service.SenatorialDistrictService;
import com.edunge.srtool.service.StateService;
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
public class SenatorialDistrictServiceImpl implements SenatorialDistrictService {

    private final StateService stateService;
    private final SenatorialDistrictRepository senatorialDistrictRepository;

    private static final String SERVICE_NAME = "Senatorial District";
    private static final Logger LOGGER = LoggerFactory.getLogger(SenatorialDistrict.class);

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
    public SenatorialDistrictServiceImpl(StateService stateService, SenatorialDistrictRepository senatorialDistrictRepository) {
        this.stateService = stateService;
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
    public long countSenatorialDistrictByState(State state) {
        return senatorialDistrictRepository.countByState(state);
    }

    @Override
    public long countSenatorialDistrict() {
        return senatorialDistrictRepository.count();
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
        SenatorialDistrict senatorialDistrict = getSenatorialDistrict(id);
        senatorialDistrictRepository.deleteById(id);
        return new SenatorialDistrictResponse("00",String.format("%s deleted successfully.",senatorialDistrict.getCode()));
    }

    @Override
    public SenatorialDistrictResponse findAll() {
        List<SenatorialDistrict> senatorialDistricts = senatorialDistrictRepository.findAll();
        return new SenatorialDistrictResponse("00", "All Senatorial District retrieved.", senatorialDistricts);
    }

    @Override
    public SenatorialDistrictResponse filterByName(String name) throws NotFoundException {
        List<SenatorialDistrict> senatorialDistrict = senatorialDistrictRepository.findByNameStartingWith(name);
        if(senatorialDistrict!=null){
            return new SenatorialDistrictResponse("00", String.format(successTemplate,SERVICE_NAME), senatorialDistrict);
        }
        throw new NotFoundException(String.format(notFoundTemplate, SERVICE_NAME));
    }

    @Override
    public SenatorialDistrictResponse findSenatorialDistrictByStateCode(Long stateCode) throws NotFoundException {
        State state = getState(stateCode);
        List<SenatorialDistrict> districtByState = senatorialDistrictRepository.findByState(state);
        return new SenatorialDistrictResponse("00", String.format(successTemplate,SERVICE_NAME), districtByState);
    }

    @Override
    public SenatorialDistrictResponse findSenatorialDistrictForDefaultState() throws NotFoundException {
        State state = getState();
        List<SenatorialDistrict> lgaByState = senatorialDistrictRepository.findByState(state);
        return new SenatorialDistrictResponse("00", String.format(successTemplate,SERVICE_NAME), lgaByState);
    }

    private SenatorialDistrict getSenatorialDistrict(Long id) throws NotFoundException {
        Optional<SenatorialDistrict> senatorialDistrict = senatorialDistrictRepository.findById(id);
        if(!senatorialDistrict.isPresent()){
            throw new NotFoundException("Senatorial District not found.");
        }
        return senatorialDistrict.get();
    }

    private State getState(Long id) throws NotFoundException {
        State currentState = stateService.findStateById(id).getState();
        if(currentState == null){
            throw new NotFoundException("State not found.");
        }
        return currentState;
    }

    private State getState() throws NotFoundException {
        State currentState = stateService.getDefaultState().getState();
        return currentState;
    }

    private void saveSenatorialDistrict(String stateCode, String code, String name)  {
        try{
            State state = stateService.findStateById(Long.parseLong(stateCode)).getState();
            SenatorialDistrict senatorialDistrict = senatorialDistrictRepository.findByCode(code);
            if(senatorialDistrict==null){
                senatorialDistrict = new SenatorialDistrict();
                senatorialDistrict.setState(state);
                senatorialDistrict.setCode(code);
                senatorialDistrict.setName(name);
                senatorialDistrictRepository.save(senatorialDistrict);
            }
        }
        catch (Exception ex){
            LOGGER.info("Senatorial District could not be saved");
        }
    }

    @Override
    public SenatorialDistrictResponse uploadSenatorialDistrict(MultipartFile file){
        List<String> csvLines = FileUtil.getCsvLines(file, fileProcessingService.getFileStorageLocation());
        return processUpload(csvLines);
    }

    private SenatorialDistrictResponse processUpload(List<String> lines){
        for (String line:lines) {
            String[] state = line.split(",");
            saveSenatorialDistrict(state[0].trim(), state[1].trim(), state[2].trim());
        }
        return new SenatorialDistrictResponse("00", "File Uploaded.");
    }
}
