package com.edunge.srtool.service.impl;

import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.IncidentStatus;
import com.edunge.srtool.repository.IncidentStatusRepository;
import com.edunge.srtool.response.IncidentStatusResponse;
import com.edunge.srtool.service.IncidentStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IncidentStatusServiceServiceImpl implements IncidentStatusService {
    private final IncidentStatusRepository incidentStatusRepository;

    private static final String SERVICE_NAME = "Incident Status";

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
    public IncidentStatusServiceServiceImpl(IncidentStatusRepository IncidentStatusRepository) {
        this.incidentStatusRepository = IncidentStatusRepository;
    }

    @Override
    public IncidentStatusResponse saveIncidentStatus(IncidentStatus IncidentStatus) throws NotFoundException {
        IncidentStatus existingIncidentStatus = incidentStatusRepository.findByCode(IncidentStatus.getCode());
        if(existingIncidentStatus==null){
            incidentStatusRepository.save(IncidentStatus);
            return new IncidentStatusResponse("00", String.format(successTemplate,SERVICE_NAME), existingIncidentStatus);
        }
        throw new DuplicateException(String.format(duplicateTemplate, IncidentStatus.getCode()));
    }

    @Override
    public IncidentStatusResponse findIncidentStatusById(Long id) throws NotFoundException {
        IncidentStatus IncidentStatus = getIncidentStatus(id);
        return new IncidentStatusResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), IncidentStatus);
    }

    @Override
    public IncidentStatusResponse findIncidentStatusByCode(String code) throws NotFoundException {
        IncidentStatus IncidentStatus = incidentStatusRepository.findByCode(code);
        if(IncidentStatus==null){
            throw new NotFoundException(String.format(notFoundTemplate, SERVICE_NAME+ " " +code));
        }
        return new IncidentStatusResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), IncidentStatus);
    }

    @Override
    public IncidentStatusResponse editIncidentStatus(Long id, IncidentStatus IncidentStatus) throws NotFoundException {
        IncidentStatus currentIncident =  getIncidentStatus(id);
        currentIncident.setId(id);
        currentIncident.setCode(IncidentStatus.getCode());
        currentIncident.setName(IncidentStatus.getName());
        incidentStatusRepository.save(currentIncident);
        return new IncidentStatusResponse("00", String.format(updateTemplate, SERVICE_NAME), currentIncident);
    }

    @Override
    public IncidentStatusResponse deleteIncidentStatusById(Long id) throws NotFoundException {
        IncidentStatus currentIncidentStatus = getIncidentStatus(id);
        return new IncidentStatusResponse("00",String.format(deleteTemplate,currentIncidentStatus.getCode()));
    }

    @Override
    public IncidentStatusResponse filterByName(String name) throws NotFoundException {
        IncidentStatus incidentStatus = incidentStatusRepository.findByNameLike(name);
        if(incidentStatus!=null){
            return new IncidentStatusResponse("00", String.format(successTemplate,SERVICE_NAME), incidentStatus);
        }
        throw new NotFoundException(String.format(notFoundTemplate, SERVICE_NAME));
    }

    @Override
    public IncidentStatusResponse findAll() {
        List<IncidentStatus> IncidentStatuss = incidentStatusRepository.findAll();
        return new IncidentStatusResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), IncidentStatuss);
    }

    private IncidentStatus getIncidentStatus(Long id) throws NotFoundException {
        Optional<IncidentStatus> currentState = incidentStatusRepository.findById(id);
        if(!currentState.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate, SERVICE_NAME));
        }
        return currentState.get();
    }
}
