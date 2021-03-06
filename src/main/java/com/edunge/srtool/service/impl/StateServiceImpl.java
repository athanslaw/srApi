package com.edunge.srtool.service.impl;

import com.edunge.srtool.config.FileConfigurationProperties;
import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.FileNotFoundException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.Lga;
import com.edunge.srtool.model.State;
import com.edunge.srtool.repository.LgaRepository;
import com.edunge.srtool.repository.StateRepository;
import com.edunge.srtool.response.StateResponse;
import com.edunge.srtool.service.StateService;
import com.edunge.srtool.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class StateServiceImpl implements StateService {
    private final StateRepository stateRepository;
    private final LgaRepository lgaRepository;
    private static final String SERVICE_NAME = "State";
    private static final Logger LOGGER = LoggerFactory.getLogger(StateService.class);

    private final Path fileStorageLocation;

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
    public StateServiceImpl(StateRepository stateRepository, LgaRepository lgaRepository, FileConfigurationProperties fileConfigurationProperties) {
        this.stateRepository = stateRepository;
        this.lgaRepository = lgaRepository;
        try {
            this.fileStorageLocation = Paths.get(fileConfigurationProperties.getSvgDir())
                .toAbsolutePath().normalize();
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileNotFoundException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public StateResponse saveState(String code, String name, MultipartFile file) throws NotFoundException {
        State existingState = stateRepository.findByCode(code);
        if(existingState==null){
            existingState = new State();
            existingState.setCode(code);
            existingState.setName(name);
            FileUtil.uploadFile(file, fileStorageLocation);
            String fileUrl = getSvgUrl(file.getOriginalFilename());
            existingState.setSvgUrl(fileUrl);

            stateRepository.save(existingState);
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
    public StateResponse editState(Long id, String code, String name, MultipartFile file) throws NotFoundException {
        State currentState =  getState(id);
        currentState.setId(id);
        currentState.setCode(code);
        currentState.setName(name);
        if(file!=null){
            FileUtil.uploadFile(file, fileStorageLocation);
            String fileUrl = getSvgUrl(file.getOriginalFilename());
            currentState.setSvgUrl(fileUrl);
        }
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
        return FileUtil.loadResource(fileName, fileStorageLocation);
    }

    @Override
    public StateResponse findAll() {
        List<State> states = stateRepository.findAll();
        return new StateResponse("00", "All states retrieved.", states);
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
        List<State> states = stateRepository.findAll();
        states.stream().filter(currentState-> !currentState.getId().equals(stateId)).forEach(currentState -> {
            currentState.setDefaultState(false);
            stateRepository.save(currentState);
        });
        state.setDefaultState(true);
        stateRepository.save(state);
        return new StateResponse("00", "Default state", state);
    }

    private void saveState(String code, String name)  {
        State existingState = stateRepository.findByCode(code);
        try{
            if(existingState==null){
                existingState = new State();
                existingState.setCode(code);
                existingState.setName(name);
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
        List<String> csvLines = FileUtil.getCsvLines(file, this.fileStorageLocation);
        return processUpload(csvLines);
    }


    private StateResponse processUpload(List<String> lines){
        for (String line:lines) {
            String[] state = line.split(",");
            saveState(state[0].trim(), state[1].trim());
        }
        return new StateResponse("00", "File Uploaded.");
    }
}
