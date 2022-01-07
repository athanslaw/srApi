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
import org.springframework.data.domain.Sort;
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
    private final ResultRealTimeRepository resultRealTimeRepository;
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
    public ResultServiceImpl(ResultRepository resultRepository, ResultRealTimeRepository resultRealTimeRepository, PartyAgentRepository partyAgentRepository, ElectionRepository electionRepository, SenatorialDistrictRepository senatorialDistrictRepository, WardRepository wardRepository, LgaRepository lgaRepository, PollingUnitRepository pollingUnitRepository, VotingLevelRepository votingLevelRepository, ResultPerPartyRepository resultPerPartyRepository, PoliticalPartyRepository politicalPartyRepository, FileConfigurationProperties fileConfigurationProperties) {
        this.resultRepository = resultRepository;
        this.resultRealTimeRepository = resultRealTimeRepository;
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
        Optional<PartyAgent> partyAgent = partyAgentRepository.findById(resultDto.getPartyAgentId());
        SenatorialDistrict senatorialDistrict = getSenatorialDistrict(resultDto.getSenatorialDistrictId());


        PollingUnit pollingUnit = getPollingUnit(resultDto.getPollingUnitId());
        Election election = getElection();
        VotingLevel votingLevel = getVotingLevel(resultDto.getVotingLevelId());
        Ward ward = getWard(resultDto.getWardId());
        Result result = resultRepository.findByElectionAndWardAndPollingUnit(election,ward,pollingUnit);
        //, resultDto.getWardId(), resultDto.getPollingUnitId());
        Lga lga = getLga(resultDto.getLgaId());
        int pollingUnitCount = 1;
        if(result==null){
            result = new Result();
            //Delete existing result if the voting level is either LGA or Ward level.
            if(votingLevel.getCode().equals("Ward")){
                result.setWard(ward);
                resultRealTimeRepository.deleteByWard(ward);
                System.out.println("Deleting existing results with ward {} "+ ward.getCode());

                pollingUnitCount = pollingUnitRepository.findByWard(ward).size();
            }
            if(votingLevel.getCode().equals("LGA")){
                result.setLga(lga);
                resultRealTimeRepository.deleteByLga(lga);
                System.out.println("Deleting existing results with LGA {} "+ lga.getCode());
                pollingUnitCount = pollingUnitRepository.findByLga(lga).size();
            }

            if(votingLevel.getCode().equals("PU")){
                result.setPollingUnit(pollingUnit);
            }

            result.setSenatorialDistrict(senatorialDistrict);
            if(partyAgent.isPresent()) result.setPartyAgent(partyAgent.get());
            result.setElection(election);
            result.setWard(ward);
            result.setLga(lga);
            result.setPollingUnit(pollingUnit);
            result.setVotingLevel(votingLevel);
            result.setAccreditedVotersCount(resultDto.getAccreditedVotersCount());
            result.setRegisteredVotersCount(resultDto.getRegisteredVotersCount());
            result.setPollingUnitCount(pollingUnitCount);

            LOGGER.info("Saving result at voting level {} ", votingLevel.getCode());
            resultRepository.save(result);
            resultRealTimeRepository.save(result);

            //@Todo Remove party code manual update. There's an API to save result per party.
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
            resultPerPartyPdp.setVoteCount(resultDto.getPdp());
            resultPerPartyPdp.setResult(result);
            resultPerPartyPdp.setPoliticalParty(pdp);
            resultPerPartyRepository.save(resultPerPartyPdp);

            //Save APC votes;
            PoliticalParty anpp = politicalPartyRepository.findByCode("ANPP");
            ResultPerParty resultPerPartyAnpp = new ResultPerParty();
            resultPerPartyAnpp.setVoteCount(resultDto.getAnpp());
            resultPerPartyAnpp.setResult(result);
            resultPerPartyAnpp.setPoliticalParty(anpp);
            resultPerPartyRepository.save(resultPerPartyAnpp);

            //Save APC votes;
            PoliticalParty others = politicalPartyRepository.findByCode("Others");
            ResultPerParty resultPerPartyOthers = new ResultPerParty();
            resultPerPartyOthers.setVoteCount(resultDto.getOthers());
            resultPerPartyOthers.setResult(result);
            resultPerPartyOthers.setPoliticalParty(others);
            resultPerPartyRepository.save(resultPerPartyOthers);

            return new ResultResponse("00", String.format(successTemplate,SERVICE_NAME));
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
        Optional<PartyAgent> partyAgent = partyAgentRepository.findById(resultDto.getPartyAgentId());
        SenatorialDistrict senatorialDistrict = getSenatorialDistrict(resultDto.getSenatorialDistrictId());
        Lga lga = getLga(resultDto.getLgaId());
        PollingUnit pollingUnit = getPollingUnit(resultDto.getPollingUnitId());
        Election election = getElection(resultDto.getElectionId());
        VotingLevel votingLevel = getVotingLevel(resultDto.getVotingLevelId());
        Ward ward = getWard(resultDto.getWardId());
        result.setSenatorialDistrict(senatorialDistrict);
        result.setLga(lga);
        result.setWard(ward);
        partyAgent.ifPresent(result::setPartyAgent);
        result.setPollingUnit(pollingUnit);
        result.setElection(election);
        result.setVotingLevel(votingLevel);
        result.setLga(lga);
        result.setAccreditedVotersCount(resultDto.getAccreditedVotersCount());
        result.setRegisteredVotersCount(resultDto.getRegisteredVotersCount());
        resultRepository.save(result);


        PoliticalParty apc = politicalPartyRepository.findByCode("APC");
        ResultPerParty resultPerParty = resultPerPartyRepository.findByResultAndPoliticalParty(result,apc);
        resultPerParty.setVoteCount(resultDto.getApc());
        resultPerParty.setResult(result);
        resultPerParty.setPoliticalParty(apc);
        resultPerPartyRepository.save(resultPerParty);

        //Save APC votes;
        PoliticalParty pdp = politicalPartyRepository.findByCode("PDP");
        ResultPerParty resultPerPartyPdp = resultPerPartyRepository.findByResultAndPoliticalParty(result,pdp);
        resultPerPartyPdp.setVoteCount(resultDto.getPdp());
        resultPerPartyPdp.setResult(result);
        resultPerPartyPdp.setPoliticalParty(pdp);
        resultPerPartyRepository.save(resultPerPartyPdp);

        //Save APC votes;
        PoliticalParty anpp = politicalPartyRepository.findByCode("ANPP");
        ResultPerParty resultPerPartyAnpp = resultPerPartyRepository.findByResultAndPoliticalParty(result,anpp);
        resultPerPartyAnpp.setVoteCount(resultDto.getAnpp());
        resultPerPartyAnpp.setResult(result);
        resultPerPartyAnpp.setPoliticalParty(anpp);
        resultPerPartyRepository.save(resultPerPartyAnpp);

        //Save APC votes;
        PoliticalParty others = politicalPartyRepository.findByCode("Others");
        ResultPerParty resultPerPartyOthers = resultPerPartyRepository.findByResultAndPoliticalParty(result,others);
        resultPerPartyOthers.setVoteCount(resultDto.getOthers());
        resultPerPartyOthers.setResult(result);
        resultPerPartyOthers.setPoliticalParty(others);
        resultPerPartyRepository.save(resultPerPartyOthers);

        return new ResultResponse("00", String.format(successTemplate,SERVICE_NAME));
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


    private Election getElection() throws NotFoundException {
        Election election = electionRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).get(0);
        if(election == null){
            throw new NotFoundException(String.format(notFoundTemplate,"Election"));
        }
        return election;
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

    @Override
    public ResultResponse filterByLga(Long lgaId) throws NotFoundException {
        Lga lga = getLga(lgaId);
        List<Result> results = resultRepository.findByLga(lga);
        return new ResultResponse("00", "Results fetched",results);
    }

    @Override
    public ResultResponse filterByWard(Long wardId) throws NotFoundException {
        Ward ward = getWard(wardId);
        List<Result> results = resultRepository.findByWard(ward);
        return new ResultResponse("00", "Results fetched",results);
    }

    @Override
    public ResultResponse filterByPollingUnit(Long pollingUnitId) throws NotFoundException {
        PollingUnit pollingUnit = getPollingUnit(pollingUnitId);
        List<Result> results = resultRepository.findByPollingUnit(pollingUnit);
        return new ResultResponse("00", "Results fetched",results);
    }

    private void saveResult(String electionCode,
                            String votingLevelCode,
                            String phoneNumber,
                            String lgaCode,
                            String wardCode,
                            String pollingUnitCode,
                            String senatorialDistrictCode,
                            String accreditedVotersCount,
                            String registeredVotersCount,
                            String apcVotes,
                            String pdpVotes,
                            String anppVotes,
                            String othersVotes) {
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
                //Delete existing result if the voting level is either LGA or Ward level.
                if(votingLevel.getCode().equals("Ward")){

                    result.setWard(ward);
                    resultRepository.deleteByWard(ward);
                    LOGGER.info("Deleting existing results with ward {} ", ward.getCode());
                }
                if(votingLevel.getCode().equals("LGA")){
                    result.setLga(lga);
                    resultRepository.deleteByLga(lga);
                    LOGGER.info("Deleting existing results with LGA {} ", lga.getCode());
                }

                if(votingLevel.getCode().equals("PU")){
                    result.setPollingUnit(pollingUnit);
                }
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
                //Save APC votes;
                PoliticalParty apc = politicalPartyRepository.findByCode("APC");
                ResultPerParty resultPerParty = new ResultPerParty();
                resultPerParty.setVoteCount(Integer.valueOf(apcVotes));
                resultPerParty.setResult(result);
                resultPerParty.setPoliticalParty(apc);
                resultPerPartyRepository.save(resultPerParty);

                //Save PDP votes;
                PoliticalParty pdp = politicalPartyRepository.findByCode("PDP");
                ResultPerParty resultPerPartyPdp = new ResultPerParty();
                resultPerPartyPdp.setVoteCount(Integer.valueOf(pdpVotes));
                resultPerPartyPdp.setResult(result);
                resultPerPartyPdp.setPoliticalParty(pdp);
                resultPerPartyRepository.save(resultPerPartyPdp);

                //Save ANPP votes;
                PoliticalParty apga = politicalPartyRepository.findByCode("APGA");
                //PoliticalParty anpp = politicalPartyRepository.findByCode("ANPP");
                ResultPerParty resultPerPartyApga = new ResultPerParty();
                resultPerPartyApga.setVoteCount(Integer.valueOf(anppVotes));
                resultPerPartyApga.setResult(result);
                resultPerPartyApga.setPoliticalParty(apga);
                resultPerPartyRepository.save(resultPerPartyApga);

                //Save Others votes;
                PoliticalParty others = politicalPartyRepository.findByCode("Others");
                ResultPerParty resultPerPartyOthers = new ResultPerParty();
                resultPerPartyOthers.setVoteCount(Integer.valueOf(othersVotes));
                resultPerPartyOthers.setResult(result);
                resultPerPartyOthers.setPoliticalParty(others);
                resultPerPartyRepository.save(resultPerPartyOthers);
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
            saveResult(state[0].trim(), state[1].trim(), state[2].trim(),state[3].trim(), state[4].trim(), state[5].trim(),state[6].trim(), state[7].trim(), state[8].trim(), state[9].trim(), state[10].trim(), state[11].trim(), state[12].trim());
        }
        return new ResultResponse("00", "File Uploaded.");
    }
}
