package com.edunge.srtool.service.impl;

import com.edunge.srtool.dto.ResultDto;
import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.*;
import com.edunge.srtool.repository.*;
import com.edunge.srtool.response.ResultRealTimeResponse;
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
import java.util.Set;

@Transactional
@Service
public class ResultServiceImpl implements ResultService {
    private final ResultRepository resultRepository;
    private final ResultRealTimeRepository resultRealTimeRepository;
    private final PartyAgentRepository partyAgentRepository;
    private final ElectionRepository electionRepository;
    private final SenatorialDistrictRepository senatorialDistrictRepository;
    private final StateRepository stateRepository;
    private final WardRepository wardRepository;
    private final LgaRepository lgaRepository;
    private final PollingUnitRepository pollingUnitRepository;
    private final VotingLevelRepository votingLevelRepository;
    private final ResultPerPartyRepository resultPerPartyRepository;
    private final PoliticalPartyRepository politicalPartyRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(ResultServiceImpl.class);

    private static final String SERVICE_NAME = "Result";
    private static final String VOTING_LEVEL_POLLING_UNIT = "PollingUnit";
    private static final String VOTING_LEVEL_LGA = "LGA";
    private static final String VOTING_LEVEL_WARD = "Ward";

    private static final String FIRST_PARTY = "party_1";
    private static final String SECOND_PARTY = "party_2";
    private static final String THIRD_PARTY = "party_3";
    private static final String FOURTH_PARTY = "party_4";


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
    public ResultServiceImpl(ResultRepository resultRepository, ResultRealTimeRepository resultRealTimeRepository, PartyAgentRepository partyAgentRepository, ElectionRepository electionRepository, SenatorialDistrictRepository senatorialDistrictRepository,
            StateRepository stateRepository, WardRepository wardRepository, LgaRepository lgaRepository, PollingUnitRepository pollingUnitRepository, VotingLevelRepository votingLevelRepository, ResultPerPartyRepository resultPerPartyRepository, PoliticalPartyRepository politicalPartyRepository) {
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
        this.stateRepository = stateRepository;
    }

    @Override
    public ResultResponse saveResult(ResultDto resultDto) throws NotFoundException {
        State state = getState();
        Optional<PartyAgent> partyAgent = partyAgentRepository.findById(resultDto.getPartyAgentId());
        SenatorialDistrict senatorialDistrict = getSenatorialDistrict(resultDto.getSenatorialDistrictId());


        Election election = getElection();
        VotingLevel votingLevel = getVotingLevel(resultDto.getVotingLevelId());
        PollingUnit pollingUnit = getPollingUnit(resultDto.getPollingUnitId());
        Ward ward = getWard(resultDto.getWardId());
        //Result result = resultRepository.findByElectionAndPollingUnit(election, pollingUnit);
        Lga lga = getLga(resultDto.getLgaId());
        int pollingUnitCount = 1;
        boolean checkingRealTime = checkForDuplicate(election, votingLevel, lga, ward);

        Result result = new Result();
        ResultRealTime resultRealTime = new ResultRealTime();

        //Delete existing result if the voting level is either LGA or Ward level.
        if(votingLevel.getCode().equals(VOTING_LEVEL_WARD)){
            result = resultRepository.findByElectionAndWardAndVotingLevel(election, ward, votingLevel);
            if(result != null){
                throw new DuplicateException(String.format("Result for %s in %s already exists.", ward.getName(), election.getDescription()));
            }
            result = new Result();
            result.setWard(ward);
            resultRealTime.setWard(ward);
            if(checkingRealTime) resultRealTimeRepository.deleteByWard(ward);

            pollingUnitCount = pollingUnitRepository.findByWard(ward).size();
        }
        else if(votingLevel.getCode().equals(VOTING_LEVEL_LGA)){
            List<ResultRealTime> resultRealTimeList = resultRealTimeRepository.findByElectionAndLgaAndVotingLevel(election, lga, votingLevel);
            if(resultRealTimeList.size() >0){
                throw new DuplicateException(String.format("Result for %s in %s already exists.", lga.getName(), election.getDescription()));
            }
            result = new Result();
            result.setLga(lga);
            resultRealTime.setLga(lga);
            if(checkingRealTime) resultRealTimeRepository.deleteByLga(lga);
            pollingUnitCount = pollingUnitRepository.findByLga(lga).size();
        }

        else if(votingLevel.getCode().equals(VOTING_LEVEL_POLLING_UNIT)){
            result = resultRepository.findByElectionAndPollingUnit(election, pollingUnit);
            if(result != null){
                throw new DuplicateException(String.format("Result for %s in %s already exists.", pollingUnit.getName(), election.getDescription()));
            }
            result = new Result();
            result.setPollingUnit(pollingUnit);
            resultRealTime.setPollingUnit(pollingUnit);
        }

        result.setSenatorialDistrict(senatorialDistrict);
        resultRealTime.setSenatorialDistrict(senatorialDistrict);
        result.setStateId(state.getId());
        resultRealTime.setStateId(state.getId());
        if(partyAgent.isPresent()){
            result.setPartyAgent(partyAgent.get());
            resultRealTime.setPartyAgent(partyAgent.get());
        }
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

        LOGGER.info("Saving result at voting level {} ", votingLevel.getCode());
        Result r = resultRepository.save(result);
        // check if a higher level already exists before committing
        if(checkingRealTime) {
            resultRealTime.setResult(r.getId());
            resultRealTime.setVoteCount(resultDto.getParty_1()+resultDto.getParty_2()+resultDto.getParty_3()+resultDto.getParty_4());
            resultRealTimeRepository.save(resultRealTime);
        }

        //@Todo Remove party code manual update. There's an API to save result per party.
        //Save APC votes;
        PoliticalParty apc = politicalPartyRepository.findByCode(SECOND_PARTY);
        ResultPerParty resultPerParty = new ResultPerParty();
        resultPerParty.setVoteCount(resultDto.getParty_2());
        resultPerParty.setResult(result);
        resultPerParty.setPoliticalParty(apc);
        resultPerPartyRepository.save(resultPerParty);

        //Save APC votes;
        PoliticalParty pdp = politicalPartyRepository.findByCode(FIRST_PARTY);
        ResultPerParty resultPerPartyPdp = new ResultPerParty();
        resultPerPartyPdp.setVoteCount(resultDto.getParty_1());
        resultPerPartyPdp.setResult(result);
        resultPerPartyPdp.setPoliticalParty(pdp);
        resultPerPartyRepository.save(resultPerPartyPdp);

        //Save ANPP votes;
        PoliticalParty anpp = politicalPartyRepository.findByCode(THIRD_PARTY);
        ResultPerParty resultPerPartyAnpp = new ResultPerParty();
        resultPerPartyAnpp.setVoteCount(resultDto.getParty_3());
        resultPerPartyAnpp.setResult(result);
        resultPerPartyAnpp.setPoliticalParty(anpp);
        resultPerPartyRepository.save(resultPerPartyAnpp);

        //Save OTHERS votes;
        PoliticalParty others = politicalPartyRepository.findByCode(FOURTH_PARTY);
        ResultPerParty resultPerPartyOthers = new ResultPerParty();
        resultPerPartyOthers.setVoteCount(resultDto.getParty_4());
        resultPerPartyOthers.setResult(result);
        resultPerPartyOthers.setPoliticalParty(others);

        resultPerPartyRepository.save(resultPerPartyOthers);

        return new ResultResponse("00", String.format(successTemplate,SERVICE_NAME));

    }

    private boolean checkForDuplicate(Election election, VotingLevel votingLevel, Lga lga, Ward ward) throws NotFoundException {
        if(votingLevel.getCode().equals(VOTING_LEVEL_LGA)) {
            return !(resultRealTimeRepository.findByElectionAndLgaAndVotingLevel(election, lga, votingLevel).size() > 0);
            // same ward
        }
        VotingLevel votingLevel1 = getVotingLevel(0L);
        if(votingLevel.getCode().equals(VOTING_LEVEL_WARD)) {
            return !(resultRealTimeRepository.findByElectionAndLgaAndVotingLevel(election, lga, votingLevel1).size() > 0);
        }
        else {
            if(resultRealTimeRepository.findByElectionAndLgaAndVotingLevel(election, lga, votingLevel1).size() >0){
                return false;
            }
            return !(resultRealTimeRepository.findByElectionAndWardAndVotingLevelLessThan(election, ward, votingLevel).size()>0);
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

        VotingLevel votingLevel = result.getVotingLevel();

        ResultRealTime resultRealTime = resultRealTimeRepository.findByElectionAndLgaAndVotingLevel(result.getElection(), result.getLga(), votingLevel).get(0);
        Optional<PartyAgent> partyAgent = partyAgentRepository.findById(resultDto.getPartyAgentId());
        partyAgent.ifPresent(result::setPartyAgent);
        result.setAccreditedVotersCount(resultDto.getAccreditedVotersCount());
        result.setRegisteredVotersCount(resultDto.getRegisteredVotersCount());

        resultRealTime.setParty_1(resultDto.getParty_1());
        resultRealTime.setParty_2(resultDto.getParty_2());
        resultRealTime.setParty_3(resultDto.getParty_3());
        resultRealTime.setParty_4(resultDto.getParty_4());
        resultRealTime.setResult(id);
        resultRealTime.setVoteCount(resultDto.getParty_1()+resultDto.getParty_2()+resultDto.getParty_3()+resultDto.getParty_4());


        resultRepository.save(result);

        resultRealTimeRepository.save(resultRealTime);


        PoliticalParty apc = politicalPartyRepository.findByCode(SECOND_PARTY);
        ResultPerParty resultPerParty = resultPerPartyRepository.findByResultAndPoliticalParty(result,apc);
        resultPerParty.setVoteCount(resultDto.getParty_2());
        resultPerParty.setResult(result);
        resultPerParty.setPoliticalParty(apc);
        resultPerPartyRepository.save(resultPerParty);

        //Save PDP votes;
        PoliticalParty pdp = politicalPartyRepository.findByCode(FIRST_PARTY);
        ResultPerParty resultPerPartyPdp = resultPerPartyRepository.findByResultAndPoliticalParty(result,pdp);
        resultPerPartyPdp.setVoteCount(resultDto.getParty_1());
        resultPerPartyPdp.setResult(result);
        resultPerPartyPdp.setPoliticalParty(pdp);
        resultPerPartyRepository.save(resultPerPartyPdp);

        //Save APGA votes;
        PoliticalParty anpp = politicalPartyRepository.findByCode(THIRD_PARTY);
        ResultPerParty resultPerPartyAnpp = resultPerPartyRepository.findByResultAndPoliticalParty(result,anpp);
        resultPerPartyAnpp.setVoteCount(resultDto.getParty_3());
        resultPerPartyAnpp.setResult(result);
        resultPerPartyAnpp.setPoliticalParty(anpp);
        resultPerPartyRepository.save(resultPerPartyAnpp);

        //Save FOURTH votes;
        PoliticalParty others = politicalPartyRepository.findByCode(FOURTH_PARTY);
        ResultPerParty resultPerPartyOthers = resultPerPartyRepository.findByResultAndPoliticalParty(result,others);
        resultPerPartyOthers.setVoteCount(resultDto.getParty_4());
        resultPerPartyOthers.setResult(result);
        resultPerPartyOthers.setPoliticalParty(others);
        resultPerPartyRepository.save(resultPerPartyOthers);

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
        List<Result> elections = resultRepository.findAll();
        return new ResultResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), elections);
    }

    @Override
    public ResultResponse findByStateId() {
        try {
            State state = this.getState();
            List<Result> elections = resultRepository.findByStateId(state.getId());
            return new ResultResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), elections);
        }catch (NotFoundException e){
            return new ResultResponse("99", String.format(fetchRecordTemplate, SERVICE_NAME));
        }
    }


    private Election getElection() throws NotFoundException {
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

    private State getState() throws NotFoundException {
        State state = stateRepository.findByDefaultState(true);
        if(state == null){
            throw new NotFoundException("Default state not found.");
        }
        return state;
    }

    private Ward getWard(Long id) throws NotFoundException {
        if(id==null) return new Ward(){{
            setId(1L);
        }};
        Optional<Ward> currentWard = wardRepository.findById(id);
        if(!currentWard.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,"Ward"));
        }
        return currentWard.get();
    }

    private PollingUnit getPollingUnit(Long id) throws NotFoundException {
        if(id==null) return new PollingUnit(){{
            setId(1L);
        }};
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
                            String party4Votes) {
        try{
            ResultDto resultDto = new ResultDto();

            Election election = electionRepository.findByCode(electionCode);
            resultDto.setElectionId(election.getId());
            PartyAgent partyAgent = partyAgentRepository.findByPhone(phoneNumber);
            resultDto.setPartyAgentId(partyAgent.getId());
            Lga lga = lgaRepository.findByCode(lgaCode);
            resultDto.setLgaId(lga.getId());
            Ward ward = wardRepository.findByCode(wardCode);
            resultDto.setWardId(ward.getId());
            PollingUnit pollingUnit = pollingUnitRepository.findByCode(pollingUnitCode);
            resultDto.setPollingUnitId(pollingUnit.getId());
            resultDto.setSenatorialDistrictId(lga.getSenatorialDistrict().getId());
            resultDto.setAccreditedVotersCount(Integer.valueOf(accreditedVotersCount));
            resultDto.setRegisteredVotersCount(Integer.valueOf(registeredVotersCount));
            resultDto.setParty_1(Integer.valueOf(party1Votes));
            resultDto.setParty_2(Integer.valueOf(party2Votes));
            resultDto.setParty_3(Integer.valueOf(party3Votes));
            resultDto.setParty_4(Integer.valueOf(party4Votes));
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
            saveResult(state[0].trim(), state[1].trim(), state[2].trim(),state[3].trim(), state[4].trim(), state[5].trim(),state[6].trim(), state[7].trim(), state[8].trim(), state[9].trim(), state[10].trim(), state[11].trim());
        }
        return new ResultResponse("00", "File Uploaded.");
    }
}
