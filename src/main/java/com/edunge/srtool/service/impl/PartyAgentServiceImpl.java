package com.edunge.srtool.service.impl;

import com.edunge.srtool.config.FileConfigurationProperties;
import com.edunge.srtool.dto.PartyAgentDto;
import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.FileNotFoundException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.*;
import com.edunge.srtool.repository.*;
import com.edunge.srtool.response.PartyAgentResponse;
import com.edunge.srtool.service.PartyAgentService;
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
public class PartyAgentServiceImpl implements PartyAgentService {

    private final LgaRepository lgaRepository;
    private final PartyAgentRepository partyAgentRepository;
    private final PollingUnitRepository pollingUnitRepository;
    private final WardRepository wardRepository;
    private final PoliticalPartyRepository politicalPartyRepository;
    private final SenatorialDistrictRepository senatorialDistrictRepository;

    private static final String SERVICE_NAME = "Party Agent";
    private final Path fileStorageLocation;
    private static final Logger LOGGER = LoggerFactory.getLogger(ResultServiceImpl.class);

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
    public PartyAgentServiceImpl(LgaRepository lgaRepository, PartyAgentRepository partyAgentRepository, PollingUnitRepository pollingUnitRepository, WardRepository wardRepository, PoliticalPartyRepository politicalPartyRepository, SenatorialDistrictRepository senatorialDistrictRepository, FileConfigurationProperties fileConfigurationProperties) {
        this.lgaRepository = lgaRepository;
        this.partyAgentRepository = partyAgentRepository;
        this.pollingUnitRepository = pollingUnitRepository;
        this.wardRepository = wardRepository;
        this.politicalPartyRepository = politicalPartyRepository;
        this.senatorialDistrictRepository = senatorialDistrictRepository;
        this.fileStorageLocation = Paths.get(fileConfigurationProperties.getSvgDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileNotFoundException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public PartyAgentResponse savePartyAgent(PartyAgentDto partyAgentDto) throws NotFoundException {
        PollingUnit pollingUnit = getPollingUnit(partyAgentDto.getPollingUnitId());
        Lga lga = getLga(partyAgentDto.getLgaId());
        Ward ward = getWard(partyAgentDto.getWardId());
        PartyAgent partyAgent = partyAgentRepository.findByPhone(partyAgentDto.getPhone());
        if(partyAgent==null){
            partyAgent  = new PartyAgent();
            partyAgent.setFirstname(partyAgentDto.getFirstname());
            partyAgent.setLastname(partyAgentDto.getLastname());
            partyAgent.setAddress(partyAgentDto.getAddress());
            partyAgent.setPhone(partyAgentDto.getPhone());
            partyAgent.setPollingUnit(pollingUnit);
            partyAgent.setWard(ward);
            if(!partyAgentDto.getEmail().isEmpty()) partyAgent.setEmail(partyAgentDto.getEmail());
            Optional<PoliticalParty> politicalParty = politicalPartyRepository.findById(partyAgentDto.getPoliticalPartyId());
            politicalParty.ifPresent(partyAgent::setPoliticalParty);
            partyAgent.setLga(lga);
            partyAgentRepository.save(partyAgent);
            return new PartyAgentResponse("00", String.format(successTemplate, SERVICE_NAME), partyAgent);
        }
        throw new DuplicateException(String.format(duplicateTemplate, partyAgent.getEmail()));
    }

    @Override
    public PartyAgentResponse findPartyAgentById(Long id) throws NotFoundException {
        PartyAgent partyAgent = getPartyAgent(id);
        return new PartyAgentResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), partyAgent);
    }

    @Override
    public PartyAgentResponse findPartyAgentByName(String firstname, String lastname) throws NotFoundException {
        List<PartyAgent> partyAgent = partyAgentRepository.findByFirstnameOrLastname(firstname, lastname);
        if(partyAgent==null){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return new PartyAgentResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), partyAgent);
    }

    @Override
    public PartyAgentResponse findPartyAgentByPhone(String phone) throws NotFoundException {
        PartyAgent partyAgent = partyAgentRepository.findByPhone(phone);
        if(partyAgent==null){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return new PartyAgentResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), partyAgent);
    }

    @Override
    public PartyAgentResponse updatePartyAgent(Long id, PartyAgentDto partyAgentDto) throws NotFoundException {

        PollingUnit pollingUnit = getPollingUnit(partyAgentDto.getPollingUnitId());
        Lga lga = getLga(partyAgentDto.getLgaId());
        Ward ward = getWard(partyAgentDto.getWardId());
        PartyAgent partyAgent = getPartyAgent(id);
        partyAgent.setFirstname(partyAgentDto.getFirstname());
        partyAgent.setLastname(partyAgentDto.getLastname());
        partyAgent.setAddress(partyAgentDto.getAddress());
        partyAgent.setPhone(partyAgentDto.getPhone());
        partyAgent.setPollingUnit(pollingUnit);
        partyAgent.setWard(ward);
        if(!partyAgentDto.getEmail().isEmpty()) partyAgent.setEmail(partyAgentDto.getEmail());
        Optional<PoliticalParty> politicalParty = politicalPartyRepository.findById(partyAgentDto.getPoliticalPartyId());
        politicalParty.ifPresent(partyAgent::setPoliticalParty);
        partyAgent.setLga(lga);
        partyAgentRepository.save(partyAgent);
        return new PartyAgentResponse("00", String.format(successTemplate, SERVICE_NAME), partyAgent);
    }

    @Override
    public PartyAgentResponse deletePartyAgentById(Long id) throws NotFoundException {
        PartyAgent partyAgent = getPartyAgent(id);
        partyAgentRepository.deleteById(id);
        return new PartyAgentResponse("00",String.format(deleteTemplate,partyAgent.getEmail()));
    }

    @Override
    public PartyAgentResponse findAll() {
        List<PartyAgent> partyAgents = partyAgentRepository.findAll();
        return new PartyAgentResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), partyAgents);
    }


    private Lga getLga(Long id) throws NotFoundException {
        Optional<Lga> currentLga = lgaRepository.findById(id);
        if(!currentLga.isPresent()){
            throw new NotFoundException("Lga not found.");
        }
        return currentLga.get();
    }

    private PartyAgent getPartyAgent(Long id) throws NotFoundException {
        Optional<PartyAgent> partyAgent = partyAgentRepository.findById(id);
        if(!partyAgent.isPresent()){
            throw new NotFoundException("Party agent not found.");
        }
        return partyAgent.get();
    }


    private PollingUnit getPollingUnit(Long id) throws NotFoundException {
        Optional<PollingUnit> pollingUnit = pollingUnitRepository.findById(id);
        if(!pollingUnit.isPresent()){
            throw new NotFoundException("Senatorial District not found.");
        }
        return pollingUnit.get();
    }

    private PoliticalParty getPoliticalParty(Long id) throws NotFoundException {
        Optional<PoliticalParty> politicalParty = politicalPartyRepository.findById(id);
        if(!politicalParty.isPresent()){
            throw new NotFoundException(String.format(fetchRecordTemplate, SERVICE_NAME));
        }
        return politicalParty.get();
    }

    private Ward getWard(Long id) throws NotFoundException {
        Optional<Ward> currentWard = wardRepository.findById(id);
        if(!currentWard.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return currentWard.get();
    }

    private void savePartyAgent(String surname,
                                String otherNames,
                                String phoneNumber,
                                String stateCode,
                                String senatorialDistrictCode,
                                String lgaCode,
                                String wardCode,
                                String pollingUnitCode,
                                String pollingUnitName)  {

        SenatorialDistrict senatorialDistrict = senatorialDistrictRepository.findByCode(senatorialDistrictCode);
        Lga lga = lgaRepository .findByCode(lgaCode);
        Ward ward = wardRepository.findByCode(wardCode);
        PollingUnit pollingUnit = pollingUnitRepository.findByCode(pollingUnitCode);
        PartyAgent partyAgent = partyAgentRepository.findByPhone(phoneNumber);

        try{
            if(partyAgent==null){
                partyAgent = new PartyAgent();
                partyAgent.setFirstname(otherNames);
                partyAgent.setLastname(surname);
                partyAgent.setAddress("KANO");
                partyAgent.setPhone(phoneNumber);
                partyAgent.setPollingUnit(pollingUnit);
                partyAgent.setWard(ward);
                partyAgent.setLga(lga);
                partyAgentRepository.save(partyAgent);
            }
        }
        catch (Exception ex){
            LOGGER.info("Party agent could not be saved");
        }
    }

    @Override
    public PartyAgentResponse uploadPartyAgent(MultipartFile file){
        List<String> csvLines = FileUtil.getCsvLines(file, this.fileStorageLocation);
        return processUpload(csvLines);
    }


    private PartyAgentResponse processUpload(List<String> lines){
        for (String line:lines) {
            String[] state = line.split(",");
            savePartyAgent(state[0].trim(), state[1].trim(), state[2].trim(), state[3].trim(),state[4].trim(),state[5].trim(),state[6].trim(),state[7].trim(),state[8].trim());
        }
        return new PartyAgentResponse("00", "File Uploaded.");
    }
}
