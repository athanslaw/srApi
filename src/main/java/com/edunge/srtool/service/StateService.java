package com.edunge.srtool.service;

import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.State;
import com.edunge.srtool.response.StateResponse;

public interface StateService {
    StateResponse saveState(State state) throws NotFoundException;
    StateResponse findStateById(Long id) throws NotFoundException;
    StateResponse findStateByCode(String code) throws NotFoundException;
    StateResponse editState(Long id, State state) throws NotFoundException;
    StateResponse deleteStateById(Long id) throws NotFoundException;
    StateResponse findAll() ;
    StateResponse filterByName(String name) throws NotFoundException;
}
