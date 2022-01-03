package com.edunge.srtool.service.impl;

import com.edunge.srtool.config.FileConfigurationProperties;
import com.edunge.srtool.dto.IncidentDto;
import com.edunge.srtool.exceptions.FileNotFoundException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.*;
import com.edunge.srtool.repository.*;
import com.edunge.srtool.response.IncidentResponse;
import com.edunge.srtool.service.IncidentRealTimeService;
import com.edunge.srtool.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class IncidentRealTimeServiceImpl implements IncidentRealTimeService {
    private final IncidentRepository incidentRepository;
    private final PartyAgentRepository partyAgentRepository;
    private final ElectionRepository electionRepository;
    private final SenatorialDistrictRepository senatorialDistrictRepository;
    private final WardRepository wardRepository;
    private final LgaRepository lgaRepository;
    private final PollingUnitRepository pollingUnitRepository;
    private final VotingLevelRepository votingLevelRepository;
    private final IncidentLevelRepository incidentLevelRepository;
    private final IncidentStatusRepository incidentStatusRepository;
    private final IncidentTypeRepository incidentTypeRepository;
    private final Path fileStorageLocation;
    private static final Logger LOGGER = LoggerFactory.getLogger(IncidentRealTimeServiceImpl.class);

    private static final String SERVICE_NAME = "Incident";

    @Value("${notfound.message.template}")
    private String notFoundTemplate;

    @Value("${success.message.template}")
    private String successTemplate;

    @Value("${update.message.template}")
    private String updateTemplate;

    @Value("${delete.message.template}")
    private String deleteTemplate;

    @Value("${fetch.message.template}")
    private String fetchRecordTemplate;

    @Autowired
    public IncidentRealTimeServiceImpl(IncidentRepository incidentRepository, PartyAgentRepository partyAgentRepository,
                                       ElectionRepository electionRepository, SenatorialDistrictRepository senatorialDistrictRepository,
                                       WardRepository wardRepository, LgaRepository lgaRepository, PollingUnitRepository pollingUnitRepository, VotingLevelRepository votingLevelRepository, IncidentLevelRepository incidentLevelRepository, IncidentStatusRepository incidentStatusRepository, IncidentTypeRepository incidentTypeRepository, FileConfigurationProperties fileConfigurationProperties) {
        this.incidentRepository = incidentRepository;
        this.partyAgentRepository = partyAgentRepository;
        this.electionRepository = electionRepository;
        this.senatorialDistrictRepository = senatorialDistrictRepository;
        this.wardRepository = wardRepository;
        this.lgaRepository = lgaRepository;
        this.pollingUnitRepository = pollingUnitRepository;
        this.votingLevelRepository = votingLevelRepository;
        this.incidentLevelRepository = incidentLevelRepository;
        this.incidentStatusRepository = incidentStatusRepository;
        this.incidentTypeRepository = incidentTypeRepository;
        this.fileStorageLocation = Paths.get(fileConfigurationProperties.getSvgDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileNotFoundException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public IncidentResponse findIncidentById(Long id) throws NotFoundException {
        Incident incident = getIncident(id);
        return new IncidentResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), incident);
    }

    @Override
    public IncidentResponse updateIncident(Long id, IncidentDto incidentDto) throws NotFoundException {
        Incident incident = getIncident(id);
        IncidentLevel incidentLevel = incidentLevel(incidentDto.getIncidentLevelId());
        IncidentStatus incidentStatus = getIncidentStatus(incidentDto.getIncidentStatusId());
        IncidentType incidentType = getIncidentType(incidentDto.getIncidentTypeId());

        Lga lga = getLga(incidentDto.getLgaId());
        PollingUnit pollingUnit = getPollingUnit(incidentDto.getPollingUnitId());
        Election election = getElection(incidentDto.getIncidentStatusId());
        Ward ward = getWard(incidentDto.getWardId());
        incident.setLga(lga);
        incident.setWard(ward);
        incident.setPollingUnit(pollingUnit);
        incident.setIncidentLevel(incidentLevel);
        incident.setIncidentStatus(incidentStatus);
        incident.setIncidentType(incidentType);
        incident.setLga(lga);
        incident.setDescription(incidentDto.getDescription());
        incident.setReportedLocation(incidentDto.getReportedLocation());
        incident.setPhoneNumberToContact(incidentDto.getPhoneNumberToContact());
        incidentRepository.save(incident);
        return new IncidentResponse("00", String.format(successTemplate,SERVICE_NAME), incident);
    }

    @Override
    public IncidentResponse deleteIncidentById(Long id) throws NotFoundException {
        Incident incident = getIncident(id);
        incidentRepository.deleteById(id);
        return new IncidentResponse("00",String.format(deleteTemplate,SERVICE_NAME));
    }

    @Override
    public IncidentResponse findAll() {
        List<Incident> elections = incidentRepository.findAll();
        return new IncidentResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), elections);
    }


    private Election getElection(Long id) throws NotFoundException {
        Optional<Election> election = electionRepository.findById(id);
        if(!election.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,"Election"));
        }
        return election.get();
    }

    private IncidentType getIncidentType(Long id) throws NotFoundException {
        Optional<IncidentType> incidentType = incidentTypeRepository.findById(id);
        if(!incidentType.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,"Incident Type"));
        }
        return incidentType.get();
    }

    private Lga getLga(Long id) throws NotFoundException {
        Optional<Lga> lga = lgaRepository.findById(id);
        if(!lga.isPresent()){
            throw new NotFoundException("LGA not found.");
        }
        return lga.get();
    }

    private IncidentStatus getIncidentStatus(Long id) throws NotFoundException {
        Optional<IncidentStatus> incidentStatus = incidentStatusRepository.findById(id);
        if(!incidentStatus.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,"Incident Status"));
        }
        return incidentStatus.get();
    }

    private Ward getWard(Long id) throws NotFoundException {
        Optional<Ward> currentWard = wardRepository.findById(id);
        if(!currentWard.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,"Ward"));
        }
        return currentWard.get();
    }

    private IncidentLevel incidentLevel(Long id) throws NotFoundException {
        Optional<IncidentLevel> incidentLevel = incidentLevelRepository.findById(id);
        if(!incidentLevel.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,"Incident Level"));
        }
        return incidentLevel.get();
    }

    private PollingUnit getPollingUnit(Long id) throws NotFoundException {
        Optional<PollingUnit> currentPollingUnit = pollingUnitRepository.findById(id);
        if(!currentPollingUnit.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,"Polling Unit"));
        }
        return currentPollingUnit.get();
    }

    private Incident getIncident(Long id) throws NotFoundException {
        Optional<Incident> incident = incidentRepository.findById(id);
        if(!incident.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return incident.get();
    }

    @Override
    public IncidentResponse findIncidentByLga(Long id) throws NotFoundException {
        Lga lga = getLga(id);
        List<Incident> elections = incidentRepository.findByLga(lga);
        return new IncidentResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), elections);
    }

    @Override
    public IncidentResponse findIncidentByWard(Long id) throws NotFoundException {
        Ward ward = getWard(id);
        List<Incident> elections = incidentRepository.findByWard(ward);
        return new IncidentResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), elections);
    }

    @Override
    public IncidentResponse findIncidentByPollingUnit(Long id) throws NotFoundException {
        PollingUnit pollingUnit = getPollingUnit(id);
        List<Incident> elections = incidentRepository.findByPollingUnit(pollingUnit);
        return new IncidentResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), elections);
    }

    private void saveIncident(
                              String incidentLevelCode,
                              String incidentTypeCode,
                              String incidentStatusCode,
                              String lgaCode,
                              String wardCode,
                              String pollingUnitCode,
                              String description,
                              String reportedLocation,
                              String phoneNumberToContact
    ) {
        try{
            Lga lga = lgaRepository.findByCode(lgaCode);
            Ward ward = wardRepository.findByCode(wardCode);
            PollingUnit pollingUnit = pollingUnitRepository.findByCode(pollingUnitCode);
            IncidentLevel incidentLevel = incidentLevelRepository.findByCode(incidentLevelCode);
            IncidentType incidentType = incidentTypeRepository.findByCode(incidentTypeCode);
            IncidentStatus incidentStatus = incidentStatusRepository.findByCode(incidentStatusCode);
            Incident incident = new Incident();
            incident.setIncidentType(incidentType);
            incident.setWard(ward);
            incident.setLga(lga);
            incident.setIncidentStatus(incidentStatus);
            incident.setPollingUnit(pollingUnit);
            incident.setDescription(description);
            incident.setReportedLocation(reportedLocation);
            incident.setIncidentLevel(incidentLevel);
            incident.setPhoneNumberToContact(phoneNumberToContact);
            incidentRepository.save(incident);
        }
        catch (Exception ex){
            LOGGER.info(String.format("%s | %s | %s Incident could not be saved",incidentLevelCode, lgaCode, wardCode));
        }
    }

    @Override
    public IncidentResponse uploadIncident(MultipartFile file){
        List<String> csvLines = FileUtil.getCsvLines(file, this.fileStorageLocation);
        return processUpload(csvLines);
    }

    private IncidentResponse processUpload(List<String> lines){
        for (String line:lines) {
            String[] state = line.split(",");
            saveIncident(state[0].trim(), state[1].trim(), state[2].trim(),state[3].trim(), state[4].trim(), state[5].trim(),state[6].trim(), state[7].trim(), state[8].trim());
        }
        return new IncidentResponse("00", "File Uploaded.");
    }
}
