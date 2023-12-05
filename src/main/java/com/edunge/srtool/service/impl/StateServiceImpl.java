package com.edunge.srtool.service.impl;

import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.*;
import com.edunge.srtool.repository.GeoPoliticalZoneRepository;
import com.edunge.srtool.repository.LgaRepository;
import com.edunge.srtool.repository.StateRepository;
import com.edunge.srtool.response.StateResponse;
import com.edunge.srtool.service.FileProcessingService;
import com.edunge.srtool.service.StateService;
import com.edunge.srtool.util.Constants;
import com.edunge.srtool.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class StateServiceImpl implements StateService {
    private final StateRepository stateRepository;
    private final GeoPoliticalZoneRepository geoPoliticalZoneRepository;
    private final LgaRepository lgaRepository;
    private static final String SERVICE_NAME = "State";
    private static final Logger LOGGER = LoggerFactory.getLogger(StateService.class);

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
    public StateServiceImpl(StateRepository stateRepository, LgaRepository lgaRepository, GeoPoliticalZoneRepository geoPoliticalZoneRepository) {
        this.stateRepository = stateRepository;
        this.lgaRepository = lgaRepository;
        this.geoPoliticalZoneRepository = geoPoliticalZoneRepository;
    }

    @Override
    public StateResponse saveState(String code, String name, Long geoPoliticalZone, MultipartFile file) throws NotFoundException {
        State existingState = stateRepository.findByCode(code);
        if(existingState==null){
            existingState = new State();
            existingState.setCode(code);
            existingState.setName(name);
            existingState.setGeoPoliticalZone(new GeoPoliticalZone(){{setId(geoPoliticalZone);}});
            existingState.setSvgUrl("default.svg");

            stateRepository.save(existingState);
            System.out.println("Got here 3");
            return new StateResponse("00", "State updated successfully", existingState);
        }
        throw new DuplicateException(String.format("%s already exist.", existingState.getCode()));
    }

    @Override
    public StateResponse findStateById(Long id) throws NotFoundException {
        State currentState = getState(id);
        return new StateResponse("00", "State retrieved successfully", currentState);
    }

    @Override
    public StateResponse findStateByCode(String code) throws NotFoundException {
        State currentState = stateRepository.findByCode(code);
        if(currentState==null){
            throw new NotFoundException("State not found.");
        }
        return new StateResponse("00", "State retrieved successfully", currentState);
    }

    @Override
    public StateResponse editState(Long id, String code, String name, Long geoPoliticalZone, MultipartFile file) throws NotFoundException {
        State currentState =  getState(id);
        currentState.setId(id);
        currentState.setCode(code);
        currentState.setName(name);
        GeoPoliticalZone geoPoliticalZone1 = geoPoliticalZoneRepository.findById(geoPoliticalZone).get();
        currentState.setGeoPoliticalZone(geoPoliticalZone1);
        stateRepository.save(currentState);
        return new StateResponse("00", "State updated successfully", currentState);
    }

    @Override
    public StateResponse deleteStateById(Long id) throws NotFoundException {
        State currentState = getState(id);
        stateRepository.deleteById(id);
        return new StateResponse("00",String.format("%s deleted successfully.",currentState.getCode()));
    }

    @Override
    public StateResponse filterByName(String name) throws NotFoundException {
        State state = stateRepository.findByNameStartingWith(name);
        if(state!=null){
            return new StateResponse("00", String.format(successTemplate,SERVICE_NAME), state);
        }
        throw new NotFoundException("State not found.");
    }

    @Override
    public Resource loadSvg(String fileName) {
        return FileUtil.loadResource(fileName, fileProcessingService.getFileStorageLocation());
    }

    @Override
    public StateResponse findAll() {
        List<State> states = stateRepository.findAll();
        states.sort(Comparator.comparing(AbstractBaseModel::getName));

        return new StateResponse("00", "All states retrieved.", states);
    }

    @Override
    public StateResponse findByZone(Long zone){
        List<State> states = stateRepository.findByGeoPoliticalZone(new GeoPoliticalZone(){{setId(zone);}});
        states.sort(Comparator.comparing(AbstractBaseModel::getId));
        return new StateResponse("00", "All states retrieved.", states);
    }

    @Override
    public long countState() {
        if(TerritorialDataCount.get(Constants.STATE) != -1){
            return TerritorialDataCount.get(Constants.STATE);
        }
        return stateRepository.count();
    }

    private State getState(Long id) throws NotFoundException {
        Optional<State> currentState = stateRepository.findById(id);
        if(!currentState.isPresent()){
            throw new NotFoundException("State not found.");
        }
        return currentState.get();
    }

    private List<Lga> getLga(State state) throws NotFoundException {
        List<Lga> lga = lgaRepository.findByState(state);
        return lga;
    }

    private String getSvgUrl(String fileName){
        StringBuilder sb = new StringBuilder();
        sb.append("/uploads/svg/");
        sb.append(fileName);
        return sb.toString();
    }

    public StateResponse getDefaultState() throws NotFoundException {

        State state = stateRepository.findByDefaultState(true);
        if(state==null) throw new NotFoundException("State not found.");
        return new StateResponse("00", "Default state", state);
    }

    public StateResponse setDefaultState(Long stateId) throws NotFoundException {
        State state = getState(stateId);
        State currentDefaultState = stateRepository.findByDefaultState(true);
        currentDefaultState.setDefaultState(false);
        stateRepository.save(currentDefaultState);
        state.setDefaultState(true);
        stateRepository.save(state);
        return new StateResponse("00", "Default state", state);
    }

    private void saveState(String code, String name, String geoPoliticalZone)  {
        State existingState = stateRepository.findByCode(code);
        try{
            if(existingState==null){
                existingState = new State();
                existingState.setCode(code);
                existingState.setName(name);
                geoPoliticalZoneRepository.findById(Long.valueOf(geoPoliticalZone)).get();
                existingState.setSvgUrl("default.svg");
                existingState.setDefaultState(false);
                stateRepository.save(existingState);
            }
        }
        catch (Exception ex){
            LOGGER.info("State could not be saved");
        }
    }

    @Override
    public StateResponse uploadState(MultipartFile file){
        List<String> csvLines = FileUtil.getCsvLines(file, fileProcessingService.getFileStorageLocation());
        return processUpload(csvLines);
    }


    private StateResponse processUpload(List<String> lines){
        for (String line:lines) {
            String[] state = line.split(",");
            saveState(state[0].trim(), state[1].trim(), state[2].trim());
        }
        return new StateResponse("00", "File Uploaded.");
    }
}
