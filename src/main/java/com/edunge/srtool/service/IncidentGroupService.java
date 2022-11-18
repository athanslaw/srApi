package com.edunge.srtool.service;

import com.edunge.srtool.dto.IncidentGroupDto;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.IncidentGroupResponse;

public interface IncidentGroupService {
    IncidentGroupResponse saveIncidentGroup(IncidentGroupDto incidentGroupDto) throws NotFoundException;
    IncidentGroupResponse findIncidentGroupById(Long id) throws NotFoundException;
    IncidentGroupResponse updateIncidentGroup(Long id, IncidentGroupDto incidentGroupDto) throws NotFoundException;
    IncidentGroupResponse activateIncidentGroup(Long id) throws NotFoundException;
    IncidentGroupResponse deleteIncidentGroupById(Long id) throws NotFoundException;
    IncidentGroupResponse findAll();
    IncidentGroupResponse findActiveIncidentGroups();
    void updateActiveIncidentGroup(Long id);
}
