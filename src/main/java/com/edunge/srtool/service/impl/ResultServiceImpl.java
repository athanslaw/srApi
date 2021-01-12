package com.edunge.srtool.service.impl;

import com.edunge.srtool.config.FileConfigurationProperties;
import com.edunge.srtool.dto.ResultDto;
import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.FileNotFoundException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.*;
import com.edunge.srtool.repository.*;
import com.edunge.srtool.response.ResultResponse;
import com.edunge.srtool.service.ResultService;
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
public class ResultServiceImpl implements ResultService {
    private final ResultRepository resultRepository;
    private final PartyAgentRepository partyAgentRepository;
    private final ElectionRepository electionRepository;
    private final SenatorialDistrictRepository senatorialDistrictRepository;
    private final WardRepository wardRepository;
    private final LgaRepository lgaRepository;
    private final PollingUnitRepository pollingUnitRepository;
    private final VotingLevelRepository votingLevelRepository;
    private final ResultPerPartyRepository resultPerPartyRepository;
    private final PoliticalPartyRepository politicalPartyRepository;
    private final Path fileStorageLocation;
    private static final Logger LOGGER = LoggerFactory.getLogger(ResultServiceImpl.class);

    private static final String SERVICE_NAME = "Result";

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
    public ResultServiceImpl(ResultRepository resultRepository, PartyAgentRepository partyAgentRepository, ElectionRepository electionRepository, SenatorialDistrictRepository senatorialDistrictRepository, WardRepository wardRepository, LgaRepository lgaRepository, PollingUnitRepository pollingUnitRepository, VotingLevelRepository votingLevelRepository, ResultPerPartyRepository resultPerPartyRepository, PoliticalPartyRepository politicalPartyRepository, FileConfigurationProperties fileConfigurationProperties) {
        this.resultRepository = resultRepository;
        this.partyAgentRepository = partyAgentRepository;
        this.electionRepository = electionRepository;
        this.senatorialDistrictRepository = senatorialDistrictRepository;
        this.wardRepository = wardRepository;
        this.lgaRepository = lgaRepository;
        this.pollingUnitRepository = pollingUnitRepository;
        this.votingLevelRepository = votingLevelRepository;
        this.resultPerPartyRepository = resultPerPartyRepository;
        this.politicalPartyRepository = politicalPartyRepository;
        this.fileStorageLocation = Paths.get(fileConfigurationProperties.getSvgDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileNotFoundException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public ResultResponse saveResult(ResultDto resultDto) throws NotFoundException {
        PartyAgent partyAgent = getPartyAgent(resultDto.getPartyAgentId());
        SenatorialDistrict senatorialDistrict = getSenatorialDistrict(resultDto.getSenatorialDistrictId());
        Lga lga = getLga(resultDto.getLgaId());
        PollingUnit pollingUnit = getPollingUnit(resultDto.getPollingUnitId());
        Election election = getElection(resultDto.getElectionId());
        VotingLevel votingLevel = getVotingLevel(resultDto.getVotingLevelId());
        Ward ward = getWard(resultDto.getWardId());
        Result result = resultRepository.findByElectionAndWardAndPollingUnit(election,ward,pollingUnit);
        //, resultDto.getWardId(), resultDto.getPollingUnitId());
        if(result==null){

            result = new Result();
            result.setSenatorialDistrict(senatorialDistrict);
            result.setLga(lga);
            result.setWard(ward);
            result.setPartyAgent(partyAgent);
            result.setPollingUnit(pollingUnit);
            result.setElection(election);
            result.setVotingLevel(votingLevel);
            result.setLga(lga);
            result.setAccreditedVotersCount(resultDto.getAccreditedVotersCount());
            result.setRegisteredVotersCount(resultDto.getRegisteredVotersCount());
            resultRepository.save(result);

            //Save APC votes;
            PoliticalParty apc = politicalPartyRepository.findByCode("APC");
            ResultPerParty resultPerParty = new ResultPerParty();
            resultPerParty.setVoteCount(resultDto.getApc());
            resultPerParty.setResult(result);
            resultPerParty.setPoliticalParty(apc);
            resultPerPartyRepository.save(resultPerParty);

            //Save APC votes;
            PoliticalParty pdp = politicalPartyRepository.findByCode("PDP");
            ResultPerParty resultPerPartyPdp = new ResultPerParty();
            resultPerPartyPdp.setVoteCount(resultDto.getApc());
            resultPerPartyPdp.setResult(result);
            resultPerPartyPdp.setPoliticalParty(pdp);
            resultPerPartyRepository.save(resultPerPartyPdp);

            //Save APC votes;
            PoliticalParty anpp = politicalPartyRepository.findByCode("ANPP");
            ResultPerParty resultPerPartyAnpp = new ResultPerParty();
            resultPerPartyAnpp.setVoteCount(resultDto.getApc());
            resultPerPartyAnpp.setResult(result);
            resultPerPartyAnpp.setPoliticalParty(anpp);
            resultPerPartyRepository.save(resultPerPartyAnpp);

            //Save APC votes;
            PoliticalParty others = politicalPartyRepository.findByCode("Others");
            ResultPerParty resultPerPartyOthers = new ResultPerParty();
            resultPerPartyOthers.setVoteCount(resultDto.getApc());
            resultPerPartyOthers.setResult(result);
            resultPerPartyOthers.setPoliticalParty(others);
            resultPerPartyRepository.save(resultPerPartyOthers);

            return new ResultResponse("00", String.format(successTemplate,SERVICE_NAME), result);
        }
        throw new DuplicateException(String.format("Result for %s in %s already exists.", ward.getName(), election.getDescription()));
    }

    @Override
    public ResultResponse findResultById(Long id) throws NotFoundException {
        Result result = getResult(id);
        return new ResultResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), result);
    }


    @Override
    public ResultResponse updateResult(Long id, ResultDto resultDto) throws NotFoundException {
        Result result = getResult(id);
        PartyAgent partyAgent = getPartyAgent(resultDto.getPartyAgentId());
        SenatorialDistrict senatorialDistrict = getSenatorialDistrict(resultDto.getSenatorialDistrictId());
        Lga lga = getLga(resultDto.getLgaId());
        PollingUnit pollingUnit = getPollingUnit(resultDto.getPollingUnitId());
        Election election = getElection(resultDto.getElectionId());
        VotingLevel votingLevel = getVotingLevel(resultDto.getVotingLevelId());
        Ward ward = getWard(resultDto.getWardId());
        result.setSenatorialDistrict(senatorialDistrict);
        result.setLga(lga);
        result.setWard(ward);
        result.setPartyAgent(partyAgent);
        result.setPollingUnit(pollingUnit);
        result.setElection(election);
        result.setVotingLevel(votingLevel);
        result.setLga(lga);
        result.setAccreditedVotersCount(resultDto.getAccreditedVotersCount());
        result.setRegisteredVotersCount(resultDto.getRegisteredVotersCount());
        resultRepository.save(result);
        return new ResultResponse("00", String.format(successTemplate,SERVICE_NAME), result);
    }

    @Override
    public ResultResponse deleteResultById(Long id) throws NotFoundException {
        Result result = getResult(id);
        resultRepository.deleteById(id);
        return new ResultResponse("00",String.format(deleteTemplate,SERVICE_NAME));
    }

    @Override
    public ResultResponse findAll() {
        List<Result> elections = resultRepository.findAll();
        return new ResultResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), elections);
    }


    private Election getElection(Long id) throws NotFoundException {
        Optional<Election> election = electionRepository.findById(id);
        if(!election.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,"Election"));
        }
        return election.get();
    }

    private VotingLevel getVotingLevel(Long id) throws NotFoundException {
        Optional<VotingLevel> votingLevel = votingLevelRepository.findById(id);
        if(!votingLevel.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,"Voting Level"));
        }
        return votingLevel.get();
    }

    private Lga getLga(Long id) throws NotFoundException {
        Optional<Lga> lga = lgaRepository.findById(id);
        if(!lga.isPresent()){
            throw new NotFoundException("State not found.");
        }
        return lga.get();
    }

    private SenatorialDistrict getSenatorialDistrict(Long id) throws NotFoundException {
        Optional<SenatorialDistrict> senatorialDistrict = senatorialDistrictRepository.findById(id);
        if(!senatorialDistrict.isPresent()){
            throw new NotFoundException("Senatorial District not found.");
        }
        return senatorialDistrict.get();
    }

    private Ward getWard(Long id) throws NotFoundException {
        Optional<Ward> currentWard = wardRepository.findById(id);
        if(!currentWard.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,"Ward"));
        }
        return currentWard.get();
    }

    private PartyAgent getPartyAgent(Long id) throws NotFoundException {
        Optional<PartyAgent> partyAgent = partyAgentRepository.findById(id);
        if(!partyAgent.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,"Party Agent"));
        }
        return partyAgent.get();
    }

    private PollingUnit getPollingUnit(Long id) throws NotFoundException {
        Optional<PollingUnit> currentPollingUnit = pollingUnitRepository.findById(id);
        if(!currentPollingUnit.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,"Polling Unit"));
        }
        return currentPollingUnit.get();
    }

    private Result getResult(Long id) throws NotFoundException {
        Optional<Result> result = resultRepository.findById(id);
        if(!result.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return result.get();
    }

    private void saveResult(String electionCode,
                            String votingLevelCode,
                            String phoneNumber,
                            String lgaCode,
                            String wardCode,
                            String pollingUnitCode,
                            String senatorialDistrictCode,
                            String accreditedVotersCount, String registeredVotersCount) {
        try{
            Lga lga = lgaRepository.findByCode(lgaCode);
            PartyAgent partyAgent = partyAgentRepository.findByPhone(phoneNumber);
            Ward ward = wardRepository.findByCode(wardCode);
            PollingUnit pollingUnit = pollingUnitRepository.findByCode(pollingUnitCode);
            VotingLevel votingLevel = votingLevelRepository.findByCode(votingLevelCode);
            SenatorialDistrict senatorialDistrict = senatorialDistrictRepository.findByCode(senatorialDistrictCode);
            Election election = electionRepository.findByCode(electionCode);
            Result result = resultRepository.findByElectionAndWardAndPollingUnit(election,ward,pollingUnit);

            if(result==null){
                result = new Result();
                result.setRegisteredVotersCount(Integer.valueOf(registeredVotersCount));
                result.setAccreditedVotersCount(Integer.valueOf(accreditedVotersCount));
                result.setElection(election);
                result.setWard(ward);
                result.setLga(lga);
                result.setPollingUnit(pollingUnit);
                result.setPartyAgent(partyAgent);
                result.setVotingLevel(votingLevel);
                result.setSenatorialDistrict(senatorialDistrict);
                resultRepository.save(result);
            }
        }
        catch (Exception ex){
            LOGGER.info(String.format("%s result could not be saved",lgaCode));
        }
    }

    @Override
    public ResultResponse uploadResult(MultipartFile file){
        List<String> csvLines = FileUtil.getCsvLines(file, this.fileStorageLocation);
        return processUpload(csvLines);
    }


    private ResultResponse processUpload(List<String> lines){
        for (String line:lines) {
            String[] state = line.split(",");
            saveResult(state[0].trim(), state[1].trim(), state[2].trim(),state[3].trim(), state[4].trim(), state[5].trim(),state[6].trim(), state[7].trim(), state[8].trim());
        }
        return new ResultResponse("00", "File Uploaded.");
    }
}
