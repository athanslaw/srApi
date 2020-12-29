package com.edunge.srtool.service;

import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.IncidentStatus;
import com.edunge.srtool.response.IncidentStatusResponse;

public interface IncidentStatusService {
    IncidentStatusResponse saveIncidentStatus(IncidentStatus IncidentStatus) throws NotFoundException;
    IncidentStatusResponse findIncidentStatusById(Long id) throws NotFoundException;
    IncidentStatusResponse findIncidentStatusByCode(String code) throws NotFoundException;
    IncidentStatusResponse editIncidentStatus(Long id, IncidentStatus IncidentStatus) throws NotFoundException;
    IncidentStatusResponse deleteIncidentStatusById(Long id) throws NotFoundException;
    IncidentStatusResponse findAll() ;
    IncidentStatusResponse filterByName(String name) throws NotFoundException;
}
