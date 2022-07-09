package com.edunge.srtool.service.impl;

import com.edunge.srtool.dto.IncidentDto;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.*;
import com.edunge.srtool.repository.*;
import com.edunge.srtool.response.IncidentResponse;
import com.edunge.srtool.service.FileProcessingService;
import com.edunge.srtool.service.IncidentService;
import com.edunge.srtool.util.FileUtil;
import com.edunge.srtool.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IncidentServiceImpl implements IncidentService {
    private final IncidentRepository incidentRepository;
    private final PartyAgentRepository partyAgentRepository;
    private final ElectionRepository electionRepository;
    private final SenatorialDistrictRepository senatorialDistrictRepository;
    private final WardRepository wardRepository;
    private final LgaRepository lgaRepository;
    private final StateRepository stateRepository;
    private final PollingUnitRepository pollingUnitRepository;
    private final VotingLevelRepository votingLevelRepository;
    private final IncidentLevelRepository incidentLevelRepository;
    private final IncidentStatusRepository incidentStatusRepository;
    private final IncidentTypeRepository incidentTypeRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(IncidentServiceImpl.class);

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
    FileProcessingService fileProcessingService;

    @Autowired
    public IncidentServiceImpl(IncidentRepository incidentRepository, PartyAgentRepository partyAgentRepository,
                               ElectionRepository electionRepository, SenatorialDistrictRepository senatorialDistrictRepository,
                               WardRepository wardRepository, LgaRepository lgaRepository, StateRepository stateRepository,
                               PollingUnitRepository pollingUnitRepository, VotingLevelRepository votingLevelRepository,
                               IncidentLevelRepository incidentLevelRepository, IncidentStatusRepository incidentStatusRepository,
                               IncidentTypeRepository incidentTypeRepository) {
        this.incidentRepository = incidentRepository;
        this.partyAgentRepository = partyAgentRepository;
        this.electionRepository = electionRepository;
        this.senatorialDistrictRepository = senatorialDistrictRepository;
        this.wardRepository = wardRepository;
        this.lgaRepository = lgaRepository;
        this.stateRepository = stateRepository;
        this.pollingUnitRepository = pollingUnitRepository;
        this.votingLevelRepository = votingLevelRepository;
        this.incidentLevelRepository = incidentLevelRepository;
        this.incidentStatusRepository = incidentStatusRepository;
        this.incidentTypeRepository = incidentTypeRepository;
    }

    @Override
    public IncidentResponse saveIncident(IncidentDto incidentDto) throws NotFoundException {

        Incident incident = new Incident();

        State state = getState();
        incidentDto.setStateId(state.getId());
        Lga lga = getLga(incidentDto.getLgaId());
        // validate incident levels
        if(incidentDto.getIncidentLevelId() == 2){
            // get all PUs in ward
            Ward ward = getWard(incidentDto.getWardId());
            pollingUnitRepository.findByWard(ward).forEach(pollingUnit -> {
                incidentDto.setPollingUnitId(pollingUnit.getId());
                try {
                    saveIncidentSingle(incidentDto, lga, ward);
                }catch (NotFoundException ne){
                }
            });
        }
        else if(incidentDto.getIncidentLevelId() == 1){
            // get all wards and PUs under the LGA
            pollingUnitRepository.findByLga(lga).forEach(pollingUnit -> {
                incidentDto.setWardId(pollingUnit.getWard().getId());
                incidentDto.setPollingUnitId(pollingUnit.getId());
                try {
                    Ward ward = getWard(incidentDto.getWardId());
                    saveIncidentSingle(incidentDto, lga, ward);
                }catch (NotFoundException ne){
                }
            });
        }
        else {
            Ward ward = getWard(incidentDto.getWardId());
            incident = saveIncidentSingle(incidentDto, lga, ward);
        }
        return new IncidentResponse("00", String.format(successTemplate,SERVICE_NAME), incident);
    }

    public Incident saveIncidentSingle(IncidentDto incidentDto, Lga lga, Ward ward) throws NotFoundException {
        // check for conflict and give 15 mins interval
        String combinedKeys = new StringBuilder().append(incidentDto.getPollingUnitId())
                .append(incidentDto.getIncidentStatusId())
                .append(incidentDto.getIncidentTypeId())
                .append(incidentDto.getLgaId())
                .append(incidentDto.getWardId()).toString();

        List<Incident> incidentChecks = incidentRepository.findByCombinedKeysOrderByTimeStampDesc(combinedKeys);
        if(incidentChecks.size() > 0 && Utilities.dateDifference(incidentChecks.get(0).getTimeStamp(), 15)){
            return null;
        }

        IncidentLevel incidentLevel = incidentLevel(incidentDto.getIncidentLevelId());
        IncidentStatus incidentStatus = getIncidentStatus(incidentDto.getIncidentStatusId());
        IncidentType incidentType = getIncidentType(incidentDto.getIncidentTypeId());

        PollingUnit pollingUnit = null;
        try {
            pollingUnit = getPollingUnit(incidentDto.getPollingUnitId());
        }catch (Exception e){
        }
       // float weight = (incidentType.getWeight() + this.processWeight()) /2;
        Incident incident = new Incident();
        incident.setLga(lga);
        incident.setStateId(incidentDto.getStateId());
        incident.setTimeStamp(LocalDateTime.now());
        incident.setCombinedKeys(combinedKeys);
        incident.setWard(ward);
        incident.setPollingUnit(pollingUnit);
        incident.setIncidentLevel(incidentLevel);
        incident.setIncidentStatus(incidentStatus);
        incident.setIncidentType(incidentType);
        incident.setLga(lga);
        incident.setDescription(incidentDto.getDescription());
        incident.setReportedLocation(incidentDto.getReportedLocation());
        incident.setPhoneNumberToContact(incidentDto.getPhoneNumberToContact());
        incident.setWeight((int) incidentDto.getWeight());
        incidentRepository.save(incident);
        return incident;
    }

    private int processWeight(){
        SimpleDateFormat sd = new SimpleDateFormat("HH");
        String str = sd.format(new Date());
        int weight = 1;
        if(str.compareTo("0") >= 0){
            weight = 5;
        }else if(str.compareTo("09") >= 0){
            weight = 3;
        }
        return weight;
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
        incident.setWeight((int)incidentDto.getWeight());
        incidentRepository.save(incident);
        return new IncidentResponse("00", String.format(successTemplate,SERVICE_NAME), incident);
    }

    @Override
    public IncidentResponse deleteIncidentById(Long id) throws NotFoundException {
        incidentRepository.deleteById(id);
        return new IncidentResponse("00",String.format(deleteTemplate,SERVICE_NAME));
    }

    @Override
    public IncidentResponse findAll(String incidentType, String incidentWeight) {
        State state = getState();
        List<Incident> elections = new ArrayList<>();
        if((incidentType != null && !incidentType.trim().equals("")) || (incidentWeight != null && !incidentWeight.trim().equals(""))){
            try {
                if (incidentType == null || incidentType.equals("")) {
                    int weight = Integer.parseInt(incidentWeight.trim());
                    elections = incidentRepository.findByWeight(weight);
                } else if (incidentWeight == null || incidentWeight.equals("")) {
                    long type = Long.parseLong(incidentType.trim());
                    elections = incidentRepository.findByIncidentType(getIncidentType(type));
                } else {
                    int weight = Integer.parseInt(incidentWeight);
                    long type = Long.parseLong(incidentType.trim());
                    elections = incidentRepository.findByWeight(weight);
                    elections = elections.stream().filter(election -> election.getIncidentType().getId()==type)
                            .collect(Collectors.toList());
                }
            }catch (NotFoundException e){}
        }else{
            elections = incidentRepository.findTop10(state);
        }
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

    private State getState() {
        State state = stateRepository.findByDefaultState(true);
        return state;
    }

    private SenatorialDistrict getSenatorialDistrict(long id) {
        SenatorialDistrict senatorialDistrict = senatorialDistrictRepository.findById(id).get();
        return senatorialDistrict;
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
    public IncidentResponse findIncidentByLga(Long id, String incidentType, String incidentWeight){
        Lga lga = new Lga(){{setId(id);}};
        List<Incident> elections;
        if((incidentType != null && !incidentType.trim().equals("")) || (incidentWeight != null && !incidentWeight.trim().equals(""))){
            if(incidentType == null || incidentType.trim().equals("")){
                int weight = Integer.parseInt(incidentWeight.trim());
                elections = incidentRepository.findByLgaAndWeight(lga, weight);
            }
            else if(incidentWeight == null || incidentWeight.trim().equals("")){
                long type = Long.parseLong(incidentType.trim());
                elections = incidentRepository.findByLgaAndIncidentType(lga, new IncidentType(){{setId(type);}});
            }
            else{
                int weight = Integer.parseInt(incidentWeight.trim());
                long type = Long.parseLong(incidentType.trim());
                elections = incidentRepository.findByLgaAndWeight(lga, weight);
                elections = elections.stream().filter(election -> election.getIncidentType().getId()==type).collect(Collectors.toList());
            }
        }
        else{
            elections = incidentRepository.findByLga(lga);
        }
        return new IncidentResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), elections);
    }

    @Override
    public IncidentResponse findIncidentBySenatorial(Long id, String incidentType, String incidentWeight){
        SenatorialDistrict senatorialDistrict = new SenatorialDistrict(){{setId(id);}};
        List<Lga> lgas = lgaRepository.findBySenatorialDistrict(senatorialDistrict);
        List<Incident> incidentList = new ArrayList<>();
        lgas.stream().forEach(lga ->
            incidentList.addAll(this.findIncidentByLga(lga.getId(), incidentType, incidentWeight).getIncidents())
        );
        try{
            Long incidentTypeId = Long.parseLong(incidentType.trim());
            incidentList.stream()
                    .filter(election -> election.getIncidentType().getId() == incidentTypeId)
                    .collect(Collectors.toList());
        }catch (Exception e){}
        try{
            Long incidentWeightId = Long.parseLong(incidentWeight);
            incidentList.stream()
                    .filter(election -> election.getWeight() == incidentWeightId)
                    .collect(Collectors.toList());
        }catch (Exception e){}
        return new IncidentResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), incidentList);
    }

    @Override
    public IncidentResponse findIncidentByWard(Long id, String incidentType, String incidentWeight) throws NotFoundException {
        Ward ward = new Ward(){{setId(id);}};
        List<Incident> elections;

        if((incidentType != null && !incidentType.trim().equals("")) || (incidentWeight != null && !incidentWeight.trim().equals(""))){
            if(incidentType == null || incidentType.trim().equals("")){
                int weight = Integer.parseInt(incidentWeight.trim());
                elections = incidentRepository.findByWardAndWeight(ward, weight);
            }
            else if(incidentWeight == null || incidentWeight.trim().equals("")){
                long type = Long.parseLong(incidentType.trim());
                elections = incidentRepository.findByWardAndIncidentType(ward, new IncidentType(){{setId(type);}});
            }
            else{
                int weight = Integer.parseInt(incidentWeight.trim());
                long type = Long.parseLong(incidentType.trim());
                elections = incidentRepository.findByWardAndWeight(ward, weight);
                elections = elections.stream().filter(election -> election.getIncidentType().getId()==type).collect(Collectors.toList());
            }
        }
        else{
            elections = incidentRepository.findByWard(ward);
        }
        return new IncidentResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), elections);
    }

    @Override
    public IncidentResponse findIncidentByPollingUnit(Long id, String incidentType, String incidentWeight) throws NotFoundException {
        PollingUnit pollingUnit = new PollingUnit(){{setId(id);}};
        List<Incident> elections;

        if((incidentType != null && !incidentType.trim().equals("")) || (incidentWeight != null && !incidentWeight.trim().equals(""))){
            if(incidentType == null || incidentType.trim().equals("")){
                int weight = Integer.parseInt(incidentWeight.trim());
                elections = incidentRepository.findByPollingUnitAndWeight(pollingUnit, weight);
            }
            else if(incidentWeight == null || incidentWeight.trim().equals("")){
                long type = Long.parseLong(incidentType.trim());
                elections = incidentRepository.findByPollingUnitAndIncidentType(pollingUnit, new IncidentType(){{setId(type);}});
            }
            else{
                int weight = Integer.parseInt(incidentWeight.trim());
                long type = Long.parseLong(incidentType.trim());
                elections = incidentRepository.findByPollingUnitAndWeight(pollingUnit, weight);
                elections = elections.stream().filter(election -> election.getIncidentType().getId()==type).collect(Collectors.toList());
            }
        }
        else{
            elections = incidentRepository.findByPollingUnit(pollingUnit);
        }

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
                              String phoneNumberToContact,
                              String severity
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
            try{
                int weight = Integer.parseInt(severity);
                incident.setWeight(weight);
            }
            catch (Exception e){
                incident.setWeight(1);
            }
            incidentRepository.save(incident);
        }
        catch (Exception ex){
            LOGGER.info(String.format("%s | %s | %s Incident could not be saved",incidentLevelCode, lgaCode, wardCode));
        }
    }

    @Override
    public IncidentResponse uploadIncident(MultipartFile file){
        List<String> csvLines = FileUtil.getCsvLines(file, fileProcessingService.getFileStorageLocation());
        return processUpload(csvLines);
    }

    private IncidentResponse processUpload(List<String> lines){
        for (String line:lines) {
            String[] state = line.split(",");
            saveIncident(state[0].trim(), state[1].trim(), state[2].trim(),state[3].trim(), state[4].trim(), state[5].trim(),state[6].trim(), state[7].trim(), state[8].trim(), state[9].trim());
        }
        return new IncidentResponse("00", "File Uploaded.");
    }
}
