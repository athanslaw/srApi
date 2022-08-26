package com.edunge.srtool.service.impl;

import com.edunge.srtool.dto.ElectionDto;
import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.Election;
import com.edunge.srtool.model.ElectionType;
import com.edunge.srtool.repository.ElectionRepository;
import com.edunge.srtool.repository.ElectionTypeRepository;
import com.edunge.srtool.response.ElectionResponse;
import com.edunge.srtool.response.ElectionTypeResponse;
import com.edunge.srtool.service.ElectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ElectionServiceImpl implements ElectionService {
    private final ElectionRepository electionRepository;
    private final ElectionTypeRepository electionTypeRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ElectionServiceImpl.class);

    private static final String SERVICE_NAME = "Election";

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
    public ElectionServiceImpl(ElectionRepository electionRepository, ElectionTypeRepository electionTypeRepository) {
        this.electionRepository = electionRepository;
        this.electionTypeRepository = electionTypeRepository;
    }

    @Override
    public ElectionResponse saveElection(ElectionDto electionDto) throws NotFoundException {

        try {
            Election election = new Election();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy");
            Date dateOfElection = simpleDateFormat.parse(electionDto.getDateOfElection());
            election.setCode(electionDto.getCode());
            election.setName(electionDto.getName());
            election.setDescription(electionDto.getDescription());
            election.setYear(electionDto.getYear());
            election.setDateOfElection(dateOfElection);
            electionRepository.save(election);
            return new ElectionResponse("00", String.format(successTemplate, SERVICE_NAME), election);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage());
        }
        throw new DuplicateException(String.format(duplicateTemplate, electionDto.getCode()));
    }

    @Override
    public ElectionResponse findElectionById(Long id) throws NotFoundException {
        Election election = getElection(id);
        return new ElectionResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), election);
    }

    @Override
    public ElectionResponse findElectionByCode(String code) throws NotFoundException {
        Election election = electionRepository.findByCode(code);
        if(election==null){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return new ElectionResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), election);
    }

    @Override
    public ElectionResponse updateElection(Long id, ElectionDto electionDto) throws NotFoundException {
        Election election = getElection(id);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy");
        try {
            Date dateOfElection = simpleDateFormat.parse(electionDto.getDateOfElection());
            election.setId(id);
            election.setCode(electionDto.getCode());
            election.setName(electionDto.getName());
            election.setDescription(electionDto.getDescription());
            election.setYear(electionDto.getYear());
            election.setDateOfElection(dateOfElection);
            electionRepository.save(election);
            return new ElectionResponse("00", String.format(updateTemplate, SERVICE_NAME), election);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public ElectionResponse deleteElectionById(Long id) throws NotFoundException {
        Election election = getElection(id);
        electionRepository.deleteById(id);
        return new ElectionResponse("00",String.format(deleteTemplate,election.getCode()));
    }

    @Override
    public ElectionResponse findAll() {
        List<Election> elections = electionRepository.findAll();
        return new ElectionResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), elections);
    }

    @Override
    public ElectionTypeResponse findAllElectionTypes() {
        return new ElectionTypeResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME),
                electionTypeRepository.findAll());
    }

    @Override
    public ElectionTypeResponse findActiveElectionTypes() {
        return new ElectionTypeResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME),
                electionTypeRepository.findByStatus(true));
    }

    @Override
    public void updateActiveElectionTypes(Long id){
        ElectionType electionType = electionTypeRepository.findById(id).get();
        electionType.setStatus(!electionType.isStatus());
        electionTypeRepository.save(electionType);
    }

    @Override
    public ElectionResponse filterByName(String name) throws NotFoundException {
        List<Election> election = electionRepository.findByNameStartingWith(name);
        if(election!=null){
            return new ElectionResponse("00", String.format(successTemplate,SERVICE_NAME), election);
        }
        throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
    }

    private Election getElection(Long id) throws NotFoundException {
        Optional<Election> currentElection = electionRepository.findById(id);
        if(!currentElection.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return currentElection.get();
    }
}
