package com.edunge.srtool.service;

import com.edunge.srtool.dto.ElectionDto;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.ElectionResponse;

public interface ElectionService {
    ElectionResponse saveElection(ElectionDto ward) throws NotFoundException;
    ElectionResponse findElectionById(Long id) throws NotFoundException;
    ElectionResponse findElectionByCode(String code) throws NotFoundException;
    ElectionResponse updateElection(Long id, ElectionDto ward) throws NotFoundException;
    ElectionResponse deleteElectionById(Long id) throws NotFoundException;
    ElectionResponse findAll() ;
    ElectionResponse filterByName(String name) throws NotFoundException;
}
