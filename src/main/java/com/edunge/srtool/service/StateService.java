package com.edunge.srtool.service;

import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.StateResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StateService {
    StateResponse saveState(String code, String name, Long geoPoliticalZone, MultipartFile file) throws NotFoundException;
    StateResponse findStateById(Long id) throws NotFoundException;
    StateResponse findStateByCode(String code) throws NotFoundException;
    StateResponse editState(Long id, String code, String name, Long geoPoliticalZone, MultipartFile file) throws NotFoundException;
    StateResponse deleteStateById(Long id) throws NotFoundException;
    StateResponse findAll() ;
    long countState();
    StateResponse filterByName(String name) throws NotFoundException;
    Resource loadSvg(String fileName);
    StateResponse getDefaultState() throws NotFoundException;
    StateResponse setDefaultState(Long stateId) throws NotFoundException;

    StateResponse uploadState(MultipartFile file);
}
