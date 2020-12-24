package com.edunge.srtool.service;

import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.IncidentType;
import com.edunge.srtool.response.IncidentTypeResponse;

public interface IncidentTypeService {
    IncidentTypeResponse saveIncidentType(IncidentType IncidentType) throws NotFoundException;
    IncidentTypeResponse findIncidentTypeById(Long id) throws NotFoundException;
    IncidentTypeResponse findIncidentTypeByCode(String code) throws NotFoundException;
    IncidentTypeResponse editIncidentType(Long id, IncidentType IncidentType) throws NotFoundException;
    IncidentTypeResponse deleteIncidentTypeById(Long id) throws NotFoundException;
    IncidentTypeResponse findAll() ;
}
