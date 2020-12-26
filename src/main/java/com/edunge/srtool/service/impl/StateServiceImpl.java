package com.edunge.srtool.service.impl;

import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.State;
import com.edunge.srtool.repository.StateRepository;
import com.edunge.srtool.response.StateResponse;
import com.edunge.srtool.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StateServiceImpl implements StateService {
    private final StateRepository stateRepository;
    private static final String SERVICE_NAME = "State";

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
    public StateServiceImpl(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    @Override
    public StateResponse saveState(State state) throws NotFoundException {
        State existingState = stateRepository.findByCode(state.getCode());
        if(existingState==null){
            stateRepository.save(state);
            return new StateResponse("00", "State updated successfully", state);
        }
        throw new DuplicateException(String.format("%s already exist.", state.getCode()));
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
    public StateResponse editState(Long id, State state) throws NotFoundException {
        State currentState =  getState(id);
        currentState.setId(id);
        currentState.setCode(state.getCode());
        currentState.setName(state.getName());
        stateRepository.save(currentState);
        return new StateResponse("00", "State updated successfully", currentState);
    }

    @Override
    public StateResponse deleteStateById(Long id) throws NotFoundException {
        State currentState = getState(id);
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
}
