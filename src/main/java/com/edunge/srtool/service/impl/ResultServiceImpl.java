package com.edunge.srtool.service.impl;

import com.edunge.srtool.dto.ResultDto;
import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.*;
import com.edunge.srtool.repository.*;
import com.edunge.srtool.response.ResultResponse;
import com.edunge.srtool.service.ResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    public ResultServiceImpl(ResultRepository resultRepository, PartyAgentRepository partyAgentRepository, ElectionRepository electionRepository, SenatorialDistrictRepository senatorialDistrictRepository, WardRepository wardRepository, LgaRepository lgaRepository, PollingUnitRepository pollingUnitRepository, VotingLevelRepository votingLevelRepository) {
        this.resultRepository = resultRepository;
        this.partyAgentRepository = partyAgentRepository;
        this.electionRepository = electionRepository;
        this.senatorialDistrictRepository = senatorialDistrictRepository;
        this.wardRepository = wardRepository;
        this.lgaRepository = lgaRepository;
        this.pollingUnitRepository = pollingUnitRepository;
        this.votingLevelRepository = votingLevelRepository;
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
}
