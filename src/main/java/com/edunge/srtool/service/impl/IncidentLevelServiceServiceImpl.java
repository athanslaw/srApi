package com.edunge.srtool.service.impl;

import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.IncidentLevel;
import com.edunge.srtool.repository.IncidentLevelRepository;
import com.edunge.srtool.response.IncidentLevelResponse;
import com.edunge.srtool.service.IncidentLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IncidentLevelServiceServiceImpl implements IncidentLevelService {
    private final IncidentLevelRepository incidentLevelRepository;

    private static final String SERVICE_NAME = "Incident Level";

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
    public IncidentLevelServiceServiceImpl(IncidentLevelRepository incidentLevelRepository) {
        this.incidentLevelRepository = incidentLevelRepository;
    }

    @Override
    public IncidentLevelResponse saveIncidentLevel(IncidentLevel incidentLevel) throws NotFoundException {
        IncidentLevel existingIncidentLevel = incidentLevelRepository.findByCode(incidentLevel.getCode());
        if(existingIncidentLevel==null){
            incidentLevelRepository.save(incidentLevel);
            return new IncidentLevelResponse("00", String.format(successTemplate,SERVICE_NAME), existingIncidentLevel);
        }
        throw new DuplicateException(String.format(duplicateTemplate, incidentLevel.getCode()));
    }

    @Override
    public IncidentLevelResponse findIncidentLevelById(Long id) throws NotFoundException {
        IncidentLevel incidentLevel = getIncidentLevel(id);
        return new IncidentLevelResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), incidentLevel);
    }

    @Override
    public IncidentLevelResponse findIncidentLevelByCode(String code) throws NotFoundException {
        IncidentLevel incidentLevel = incidentLevelRepository.findByCode(code);
        if(incidentLevel==null){
            throw new NotFoundException(String.format(notFoundTemplate, SERVICE_NAME+ " " +code));
        }
        return new IncidentLevelResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), incidentLevel);
    }

    @Override
    public IncidentLevelResponse editIncidentLevel(Long id, IncidentLevel incidentLevel) throws NotFoundException {
        IncidentLevel currentIncident =  getIncidentLevel(id);
        currentIncident.setId(id);
        currentIncident.setCode(incidentLevel.getCode());
        currentIncident.setName(incidentLevel.getName());
        incidentLevelRepository.save(currentIncident);
        return new IncidentLevelResponse("00", String.format(updateTemplate, SERVICE_NAME), currentIncident);
    }

    @Override
    public IncidentLevelResponse deleteIncidentLevelById(Long id) throws NotFoundException {
        IncidentLevel currentIncidentLevel = getIncidentLevel(id);
        return new IncidentLevelResponse("00",String.format(deleteTemplate,currentIncidentLevel.getCode()));
    }

    @Override
    public IncidentLevelResponse findAll() {
        List<IncidentLevel> incidentLevels = incidentLevelRepository.findAll();
        return new IncidentLevelResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), incidentLevels);
    }

    private IncidentLevel getIncidentLevel(Long id) throws NotFoundException {
        Optional<IncidentLevel> currentState = incidentLevelRepository.findById(id);
        if(!currentState.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate, SERVICE_NAME));
        }
        return currentState.get();
    }

    @Override
    public IncidentLevelResponse filterByName(String name) throws NotFoundException {
        IncidentLevel incidentLevel = incidentLevelRepository.findByNameLike(name);
        if(incidentLevel!=null){
            return new IncidentLevelResponse("00", String.format(successTemplate,SERVICE_NAME), incidentLevel);
        }
        throw new NotFoundException(String.format(notFoundTemplate, SERVICE_NAME));
    }
}
