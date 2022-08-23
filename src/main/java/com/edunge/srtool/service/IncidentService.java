package com.edunge.srtool.service;

import com.edunge.srtool.dto.IncidentDto;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.IncidentResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IncidentService {
    IncidentResponse saveIncident(IncidentDto incidentDto) throws NotFoundException;
    IncidentResponse findIncidentById(Long id) throws NotFoundException;
    IncidentResponse updateIncident(Long id, IncidentDto incidentDto) throws NotFoundException;
    IncidentResponse deleteIncidentById(Long id) throws NotFoundException;
    IncidentResponse findAll(String incidentType, String incidentWeight) ;
    IncidentResponse findIncidentByLga(Long id, String incidentType, String incidentWeight);
    IncidentResponse findIncidentBySenatorial(Long id, String incidentType, String incidentWeight) throws NotFoundException;
    IncidentResponse findIncidentByStateId(Long id, String incidentType, String incidentWeight) throws NotFoundException;
    IncidentResponse findIncidentByZone(Long id, String incidentType, String incidentWeight) throws NotFoundException;
    IncidentResponse findIncidentByWard(Long id, String incidentType, String incidentWeight) throws NotFoundException;
    IncidentResponse findIncidentByPollingUnit(Long id, String incidentType, String incidentWeight) throws NotFoundException;

    IncidentResponse uploadIncident(MultipartFile file);
}
