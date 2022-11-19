package com.edunge.srtool.service.impl;

import com.edunge.srtool.dto.IncidentGroupDto;
import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.Event;
import com.edunge.srtool.model.IncidentGroup;
import com.edunge.srtool.repository.IncidentGroupRepository;
import com.edunge.srtool.response.EventResponse;
import com.edunge.srtool.response.IncidentGroupResponse;
import com.edunge.srtool.service.IncidentGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IncidentGroupServiceImpl implements IncidentGroupService {

    @Autowired
    private IncidentGroupRepository incidentGroupRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(IncidentGroupServiceImpl.class);

    private static final String SERVICE_NAME = "IncidentGroup";

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

    @Override
    public IncidentGroupResponse saveIncidentGroup(IncidentGroupDto incidentGroupDto) throws NotFoundException {

        try {
            IncidentGroup incidentGroup = new IncidentGroup();
            incidentGroup.setCode(incidentGroupDto.getCode());
            incidentGroup.setName("");
            incidentGroup.setDescription(incidentGroupDto.getDescription());
            incidentGroup.setYear(incidentGroupDto.getYear());
            incidentGroupRepository.save(incidentGroup);
            return new IncidentGroupResponse("00", String.format(successTemplate, SERVICE_NAME), incidentGroup);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        throw new DuplicateException(String.format(duplicateTemplate, incidentGroupDto.getCode()));
    }

    @Override
    public IncidentGroupResponse findIncidentGroupById(Long id) throws NotFoundException {
        IncidentGroup incidentGroup = getIncidentGroup(id);
        return new IncidentGroupResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), incidentGroup);
    }

    @Override
    public IncidentGroupResponse updateIncidentGroup(Long id, IncidentGroupDto incidentGroupDto) throws NotFoundException {
        IncidentGroup incidentGroup = getIncidentGroup(id);
        try {
            incidentGroup.setId(id);
            incidentGroup.setCode(incidentGroupDto.getCode());
            incidentGroup.setName(incidentGroupDto.getName());
            incidentGroup.setDescription(incidentGroupDto.getDescription());
            incidentGroup.setYear(incidentGroupDto.getYear());
            incidentGroupRepository.save(incidentGroup);
            return new IncidentGroupResponse("00", String.format(updateTemplate, SERVICE_NAME), incidentGroup);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public IncidentGroupResponse activateIncidentGroup(Long id) throws NotFoundException {
        List<IncidentGroup> incidentGroups = incidentGroupRepository.findByStatus(true);
        for(int i=0; i<incidentGroups.size(); i++){
            IncidentGroup e = incidentGroups.get(i);
            e.setStatus(false);
            incidentGroupRepository.save(e);
        }
        IncidentGroup incidentGroup = getIncidentGroup(id);
        try {
            incidentGroup.setStatus(true);
            return new IncidentGroupResponse("00", String.format(updateTemplate, SERVICE_NAME), incidentGroup);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    @Override
    public IncidentGroupResponse deleteIncidentGroupById(Long id) throws NotFoundException {
        IncidentGroup incidentGroup = getIncidentGroup(id);
        incidentGroupRepository.deleteById(id);
        return new IncidentGroupResponse("00",String.format(deleteTemplate,incidentGroup.getCode()));
    }

    @Override
    public IncidentGroupResponse findAll() {
        List<IncidentGroup> incidentGroups = incidentGroupRepository.findAll();
        return new IncidentGroupResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), incidentGroups);
    }

    @Override
    public IncidentGroupResponse findActiveIncidentGroups() {
        return new IncidentGroupResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME),
                incidentGroupRepository.findByStatus(true));
    }

    @Override
    public void updateActiveIncidentGroup(Long id){
        IncidentGroup incidentGroup = incidentGroupRepository.findById(id).get();
        incidentGroup.setStatus(!incidentGroup.getStatus());
        incidentGroupRepository.save(incidentGroup);
    }

    private IncidentGroup getIncidentGroup(Long id) throws NotFoundException {
        Optional<IncidentGroup> currentIncidentGroup = incidentGroupRepository.findById(id);
        if(!currentIncidentGroup.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return currentIncidentGroup.get();
    }
}
