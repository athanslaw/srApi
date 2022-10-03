package com.edunge.srtool.service.impl;

import com.edunge.srtool.dto.PartyAgentDto;
import com.edunge.srtool.dto.UserDto;
import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.*;
import com.edunge.srtool.repository.*;
import com.edunge.srtool.response.PartyAgentResponse;
import com.edunge.srtool.service.FileProcessingService;
import com.edunge.srtool.service.PartyAgentService;
import com.edunge.srtool.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PartyAgentServiceImpl implements PartyAgentService {

    private final LgaRepository lgaRepository;
    private final PartyAgentRepository partyAgentRepository;
    private final PollingUnitRepository pollingUnitRepository;
    private final WardRepository wardRepository;
    private final PoliticalPartyRepository politicalPartyRepository;
    private final UserServiceImpl userService;
    private final StateRepository stateRepository;

    private static final String SERVICE_NAME = "Party Agent";
    private static final Logger LOGGER = LoggerFactory.getLogger(ResultServiceImpl.class);

    @Autowired
    FileProcessingService fileProcessingService;

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
    public PartyAgentServiceImpl(LgaRepository lgaRepository, PartyAgentRepository partyAgentRepository, PollingUnitRepository pollingUnitRepository, WardRepository wardRepository, PoliticalPartyRepository politicalPartyRepository, UserServiceImpl userService, StateRepository stateRepository) {
        this.lgaRepository = lgaRepository;
        this.partyAgentRepository = partyAgentRepository;
        this.pollingUnitRepository = pollingUnitRepository;
        this.wardRepository = wardRepository;
        this.politicalPartyRepository = politicalPartyRepository;
        this.userService = userService;
        this.stateRepository = stateRepository;
    }

    @Override
    public PartyAgentResponse savePartyAgent(PartyAgentDto partyAgentDto) throws NotFoundException {
        PollingUnit pollingUnit = getPollingUnit(Long.valueOf(partyAgentDto.getPollingUnitId()));
        Lga lga = getLga(Long.valueOf(partyAgentDto.getLgaId()));
        Ward ward = getWard(Long.valueOf(partyAgentDto.getWardId()));
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
            partyAgent.setRole(partyAgentDto.getRole());
            partyAgentRepository.save(partyAgent);

            // save in users table too
            userService.saveAgent(partyAgentToUserDto(partyAgent, partyAgentDto.getPwd()));

            return new PartyAgentResponse("00", String.format(successTemplate, SERVICE_NAME), partyAgent);
        }
        throw new DuplicateException(String.format(duplicateTemplate, "Matching record"));
    }

    private UserDto partyAgentToUserDto(PartyAgent partyAgent, String pwd){
        UserDto userDto = new UserDto();
        userDto.setEmail(partyAgent.getEmail());
        userDto.setFirstname(partyAgent.getFirstname());
        userDto.setLastname(partyAgent.getLastname());
        userDto.setPhone(partyAgent.getPhone());
        userDto.setPassword(pwd);
        userDto.setLgaId(partyAgent.getLga().getId().toString());
        userDto.setRole("agent");
        return userDto;
    }

    @Override
    public PartyAgentResponse findPartyAgentById(Long id) throws NotFoundException {
        PartyAgent partyAgent = getPartyAgent(id);
        return new PartyAgentResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), partyAgent);
    }

    @Override
    public PartyAgentResponse findPartyAgentByName(String firstname, String lastname) throws NotFoundException {
        List<PartyAgent> partyAgent = partyAgentRepository.findByFirstnameOrLastnameOrPhone(firstname, lastname, firstname);
        List<PartyAgentDto> partyAgentDtoList = new ArrayList<>();
        partyAgent.stream().forEach(agents->{
            PartyAgentDto partyAgentDto = new PartyAgentDto();
            partyAgentDto.setStateId(agents.getPollingUnit().getState().getId());
            partyAgentDto.setLgaName(agents.getLga().getName());
            partyAgentDto.setLgaId(agents.getPollingUnit().getLga().getCode());
            partyAgentDto.setPollingUnitId(agents.getPollingUnit().getCode());
            partyAgentDto.setPollingUnitName(agents.getPollingUnit().getName());
            partyAgentDto.setWardName(agents.getWard().getName());
            partyAgentDto.setWardId(agents.getPollingUnit().getWard().getCode());
            partyAgentDto.setAddress(agents.getAddress());
            partyAgentDto.setFirstname(agents.getFirstname());
            partyAgentDto.setLastname(agents.getLastname());
            partyAgentDto.setEmail(agents.getEmail());
            partyAgentDto.setPhone(agents.getPhone());
            partyAgentDto.setId(agents.getId());
            partyAgentDto.setRole(agents.getRole());
            partyAgentDtoList.add(partyAgentDto);
        });
        if(partyAgent==null){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return new PartyAgentResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), partyAgentDtoList);
    }

    @Override
    public PartyAgentResponse findPartyAgentByLga(Long lgaId) throws NotFoundException {
        Lga lga = new Lga(){{setId(lgaId);}};
        List<PartyAgent> partyAgent = partyAgentRepository.findByLga(lga);
        List<PartyAgentDto> partyAgentDtoList = new ArrayList<>();
        partyAgent.stream().forEach(agents->{
            PartyAgentDto partyAgentDto = new PartyAgentDto();
            partyAgentDto.setStateId(agents.getPollingUnit().getState().getId());
            partyAgentDto.setLgaName(agents.getLga().getName());
            partyAgentDto.setLgaId(agents.getPollingUnit().getLga().getCode());
            partyAgentDto.setPollingUnitId(agents.getPollingUnit().getCode());
            partyAgentDto.setPollingUnitName(agents.getPollingUnit().getName());
            partyAgentDto.setWardName(agents.getWard().getName());
            partyAgentDto.setWardId(agents.getPollingUnit().getWard().getCode());
            partyAgentDto.setAddress(agents.getAddress());
            partyAgentDto.setFirstname(agents.getFirstname());
            partyAgentDto.setLastname(agents.getLastname());
            partyAgentDto.setEmail(agents.getEmail());
            partyAgentDto.setPhone(agents.getPhone());
            partyAgentDto.setId(agents.getId());
            partyAgentDto.setRole(agents.getRole());
            partyAgentDtoList.add(partyAgentDto);
        });
        if(partyAgent==null){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return new PartyAgentResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), partyAgentDtoList);
    }

    @Override
    public PartyAgentResponse findPartyAgentByState(Long stateId) throws NotFoundException{
        List<PartyAgentDto> partyAgentDtoList = new ArrayList<>();
        State state = new State(){{setId(stateId);}};
        List<Lga> states = lgaRepository.findByState(state);
        states.stream()
                .forEach(lga -> {
                    List<PartyAgent> partyAgents = partyAgentRepository.findByLga(lga);
                    partyAgents.forEach(agents -> {
                        PartyAgentDto partyAgentDto = new PartyAgentDto();
                        partyAgentDto.setStateId(agents.getPollingUnit().getState().getId());
                        partyAgentDto.setLgaName(agents.getLga().getName());
                        partyAgentDto.setLgaId(agents.getPollingUnit().getLga().getCode());
                        partyAgentDto.setPollingUnitId(agents.getPollingUnit().getCode());
                        partyAgentDto.setPollingUnitName(agents.getPollingUnit().getName());
                        partyAgentDto.setWardName(agents.getWard().getName());
                        partyAgentDto.setWardId(agents.getPollingUnit().getWard().getCode());
                        partyAgentDto.setAddress(agents.getAddress());
                        partyAgentDto.setFirstname(agents.getFirstname());
                        partyAgentDto.setLastname(agents.getLastname());
                        partyAgentDto.setEmail(agents.getEmail());
                        partyAgentDto.setPhone(agents.getPhone());
                        partyAgentDto.setId(agents.getId());
                        partyAgentDto.setRole(agents.getRole());
                        partyAgentDtoList.add(partyAgentDto);
                    });

                });

        return new PartyAgentResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), partyAgentDtoList);
    }
    @Override
    public PartyAgentResponse findPartyAgentBySenatorialDistrict(Long senatorialDistrictId) throws NotFoundException {
        List<PartyAgentDto> partyAgentDtoList = new ArrayList<>();
        SenatorialDistrict senatorialDistrict = new SenatorialDistrict(){{setId(senatorialDistrictId);}};
        List<Lga> lgas = lgaRepository.findBySenatorialDistrict(senatorialDistrict);
        lgas.stream()
                .forEach(lga -> {
                    List<PartyAgent> partyAgents = partyAgentRepository.findByLga(lga);
                    partyAgents.forEach(agents -> {
                        PartyAgentDto partyAgentDto = new PartyAgentDto();
                        partyAgentDto.setStateId(agents.getPollingUnit().getState().getId());
                        partyAgentDto.setLgaName(agents.getLga().getName());
                        partyAgentDto.setLgaId(agents.getPollingUnit().getLga().getCode());
                        partyAgentDto.setPollingUnitId(agents.getPollingUnit().getCode());
                        partyAgentDto.setPollingUnitName(agents.getPollingUnit().getName());
                        partyAgentDto.setWardName(agents.getWard().getName());
                        partyAgentDto.setWardId(agents.getPollingUnit().getWard().getCode());
                        partyAgentDto.setAddress(agents.getAddress());
                        partyAgentDto.setFirstname(agents.getFirstname());
                        partyAgentDto.setLastname(agents.getLastname());
                        partyAgentDto.setEmail(agents.getEmail());
                        partyAgentDto.setPhone(agents.getPhone());
                        partyAgentDto.setId(agents.getId());
                        partyAgentDto.setRole(agents.getRole());
                        partyAgentDtoList.add(partyAgentDto);
                    });

                });

        return new PartyAgentResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), partyAgentDtoList);
    }

    @Override
    public PartyAgentResponse findPartyAgentByWard(Long wardId) throws NotFoundException {
        Ward ward = getWard(wardId);
        List<PartyAgent> partyAgent = partyAgentRepository.findByWard(ward);
        List<PartyAgentDto> partyAgentDtoList = new ArrayList<>();
        partyAgent.stream().forEach(agents->{
            PartyAgentDto partyAgentDto = new PartyAgentDto();
            partyAgentDto.setStateId(agents.getPollingUnit().getState().getId());
            partyAgentDto.setLgaName(agents.getLga().getName());
            partyAgentDto.setLgaId(agents.getPollingUnit().getLga().getCode());
            partyAgentDto.setPollingUnitId(agents.getPollingUnit().getCode());
            partyAgentDto.setPollingUnitName(agents.getPollingUnit().getName());
            partyAgentDto.setWardName(agents.getWard().getName());
            partyAgentDto.setWardId(agents.getPollingUnit().getWard().getCode());
            partyAgentDto.setAddress(agents.getAddress());
            partyAgentDto.setFirstname(agents.getFirstname());
            partyAgentDto.setLastname(agents.getLastname());
            partyAgentDto.setEmail(agents.getEmail());
            partyAgentDto.setPhone(agents.getPhone());
            partyAgentDto.setId(agents.getId());
            partyAgentDto.setRole(agents.getRole());
            partyAgentDtoList.add(partyAgentDto);
        });
        return new PartyAgentResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), partyAgentDtoList);
    }

    @Override
    public PartyAgentResponse findPartyAgentByPollingUnit(Long pollingUnitId) throws NotFoundException {
        PollingUnit pollingUnit = getPollingUnit(pollingUnitId);
        List<PartyAgent> partyAgent = partyAgentRepository.findByPollingUnit(pollingUnit);
        List<PartyAgentDto> partyAgentDtoList = new ArrayList<>();
        partyAgent.stream().forEach(agents->{
            PartyAgentDto partyAgentDto = new PartyAgentDto();
            partyAgentDto.setStateId(agents.getPollingUnit().getState().getId());
            partyAgentDto.setLgaName(agents.getLga().getName());
            partyAgentDto.setLgaId(agents.getPollingUnit().getLga().getCode());

            partyAgentDto.setPollingUnitId(agents.getPollingUnit().getCode());
            partyAgentDto.setPollingUnitName(agents.getPollingUnit().getName());
            partyAgentDto.setWardName(agents.getWard().getName());
            partyAgentDto.setWardId(agents.getPollingUnit().getWard().getCode());
            partyAgentDto.setAddress(agents.getAddress());
            partyAgentDto.setFirstname(agents.getFirstname());
            partyAgentDto.setLastname(agents.getLastname());
            partyAgentDto.setEmail(agents.getEmail());
            partyAgentDto.setPhone(agents.getPhone());
            partyAgentDto.setId(agents.getId());
            partyAgentDto.setRole(agents.getRole());
            partyAgentDtoList.add(partyAgentDto);
        });
        if(partyAgent==null){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return new PartyAgentResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), partyAgentDtoList);
    }

    @Override
    public PartyAgentResponse findPartyAgentByPhone(String phone) throws NotFoundException {
        PartyAgent partyAgent = partyAgentRepository.findByPhone(phone);
        if(partyAgent==null){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        PartyAgentDto partyAgentDto = new PartyAgentDto();
        partyAgentDto.setStateId(partyAgent.getPollingUnit().getState().getId());
        partyAgentDto.setLgaName(partyAgent.getLga().getName());
        partyAgentDto.setLgaId(partyAgent.getPollingUnit().getLga().getId()+"");
        partyAgentDto.setPollingUnitId(partyAgent.getPollingUnit().getId()+"");
        partyAgentDto.setPollingUnitName(partyAgent.getPollingUnit().getName());
        partyAgentDto.setWardName(partyAgent.getWard().getName());
        partyAgentDto.setWardId(partyAgent.getPollingUnit().getWard().getId()+"");
        partyAgentDto.setAddress(partyAgent.getAddress());
        partyAgentDto.setFirstname(partyAgent.getFirstname());
        partyAgentDto.setLastname(partyAgent.getLastname());
        partyAgentDto.setEmail(partyAgent.getEmail());
        partyAgentDto.setPhone(partyAgent.getPhone());
        partyAgentDto.setId(partyAgent.getId());
        partyAgentDto.setRole(partyAgent.getRole());

        return new PartyAgentResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), partyAgentDto);
    }

    @Override
    public PartyAgentResponse updatePartyAgent(Long id, PartyAgentDto partyAgentDto) throws NotFoundException {
        PollingUnit pollingUnit = getPollingUnit(Long.valueOf(partyAgentDto.getPollingUnitId()));
        Lga lga = pollingUnit.getLga();
        Ward ward = pollingUnit.getWard();
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
        partyAgent.setRole(partyAgentDto.getRole());
        partyAgentRepository.save(partyAgent);
        userService.saveAgent(partyAgentToUserDto(partyAgent, partyAgentDto.getPwd()));
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
        List<Lga> lgaList = lgaRepository.findByState(stateRepository.findByDefaultState(true));
        List<PartyAgentDto> partyAgentDtoList = new ArrayList<>();

        lgaList.stream().forEach(lga -> {
            List<PartyAgent> partyAgents = partyAgentRepository.findByLga(lga);
            partyAgents.stream().forEach(agents->{
                PartyAgentDto partyAgentDto = new PartyAgentDto();
                partyAgentDto.setStateId(agents.getPollingUnit().getState().getId());
                partyAgentDto.setLgaName(agents.getLga().getName());
                partyAgentDto.setLgaId(agents.getPollingUnit().getLga().getCode());
                partyAgentDto.setPollingUnitId(agents.getPollingUnit().getCode());
                partyAgentDto.setPollingUnitName(agents.getPollingUnit().getName());
                partyAgentDto.setWardName(agents.getWard().getName());
                partyAgentDto.setWardId(agents.getPollingUnit().getWard().getCode());
                partyAgentDto.setAddress(agents.getAddress());
                partyAgentDto.setFirstname(agents.getFirstname());
                partyAgentDto.setLastname(agents.getLastname());
                partyAgentDto.setEmail(agents.getEmail());
                partyAgentDto.setPhone(agents.getPhone());
                partyAgentDto.setId(agents.getId());
                partyAgentDto.setRole(agents.getRole());
                partyAgentDtoList.add(partyAgentDto);
            });
        });

        return new PartyAgentResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), partyAgentDtoList);
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

    private Ward getWard(Long id) throws NotFoundException {
        Optional<Ward> currentWard = wardRepository.findById(id);
        if(!currentWard.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return currentWard.get();
    }

    private void savePartyAgent(String surname,
                                String firstName,
                                String phoneNumber,
                                String pollingUnitCode,
                                String address,
                                String password,
                                String role)  {

        PollingUnit pollingUnit = pollingUnitRepository.findById(Long.valueOf(pollingUnitCode)).get();
        PartyAgent partyAgent = partyAgentRepository.findByPhone(phoneNumber);

        try{
            if(partyAgent==null){
                partyAgent = new PartyAgent();
                partyAgent.setFirstname(firstName);
                partyAgent.setLastname(surname);
                partyAgent.setAddress(address);
                partyAgent.setPhone(phoneNumber);
                partyAgent.setPollingUnit(pollingUnit);
                partyAgent.setWard(pollingUnit.getWard());
                partyAgent.setLga(pollingUnit.getLga());
                partyAgent.setRole(role);
                partyAgentRepository.save(partyAgent);

                userService.saveAgent(partyAgentToUserDto(partyAgent, password));
            }
        }
        catch (Exception ex){
            LOGGER.info("Party agent could not be saved");
        }
    }

    @Override
    public PartyAgentResponse uploadPartyAgent(MultipartFile file){
        List<String> csvLines = FileUtil.getCsvLines(file, fileProcessingService.getFileStorageLocation());
        return processUpload(csvLines);
    }


    private PartyAgentResponse processUpload(List<String> lines){
        for (String line:lines) {
            String[] state = line.split(",");
            savePartyAgent(state[0].trim(), state[1].trim(), state[2].trim(), state[3].trim(),state[4].trim(),state[5].trim(),state[6].trim());
        }
        return new PartyAgentResponse("00", "File Uploaded.");
    }
}
