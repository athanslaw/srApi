package com.edunge.srtool.service;

import com.edunge.srtool.dto.IncidentDto;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.IncidentResponse;

public interface IncidentService {
    IncidentResponse saveIncident(IncidentDto incidentDto) throws NotFoundException;
    IncidentResponse findIncidentById(Long id) throws NotFoundException;
    IncidentResponse updateIncident(Long id, IncidentDto incidentDto) throws NotFoundException;
    IncidentResponse deleteIncidentById(Long id) throws NotFoundException;
    IncidentResponse findAll() ;
}
