package com.edunge.srtool.service.impl;

import com.edunge.srtool.dto.ResultDto;
import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.*;
import com.edunge.srtool.repository.*;
import com.edunge.srtool.response.ResultResponse;
import com.edunge.srtool.service.FileProcessingService;
import com.edunge.srtool.service.ResultService;
import com.edunge.srtool.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class ResultServiceImpl implements ResultService {
    private final ResultRepository resultRepository;
    private final ResultRealTimeRepository resultRealTimeRepository;
    private final PartyAgentServiceImpl partyAgentService;
    private final ElectionRepository electionRepository;
    private final SenatorialDistrictServiceImpl senatorialDistrictService;
    private final WardServiceImpl wardService;
    private final LgaServiceImpl lgaService;
    private final PollingUnitServiceImpl pollingUnitService;
    private final VotingLevelRepository votingLevelRepository;
    private final ResultPerPartyRepository resultPerPartyRepository;
    private final PoliticalPartyServiceImpl politicalPartyService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ResultServiceImpl.class);

    private static final String SERVICE_NAME = "Result";
    private static final String VOTING_LEVEL_POLLING_UNIT = "PollingUnit";
    private static final String VOTING_LEVEL_LGA = "LGA";
    private static final String VOTING_LEVEL_WARD = "Ward";

    private static final String FIRST_PARTY = "party_1";
    private static final String SECOND_PARTY = "party_2";
    private static final String THIRD_PARTY = "party_3";
    private static final String FOURTH_PARTY = "party_4";
    private static final String FIFTH_PARTY = "party_5";
    private static final String SIXTH_PARTY = "party_6";


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
    FileProcessingService fileProcessingService;

    @Autowired
    public ResultServiceImpl(ResultRepository resultRepository, ResultRealTimeRepository resultRealTimeRepository, PartyAgentServiceImpl partyAgentService, ElectionRepository electionRepository, SenatorialDistrictServiceImpl senatorialDistrictService,
            WardServiceImpl wardService, LgaServiceImpl lgaService, PollingUnitServiceImpl pollingUnitService, VotingLevelRepository votingLevelRepository, ResultPerPartyRepository resultPerPartyRepository, PoliticalPartyServiceImpl politicalPartyService) {
        this.resultRepository = resultRepository;
        this.resultRealTimeRepository = resultRealTimeRepository;
        this.partyAgentService = partyAgentService;
        this.electionRepository = electionRepository;
        this.senatorialDistrictService = senatorialDistrictService;
        this.wardService = wardService;
        this.lgaService = lgaService;
        this.pollingUnitService = pollingUnitService;
        this.votingLevelRepository = votingLevelRepository;
        this.resultPerPartyRepository = resultPerPartyRepository;
        this.politicalPartyService = politicalPartyService;
    }

    @Override
    public ResultResponse saveResult(ResultDto resultDto) throws NotFoundException {
        PartyAgent partyAgent = partyAgentService.findPartyAgentById(resultDto.getPartyAgentId()).getPartyAgent();
        SenatorialDistrict senatorialDistrict = getSenatorialDistrict(resultDto.getSenatorialDistrictId());

        Election election = getElection();
        VotingLevel votingLevel = getVotingLevel(resultDto.getVotingLevelId());
        PollingUnit pollingUnit = getPollingUnit(resultDto.getPollingUnitId());
        Ward ward = getWard(resultDto.getWardId());
        State state = senatorialDistrict.getState();
        Lga lga = getLga(resultDto.getLgaId());
        int pollingUnitCount = 1;
        boolean checkingRealTime = checkForDuplicate(election, votingLevel, lga, ward, resultDto.getElectionType());

        Result result = new Result();
        ResultRealTime resultRealTime = new ResultRealTime();

        //Delete existing result if the voting level is either LGA or Ward level.
        if(votingLevel.getCode().equals(VOTING_LEVEL_WARD)){
            result = resultRepository.findByElectionAndWardAndVotingLevelAndElectionType(election, ward, votingLevel, resultDto.getElectionType());
            if(result != null){
                throw new DuplicateException(String.format("Result for %s in %s already exists.", ward.getName(), election.getDescription()));
            }
            result = new Result();
            if(checkingRealTime) resultRealTimeRepository.deleteByWard(ward);
            pollingUnitCount = (int)pollingUnitService.findCountByWard(ward.getId());
        }
        else if(votingLevel.getCode().equals(VOTING_LEVEL_LGA)){
            List<ResultRealTime> resultRealTimeList = resultRealTimeRepository.findByElectionAndLgaAndVotingLevelAndElectionType(election, lga, votingLevel, resultDto.getElectionType());
            if(resultRealTimeList.size() >0){
                throw new DuplicateException(String.format("Result for %s in %s already exists.", lga.getName(), election.getDescription()));
            }
            result = new Result();
            if(checkingRealTime) resultRealTimeRepository.deleteByLga(lga);
            pollingUnitCount = (int)pollingUnitService.findCountByLga(lga.getId());
        }

        else if(votingLevel.getCode().equals(VOTING_LEVEL_POLLING_UNIT)){
            result = resultRepository.findByElectionAndPollingUnitAndElectionType(election, pollingUnit, resultDto.getElectionType());
            if(result != null){
                throw new DuplicateException(String.format("Result for %s in %s already exists.", pollingUnit.getName(), election.getDescription()));
            }
            result = new Result();
        }

        result.setGeoPoliticalZoneId(state.getGeoPoliticalZone().getId());
        result.setSenatorialDistrict(senatorialDistrict);
        resultRealTime.setGeoPoliticalZoneId(state.getGeoPoliticalZone().getId());
        resultRealTime.setSenatorialDistrict(senatorialDistrict);
        result.setStateId(state.getId());
        resultRealTime.setStateId(state.getId());
        result.setElectionType(resultDto.getElectionType());
        resultRealTime.setElectionType(resultDto.getElectionType());
        result.setPartyAgent(partyAgent);
        resultRealTime.setPartyAgent(partyAgent);
        result.setElection(election);
        resultRealTime.setElection(election);
        resultRealTime.setWard(ward);
        result.setWard(ward);
        result.setLga(lga);
        resultRealTime.setLga(lga);
        result.setPollingUnit(pollingUnit);
        resultRealTime.setPollingUnit(pollingUnit);
        result.setVotingLevel(votingLevel);
        resultRealTime.setVotingLevel(votingLevel);
        result.setAccreditedVotersCount(resultDto.getAccreditedVotersCount());
        resultRealTime.setAccreditedVotersCount(resultDto.getAccreditedVotersCount());
        result.setRegisteredVotersCount(resultDto.getRegisteredVotersCount());
        resultRealTime.setRegisteredVotersCount(resultDto.getRegisteredVotersCount());
        result.setPollingUnitCount(pollingUnitCount);
        resultRealTime.setPollingUnitCount(pollingUnitCount);

        resultRealTime.setParty_1(resultDto.getParty_1());
        resultRealTime.setParty_2(resultDto.getParty_2());
        resultRealTime.setParty_3(resultDto.getParty_3());
        resultRealTime.setParty_4(resultDto.getParty_4());
        resultRealTime.setParty_5(resultDto.getParty_5());
        resultRealTime.setParty_6(resultDto.getParty_6());

        LOGGER.info("Saving result at voting level {} ", votingLevel.getCode());
        Result r = resultRepository.save(result);
        // check if a higher level already exists before committing
        if(checkingRealTime) {
            resultRealTime.setResult(r.getId());
            resultRealTime.setVoteCount(resultDto.getParty_1()+resultDto.getParty_2()+resultDto.getParty_3()+resultDto.getParty_4()+resultDto.getParty_5()+resultDto.getParty_6());
            resultRealTimeRepository.save(resultRealTime);
        }

        saveResultPerParty(resultDto, state, result);

        return new ResultResponse("00", String.format(successTemplate,SERVICE_NAME));

    }

    private void saveResultPerParty(ResultDto resultDto, State state, Result result) {
        PoliticalParty first = politicalPartyService.findPoliticalPartyByCodeAndDefaultState(FIRST_PARTY, state).getPoliticalParty();
        ResultPerParty resultPerPartyFirst = new ResultPerParty();
        resultPerPartyFirst.setVoteCount(resultDto.getParty_1());
        resultPerPartyFirst.setResult(result);
        resultPerPartyFirst.setPoliticalParty(first);
        resultPerPartyRepository.save(resultPerPartyFirst);

        PoliticalParty second = politicalPartyService.findPoliticalPartyByCodeAndDefaultState(SECOND_PARTY, state).getPoliticalParty();
        ResultPerParty resultPerPartySecond = new ResultPerParty();
        resultPerPartySecond.setVoteCount(resultDto.getParty_2());
        resultPerPartySecond.setResult(result);
        resultPerPartySecond.setPoliticalParty(second);
        resultPerPartyRepository.save(resultPerPartySecond);

        //Save ANPP votes;
        PoliticalParty third = politicalPartyService.findPoliticalPartyByCodeAndDefaultState(THIRD_PARTY, state).getPoliticalParty();
        ResultPerParty resultPerPartyThird = new ResultPerParty();
        resultPerPartyThird.setVoteCount(resultDto.getParty_3());
        resultPerPartyThird.setResult(result);
        resultPerPartyThird.setPoliticalParty(third);
        resultPerPartyRepository.save(resultPerPartyThird);

        //Save FOURTH votes;
        PoliticalParty fourth = politicalPartyService.findPoliticalPartyByCodeAndDefaultState(FOURTH_PARTY, state).getPoliticalParty();
        ResultPerParty resultPerPartyFourth = new ResultPerParty();
        resultPerPartyFourth.setVoteCount(resultDto.getParty_4());
        resultPerPartyFourth.setResult(result);
        resultPerPartyFourth.setPoliticalParty(fourth);
        resultPerPartyRepository.save(resultPerPartyFourth);

        //Save FIFTH votes;
        PoliticalParty fifth = politicalPartyService.findPoliticalPartyByCodeAndDefaultState(FIFTH_PARTY, state).getPoliticalParty();
        ResultPerParty resultPerPartyFifth = new ResultPerParty();
        resultPerPartyFifth.setVoteCount(resultDto.getParty_5());
        resultPerPartyFifth.setResult(result);
        resultPerPartyFifth.setPoliticalParty(fifth);
        resultPerPartyRepository.save(resultPerPartyFifth);

        //Save SIXTH votes;
        PoliticalParty sixth = politicalPartyService.findPoliticalPartyByCodeAndDefaultState(SIXTH_PARTY, state).getPoliticalParty();
        ResultPerParty resultPerPartySixth = new ResultPerParty();
        resultPerPartySixth.setVoteCount(resultDto.getParty_6());
        resultPerPartySixth.setResult(result);
        resultPerPartySixth.setPoliticalParty(sixth);

        resultPerPartyRepository.save(resultPerPartySixth);
    }

    private boolean checkForDuplicate(Election election, VotingLevel votingLevel, Lga lga, Ward ward, Long electionType) throws NotFoundException {
        if(votingLevel.getCode().equals(VOTING_LEVEL_LGA)) {
            return !(resultRealTimeRepository.findByElectionAndLgaAndVotingLevelAndElectionType(election, lga, votingLevel, electionType).size() > 0);
            // same ward
        }
        VotingLevel votingLevel1 = getVotingLevel(0L);
        if(votingLevel.getCode().equals(VOTING_LEVEL_WARD)) {
            return !(resultRealTimeRepository.findByElectionAndLgaAndVotingLevelAndElectionType(election, lga, votingLevel1, electionType).size() > 0);
        }
        else {
            if(resultRealTimeRepository.findByElectionAndLgaAndVotingLevelAndElectionType(election, lga, votingLevel1, electionType).size() >0){
                return false;
            }
            return !(resultRealTimeRepository.findByElectionAndWardAndVotingLevelLessThanAndElectionType(election, ward, votingLevel, electionType).size()>0);
        }
    }

    @Override
    public ResultResponse findResultById(Long id) throws NotFoundException {
        Result result = getResult(id);
        return new ResultResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), result);
    }

    @Override
    public ResultResponse updateResult(Long id, ResultDto resultDto) throws NotFoundException {
        Result result = getResult(id);
        State state = result.getSenatorialDistrict().getState();
        VotingLevel votingLevel = result.getVotingLevel();

        ResultRealTime resultRealTime = resultRealTimeRepository.findByElectionAndLgaAndVotingLevelAndElectionType(result.getElection(), result.getLga(), votingLevel, result.getElectionType()).get(0);
        PartyAgent partyAgent = partyAgentService.findPartyAgentById(resultDto.getPartyAgentId()).getPartyAgent();
        result.setPartyAgent(partyAgent);
        result.setElectionType(resultDto.getElectionType());
        result.setAccreditedVotersCount(resultDto.getAccreditedVotersCount());
        result.setRegisteredVotersCount(resultDto.getRegisteredVotersCount());
        resultRealTime.setParty_1(resultDto.getParty_1());
        resultRealTime.setParty_2(resultDto.getParty_2());
        resultRealTime.setParty_3(resultDto.getParty_3());
        resultRealTime.setParty_4(resultDto.getParty_4());
        resultRealTime.setParty_5(resultDto.getParty_5());
        resultRealTime.setParty_6(resultDto.getParty_6());
        resultRealTime.setResult(id);
        resultRealTime.setElectionType(resultDto.getElectionType());
        resultRealTime.setVoteCount(resultDto.getParty_1()+resultDto.getParty_2()+resultDto.getParty_3()+resultDto.getParty_4()+resultDto.getParty_5()+resultDto.getParty_6());

        resultRepository.save(result);
        resultRealTimeRepository.save(resultRealTime);
        saveResultPerParty(resultDto, state, result);

        return new ResultResponse("00", String.format(successTemplate,SERVICE_NAME));
    }

    @Override
    public ResultResponse deleteResultById(Long id) throws NotFoundException {
        Result result = getResult(id);
        resultPerPartyRepository.deleteByResult(result);
        resultRepository.deleteById(id);
        resultRealTimeRepository.deleteByResult(id);
        return new ResultResponse("00",String.format(deleteTemplate,SERVICE_NAME));
    }

    @Override
    public ResultResponse findAll() {
        // modify to find by election type
        List<Result> elections = resultRepository.findAll();
        return new ResultResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), elections);
    }

    @Override
    public ResultResponse findByStateId(Long stateId) {
        List<Result> results = resultRepository.findByStateId(stateId);
        return new ResultResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), results);
    }

    @Override
    public ResultResponse findByZoneId(Long zoneId) {
        List<Result> results = resultRepository.findByGeoPoliticalZoneId(zoneId);
        return new ResultResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), results);
    }

    @Override
    public Election getElection() throws NotFoundException {
        List<Election> election = electionRepository.findByStatus(true);
        if(election.size() == 0){
            throw new NotFoundException(String.format(notFoundTemplate,"Election"));
        }
        return election.get(0);
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
        Lga lga = lgaService.findLgaById(id).getLga();
        return lga;
    }

    private SenatorialDistrict getSenatorialDistrict(Long id) throws NotFoundException {
        SenatorialDistrict senatorialDistrict = senatorialDistrictService.findSenatorialDistrictById(id).getSenatorialDistrict();
        return senatorialDistrict;
    }

    private Ward getWard(Long id) throws NotFoundException {
        if(id==null) return new Ward(){{
            setId(1L);
        }};
        Ward currentWard = wardService.findWardById(id).getWard();
        if(currentWard == null){
            throw new NotFoundException(String.format(notFoundTemplate,"Ward"));
        }
        return currentWard;
    }

    private PollingUnit getPollingUnit(Long id) throws NotFoundException {
        if(id==null) return new PollingUnit(){{
            setId(1L);
        }};
        PollingUnit currentPollingUnit = pollingUnitService.findPollingUnitById(id).getPollingUnit();
        if(currentPollingUnit == null){
            throw new NotFoundException(String.format(notFoundTemplate,"Polling Unit"));
        }
        return currentPollingUnit;
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
    public ResultResponse filterBySenatorialDistrict(Long id) throws NotFoundException {
        SenatorialDistrict senatorialDistrict = getSenatorialDistrict(id);
        List<Result> results = resultRepository.findBySenatorialDistrict(senatorialDistrict);
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
                            String accreditedVotersCount,
                            String registeredVotersCount,
                            String party1Votes,
                            String party2Votes,
                            String party3Votes,
                            String party4Votes,
                            String party5Votes,
                            String party6Votes
    ) {
        try{
            ResultDto resultDto = new ResultDto();

            Election election = electionRepository.findByCode(electionCode);
            resultDto.setElectionId(election.getId());
            PartyAgent partyAgent = partyAgentService.findPartyAgentByPhone(phoneNumber).getPartyAgent();
            resultDto.setPartyAgentId(partyAgent.getId());
            Lga lga = lgaService.findLgaById(Long.parseLong(lgaCode)).getLga();
            resultDto.setLgaId(lga.getId());
            Ward ward = wardService.findWardById(Long.parseLong(wardCode)).getWard();
            resultDto.setWardId(ward.getId());
            PollingUnit pollingUnit = pollingUnitService.findPollingUnitById(Long.parseLong(pollingUnitCode)).getPollingUnit();
            resultDto.setPollingUnitId(pollingUnit.getId());
            resultDto.setSenatorialDistrictId(lga.getSenatorialDistrict().getId());
            resultDto.setAccreditedVotersCount(Integer.valueOf(accreditedVotersCount));
            resultDto.setRegisteredVotersCount(Integer.valueOf(registeredVotersCount));
            resultDto.setParty_1(Integer.valueOf(party1Votes));
            resultDto.setParty_2(Integer.valueOf(party2Votes));
            resultDto.setParty_3(Integer.valueOf(party3Votes));
            resultDto.setParty_4(Integer.valueOf(party4Votes));
            resultDto.setParty_5(Integer.valueOf(party5Votes));
            resultDto.setParty_6(Integer.valueOf(party6Votes));
            VotingLevel votingLevel = votingLevelRepository.findByCode(votingLevelCode);
            resultDto.setVotingLevelId(votingLevel.getId());

            saveResult(resultDto);

        }
        catch (Exception ex){
            LOGGER.info(String.format("%s result could not be saved",lgaCode));
        }
    }

    @Override
    public ResultResponse uploadResult(MultipartFile file){
        List<String> csvLines = FileUtil.getCsvLines(file, fileProcessingService.getFileStorageLocation());
        return processUpload(csvLines);
    }


    private ResultResponse processUpload(List<String> lines){
        for (String line:lines) {
            String[] state = line.split(",");
            saveResult(state[0].trim(), state[1].trim(), state[2].trim(),state[3].trim(), state[4].trim(), state[5].trim(),state[6].trim(), state[7].trim(), state[8].trim(), state[9].trim(), state[10].trim(), state[11].trim(), state[12].trim(), state[13].trim());
        }
        return new ResultResponse("00", "File Uploaded.");
    }
}
