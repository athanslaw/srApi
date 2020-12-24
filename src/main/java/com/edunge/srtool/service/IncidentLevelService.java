package com.edunge.srtool.service;

import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.IncidentLevel;
import com.edunge.srtool.response.IncidentLevelResponse;

public interface IncidentLevelService {
    IncidentLevelResponse saveIncidentLevel(IncidentLevel incidentLevel) throws NotFoundException;
    IncidentLevelResponse findIncidentLevelById(Long id) throws NotFoundException;
    IncidentLevelResponse findIncidentLevelByCode(String code) throws NotFoundException;
    IncidentLevelResponse editIncidentLevel(Long id, IncidentLevel incidentLevel) throws NotFoundException;
    IncidentLevelResponse deleteIncidentLevelById(Long id) throws NotFoundException;
    IncidentLevelResponse findAll() ;
}
