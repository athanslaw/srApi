package com.edunge.srtool.service.impl;

import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.IncidentType;
import com.edunge.srtool.repository.IncidentTypeRepository;
import com.edunge.srtool.response.IncidentTypeResponse;
import com.edunge.srtool.service.IncidentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IncidentTypeServiceServiceImpl implements IncidentTypeService {
    private final IncidentTypeRepository incidentTypeRepository;

    private static final String SERVICE_NAME = "Incident Type";

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

    @Autowired
    public IncidentTypeServiceServiceImpl(IncidentTypeRepository incidentTypeRepository) {
        this.incidentTypeRepository = incidentTypeRepository;
    }

    @Override
    public IncidentTypeResponse saveIncidentType(IncidentType IncidentType) throws NotFoundException {
        IncidentType existingIncidentType = incidentTypeRepository.findByCode(IncidentType.getCode());
        if(existingIncidentType==null){
            incidentTypeRepository.save(IncidentType);
            return new IncidentTypeResponse("00", String.format(successTemplate,SERVICE_NAME), existingIncidentType);
        }
        throw new DuplicateException(String.format(duplicateTemplate, IncidentType.getCode()));
    }

    @Override
    public IncidentTypeResponse findIncidentTypeById(Long id) throws NotFoundException {
        IncidentType IncidentType = getIncidentType(id);
        return new IncidentTypeResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), IncidentType);
    }

    @Override
    public IncidentTypeResponse findIncidentTypeByCode(String code) throws NotFoundException {
        IncidentType IncidentType = incidentTypeRepository.findByCode(code);
        if(IncidentType==null){
            throw new NotFoundException(String.format(notFoundTemplate, SERVICE_NAME+ " " +code));
        }
        return new IncidentTypeResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), IncidentType);
    }

    @Override
    public IncidentTypeResponse editIncidentType(Long id, IncidentType IncidentType) throws NotFoundException {
        IncidentType currentIncident =  getIncidentType(id);
        currentIncident.setId(id);
        currentIncident.setCode(IncidentType.getCode());
        currentIncident.setName(IncidentType.getName());
        incidentTypeRepository.save(currentIncident);
        return new IncidentTypeResponse("00", String.format(updateTemplate, SERVICE_NAME), currentIncident);
    }

    @Override
    public IncidentTypeResponse deleteIncidentTypeById(Long id) throws NotFoundException {
        IncidentType currentIncidentType = getIncidentType(id);
        return new IncidentTypeResponse("00",String.format(deleteTemplate,currentIncidentType.getCode()));
    }

    @Override
    public IncidentTypeResponse findAll() {
        List<IncidentType> IncidentTypes = incidentTypeRepository.findAll();
        return new IncidentTypeResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), IncidentTypes);
    }

    private IncidentType getIncidentType(Long id) throws NotFoundException {
        Optional<IncidentType> currentState = incidentTypeRepository.findById(id);
        if(!currentState.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate, SERVICE_NAME));
        }
        return currentState.get();
    }
}
