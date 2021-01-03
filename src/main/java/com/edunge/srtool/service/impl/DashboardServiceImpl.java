package com.edunge.srtool.service.impl;

import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.*;
import com.edunge.srtool.repository.*;
import com.edunge.srtool.response.DashboardResponse;
import com.edunge.srtool.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final StateRepository stateRepository;
    private final LgaRepository lgaRepository;
    private final SenatorialDistrictRepository senatorialDistrictRepository;
    private  final WardRepository wardRepository;
    private final PollingUnitRepository pollingUnitRepository;
    private final PoliticalPartyRepository politicalPartyRepository;
    private final ResultRepository resultRepository;
    private final ResultPerPartyRepository resultPerPartyRepository;

    @Autowired
    public DashboardServiceImpl(StateRepository stateRepository, LgaRepository lgaRepository, SenatorialDistrictRepository senatorialDistrictRepository, WardRepository wardRepository, PollingUnitRepository pollingUnitRepository, PoliticalPartyRepository politicalPartyRepository, ResultRepository resultRepository, ResultPerPartyRepository resultPerPartyRepository) {
        this.stateRepository = stateRepository;
        this.lgaRepository = lgaRepository;
        this.senatorialDistrictRepository = senatorialDistrictRepository;
        this.wardRepository = wardRepository;
        this.pollingUnitRepository = pollingUnitRepository;
        this.politicalPartyRepository = politicalPartyRepository;
        this.resultRepository = resultRepository;
        this.resultPerPartyRepository = resultPerPartyRepository;
    }

    private Long getTotalStates(){
        return stateRepository.count();
    }

    private Long getTotalLgas(){
        return lgaRepository.count();
    }

    private Long getSenatorialDistrics(){
        return senatorialDistrictRepository.count();
    }

    private Long getWards(){
        return wardRepository.count();
    }
    private Long getPollingUnits(){
        return pollingUnitRepository.count();
    }
    private Long getResults(){
        return resultRepository.count();
    }

    private Long getPoliticalParties() {
        return politicalPartyRepository.count();
    }

    private Integer getTotalRegisteredVotes(){
        List<Result> results = resultRepository.findAll();
        return results.stream().map(Result::getRegisteredVotersCount).mapToInt(Integer::intValue).sum();
    }

    private Integer getTotalAccreditedVotes(){
        List<Result> results = resultRepository.findAll();
        return results.stream().map(Result::getAccreditedVotersCount).mapToInt(Integer::intValue).sum();
    }

    private Integer getTotalVotes(){
        List<ResultPerParty> resultPerParties = resultPerPartyRepository.findAll();
        return resultPerParties.stream().map(ResultPerParty::getVoteCount).mapToInt(Integer::intValue).sum();
    }

    public DashboardResponse getDefaultDashboard(){
        Long totalStates = getTotalStates();
        Long totalLgas = getTotalLgas();
        Long totalSenatorialDistricts = getSenatorialDistrics();
        Integer totalRegisteredVotes = getTotalRegisteredVotes();
        Integer totalAccreditedVotes = getTotalAccreditedVotes();
        Integer totalVoteCounts = getTotalVotes();
        Long totalWards  = getWards();
        Long totalPollingUnits = getPollingUnits();
//        ((Count of Polling G + Total number Polling units in Ward C + Total number of polling units in LGA A) / Total number of polling units in the system) * 100
        Double resultsReceived = (totalVoteCounts *100.0) / totalAccreditedVotes;
        return new DashboardResponse("00", "Dashboard loaded", totalStates, totalLgas, totalSenatorialDistricts, totalRegisteredVotes, totalAccreditedVotes, totalVoteCounts, totalWards, totalPollingUnits, resultsReceived);
    }

    public DashboardResponse getDashboardByState(Long stateId) throws NotFoundException {
        State state = getState(stateId);

        Long totalStates = getTotalStates();
        Long totalLgas = lgaByState(state);
        Long totalSenatorialDistricts = senatorialDistrictByState(state);
        Integer totalRegisteredVotes = getRegisteredVotersPerSenatorialDistrict(state);
        Integer totalAccreditedVotes =  getAccreditedVotesPerSenatorialDistrict(state);
        Long totalWards  = Long.valueOf(getWardsByLocalGovernment(state));
        Long totalPollingUnits = Long.valueOf(getPollingUnitsByWard(state));
        Integer totalVoteCounts = getVoteCountsByState(state);
        Long lgaWithResults = getStateLgasWithResult(state);
        Long wardsWithResults = getStateWardsWithResult(state);
        Long pollingUnitsWithResults = getStatePollingUnitsWithResult(state);

        Double resultsReceived = (totalVoteCounts *100.0) / totalAccreditedVotes;

        return new DashboardResponse("00", "Dashboard loaded", totalStates,
                totalLgas, totalSenatorialDistricts, totalRegisteredVotes, totalAccreditedVotes,
                totalVoteCounts, totalWards, totalPollingUnits,
                lgaWithResults,
                wardsWithResults,
                pollingUnitsWithResults,
                resultsReceived
        );
    }


    private Long lgaByState(State state){
        List<Lga> lga = lgaRepository.findByState(state);
        return (long) lga.size();
    }

    private Long senatorialDistrictByState(State state){
        List<SenatorialDistrict> senatorialDistricts = senatorialDistrictRepository.findByState(state);
        return (long) senatorialDistricts.size();
    }

    private Integer getRegisteredVotersPerSenatorialDistrict(State state){
        List<Result> results = resultRepository.findAll();
        List<SenatorialDistrict> senatorialDistricts = senatorialDistrictRepository.findByState(state);
        int totalResult = 0;

        if(senatorialDistricts.size()>0){
            for (SenatorialDistrict district:senatorialDistricts) {
                totalResult += results.stream()
                        .filter(result -> result.getSenatorialDistrict().getId().equals(district.getId()))
                        .map(Result::getRegisteredVotersCount).mapToInt(Integer::intValue).sum();
            }
        }
        return totalResult;
    }

    private Integer getAccreditedVotesPerSenatorialDistrict(State state){
        List<Result> results = resultRepository.findAll();
        List<SenatorialDistrict> senatorialDistricts = senatorialDistrictRepository.findByState(state);
        int totalResult = 0;

        if(senatorialDistricts.size()>0){
            for (SenatorialDistrict district:senatorialDistricts) {
                totalResult += results.stream()
                        .filter(result -> result.getSenatorialDistrict().getId().equals(district.getId()))
                        .map(Result::getAccreditedVotersCount).mapToInt(Integer::intValue).sum();
            }
        }
        return totalResult;
    }


    public Integer getWardsByLocalGovernment(State state){
        List<Ward> wards = wardRepository.findAll();
        List<Lga> lgas = lgaRepository.findByState(state);
        int totalResult = 0;

        if(lgas.size()>0){
            for (Lga lga:lgas) {
                totalResult += wards.stream()
                        .filter(ward ->  ward.getLga().getId().equals(lga.getId())).count();
            }
        }
        return totalResult;
    }

    public Integer getPollingUnitsByWard(State state){
        List<PollingUnit> pollingUnits = pollingUnitRepository.findAll();
        List<Ward> wards = wardRepository.findAll();
        List<Lga> lgas = lgaRepository.findByState(state);
        int totalResult = 0;

        if(lgas.size()>0){
            for (Lga lga:lgas) {
                for (Ward ward : wards) {
                    if (ward.getLga().getId().equals(lga.getId())) {
                        totalResult += pollingUnits.stream()
                                .filter(pollingUnit -> pollingUnit.getWard().getId().equals(ward.getId())).count();
                    }
                }
            }
        }
        return totalResult;
    }

    public Integer getVoteCountsByState(State state){
        List<ResultPerParty> resultPerParties = resultPerPartyRepository.findAll();
        return resultPerParties.stream()
                .filter(resultPerParty -> resultPerParty.getResult().getLga().getState().getId().equals(state.getId()))
                .map(ResultPerParty::getVoteCount)
                .mapToInt(Integer::intValue).sum();
    }

    public Long getStatePollingUnitsWithResult(State state){
        List<ResultPerParty> resultPerParties = resultPerPartyRepository.findAll();
        HashSet<String> pollingUnits = new HashSet<>();
        resultPerParties.stream()
                .filter(resultPerParty -> resultPerParty.getResult().getLga().getState().getId().equals(state.getId()))
                .forEach(resultPerParty -> pollingUnits.add(resultPerParty.getResult().getPollingUnit().getCode()));
        return (long) pollingUnits.size();
    }

    public Long getStateWardsWithResult(State state){
        List<ResultPerParty> resultPerParties = resultPerPartyRepository.findAll();
        HashSet<String> wards = new HashSet<>();
        resultPerParties.stream()
                .filter(resultPerParty -> resultPerParty.getResult().getLga().getState().getId().equals(state.getId()))
                .forEach(resultPerParty -> wards.add(resultPerParty.getResult().getWard().getCode()));
        return (long) wards.size();
    }

    public Long getStateLgasWithResult(State state){
        List<ResultPerParty> resultPerParties = resultPerPartyRepository.findAll();
        HashSet<String> lga = new HashSet<>();
        resultPerParties.stream()
                .filter(resultPerParty -> resultPerParty.getResult().getLga().getState().getId().equals(state.getId()))
                .forEach(resultPerParty -> lga.add(resultPerParty.getResult().getLga().getCode()));
        return (long) lga.size();
    }

    private State getState(Long id) throws NotFoundException {
        Optional<State> state = stateRepository.findById(id);
        if(!state.isPresent()){
            throw new NotFoundException("State not found.");
        }
        return state.get();
    }
}
