package com.edunge.srtool.service.impl;

import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.*;
import com.edunge.srtool.repository.*;
import com.edunge.srtool.response.DashboardResponse;
import com.edunge.srtool.response.PartyResult;
import com.edunge.srtool.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final StateRepository stateRepository;
    private final LgaRepository lgaRepository;
    private final SenatorialDistrictRepository senatorialDistrictRepository;
    private  final WardRepository wardRepository;
    private final PollingUnitRepository pollingUnitRepository;
    private final PoliticalPartyRepository politicalPartyRepository;
    private final ResultRepository resultRepository;
    private final ResultRealTimeRepository resultRealTimeRepository;
    private final ResultPerPartyRepository resultPerPartyRepository;
    private static final String FIRST_PARTY = "party_1";
    private static final String SECOND_PARTY = "party_2";
    private static final String THIRD_PARTY = "party_3";
    private static final String FOURTH_PARTY = "party_4";

    @Autowired
    public DashboardServiceImpl(StateRepository stateRepository, LgaRepository lgaRepository,
                                SenatorialDistrictRepository senatorialDistrictRepository,
                                WardRepository wardRepository, PollingUnitRepository pollingUnitRepository,
                                PoliticalPartyRepository politicalPartyRepository, ResultRepository resultRepository,
                                ResultPerPartyRepository resultPerPartyRepository, ResultRealTimeRepository resultRealTimeRepository) {
        this.stateRepository = stateRepository;
        this.lgaRepository = lgaRepository;
        this.senatorialDistrictRepository = senatorialDistrictRepository;
        this.wardRepository = wardRepository;
        this.pollingUnitRepository = pollingUnitRepository;
        this.politicalPartyRepository = politicalPartyRepository;
        this.resultRepository = resultRepository;
        this.resultPerPartyRepository = resultPerPartyRepository;
        this.resultRealTimeRepository = resultRealTimeRepository;
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
        List<ResultRealTime> resultRealTimeList = resultRealTimeRepository.findAll();
        return resultRealTimeList.stream().map(ResultRealTime::getVoteCount).mapToInt(Integer::intValue).sum();
    }

    private double getResultsReceivedByState(State state){
        // total polling units in the system and total polling units gotten
        int pollingUnits = pollingUnitRepository.findByState(state).size();
        List<ResultRealTime> resultRealTimeList = resultRealTimeRepository.findAll();
        int pollingUnitsReceived = resultRealTimeList.stream().map(ResultRealTime::getPollingUnitCount).mapToInt(Integer::intValue).sum();
        return pollingUnitsReceived * 100 / pollingUnits;
    }

    private double getResultsReceivedBySenatorialDistrict(SenatorialDistrict senatorialDistrict){
        // total polling units in the system and total polling units gotten
        int pollingUnits = pollingUnitRepository.findBySenatorialDistrict(senatorialDistrict).size();
        List<ResultRealTime> resultRealTimeList = resultRealTimeRepository.findBySenatorialDistrict(senatorialDistrict);
        int pollingUnitsReceived = resultRealTimeList.stream().map(ResultRealTime::getPollingUnitCount).mapToInt(Integer::intValue).sum();
        return pollingUnitsReceived * 100 / pollingUnits;
    }

    private double getResultsReceivedByLga(Lga lga){
        // total polling units in the system and total polling units gotten
        int pollingUnits = pollingUnitRepository.findByLga(lga).size();
        List<ResultRealTime> resultRealTimeList = resultRealTimeRepository.findByLga(lga);
        int pollingUnitsReceived = resultRealTimeList.stream().map(ResultRealTime::getPollingUnitCount).mapToInt(Integer::intValue).sum();
        return pollingUnitsReceived * 100 / pollingUnits;
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
        Integer totalRegisteredVotes = getRegisteredVotersPerState(state);
        Integer totalAccreditedVotes =  getAccreditedVotesPerState(state);
        Long totalWards  = Long.valueOf(getWards(state));
        Long totalPollingUnits = Long.valueOf(getPollingUnitsByWard(state));
        Integer totalVoteCounts = getVoteCountsByState(state);
        Long lgaWithResults = getStateLgasWithResult(state);
        Long wardsWithResults = getStateWardsWithResult(state);
        Long pollingUnitsWithResults = getStatePollingUnitsWithResult(state);

        List<PoliticalParty> politicalParties = politicalPartyRepository.findAll();
        List<PartyResult> partyResults = new ArrayList<>();

        politicalParties
                .forEach(politicalParty -> {

                    List<Result> results = resultRepository.findAll();
                    AtomicReference<Integer> voteCount = new AtomicReference<>(0);
                    AtomicReference<Integer> partyCount = new AtomicReference<>(0);
                    results.stream()
                            .filter(result -> result.getLga().getState().getId().equals(state.getId()))
                            .map(Result::getResultPerParties).forEach(resultPerParties -> {
                        for (ResultPerParty resultPerparty: resultPerParties) {
                            if(resultPerparty.getPoliticalParty().getId().equals(politicalParty.getId())){
                                voteCount.updateAndGet(v -> v + resultPerparty.getVoteCount());
                            }
                        }
                    });
                    PartyResult partyResult = new PartyResult();
                    partyResult.setPoliticalParty(politicalParty);
                    partyResult.setTotalVoteCount(voteCount.get());
                    Double percent = (voteCount.get() * 100.0) / totalVoteCounts;
                    partyResult.setPercent(percent);
                    partyResults.add(partyResult);
                });

        List<Result> results = resultRepository.findAll();

        //Get polling units by wardLevel results
        AtomicReference<Integer> wardLevelResult = new AtomicReference<>(0);
        AtomicReference<Integer> existingPollingUnitUnderWard = new AtomicReference<>(0);
        results.stream()
                .filter(result -> result.getLga().getState().getId().equals(state.getId()))
                .filter(result -> result.getVotingLevel().getCode().equals("Ward")).forEach(result -> {
            List<PollingUnit> pollingUnits = pollingUnitRepository.findByWard(result.getWard());
            long current = results.stream()
                    .filter(currentResult -> currentResult.getWard().getId().equals(result.getWard().getId()))
                    .count();
            wardLevelResult.updateAndGet(v -> v + pollingUnits.size());
            existingPollingUnitUnderWard.set(Math.toIntExact(current));
        });
        pollingUnitsWithResults+=wardLevelResult.get();
        pollingUnitsWithResults-=existingPollingUnitUnderWard.get();

        //Get polling units by wardLevel results
        AtomicReference<Integer> lgaLevelResult = new AtomicReference<>(0);
        AtomicReference<Integer> existingPollingUnitUnderLga = new AtomicReference<>(0);
        results.stream()
                .filter(result -> result.getLga().getState().getId().equals(state.getId()))
                .filter(result -> result.getVotingLevel().getCode().equals("LGA")).forEach(result -> {
            List<PollingUnit> pollingUnits = pollingUnitRepository.findByLga(result.getLga());

            long current = results.stream()
                    .filter(result1 -> result1.getLga().getId().equals(result.getLga().getId()))
                    .count();
            lgaLevelResult.updateAndGet(v -> v + pollingUnits.size());
            existingPollingUnitUnderLga.set((int) current);
        });
        pollingUnitsWithResults+=lgaLevelResult.get();
        pollingUnitsWithResults-= existingPollingUnitUnderLga.get();

        List<LgaResult> lgaResults = new ArrayList<>();
        HashSet<String> lgaSet = new HashSet<>();
        HashMap<String,Integer> lgasWon = new HashMap<>();
        results.stream().filter(result -> result.getLga().getState().getId().equals(stateId))
                .forEach(result -> {
                    try {
                        if(lgaSet.add(result.getLga().getCode())){
                            LgaResult lgaResult = getLgaResult(result.getLga().getId());
                            String winningParty = lgaResult.getPartyResults().get(lgaResult.getPartyResults().size()-1).getPoliticalParty().getName();
                            Integer currentValue = lgasWon.getOrDefault(winningParty, 0);
                            lgasWon.put(winningParty,currentValue+1);
                            lgaResults.add(lgaResult);
                        }
                    } catch (NotFoundException e) {
                        e.printStackTrace();
                    }
                });
        List<PartyLgaResult> partyLgaResults = new ArrayList<>();
        for (Map.Entry<String, Integer> lgaWon:lgasWon.entrySet()) {
            PartyLgaResult partyLgaResult = new PartyLgaResult(lgaWon.getKey(), lgaWon.getValue());
            partyLgaResults.add(partyLgaResult);
        }
        Double resultsReceived = (pollingUnitsWithResults * 100.0) / totalPollingUnits;//(totalVoteCounts *100.0) / totalAccreditedVotes;
        return new DashboardResponse("00", "Dashboard loaded.", totalStates,
                totalLgas, totalSenatorialDistricts, totalRegisteredVotes, totalAccreditedVotes,
                totalVoteCounts, totalWards, totalPollingUnits,
                lgaWithResults,
                wardsWithResults,
                pollingUnitsWithResults,
                resultsReceived,
                partyResults, lgaResults,partyLgaResults
        );
    }
/*
    public DashboardResponse getDashboardByState1(Long stateId) throws NotFoundException {
        State state = getState(stateId);

        Long totalStates = getTotalStates();
        Long totalLgas = lgaByState(state);
        Long totalSenatorialDistricts = senatorialDistrictByState(state);
        Integer totalRegisteredVotes = getRegisteredVotersPerState(state);
        Integer totalAccreditedVotes =  getAccreditedVotesPerState(state);
        Long totalWards  = Long.valueOf(getWards(state));
        Long totalPollingUnits = Long.valueOf(getPollingUnitsByWard(state));
        Integer totalVoteCounts = getVoteCountsByState(state);

        List<ResultRealTime> results = resultRealTimeRepository.findAll();
        HashSet<ResultRealTime> lgaWithResultList = lgasWithResult(results);
        Long lgaWithResults = (long)lgaWithResultList.size();
        Long wardsWithResults = getStateWardsWithResult(state);
        Long pollingUnitsWithResults = getStatePollingUnitsWithResult(state);

        List<PartyResult> partyResults = this.getPartyResults(totalVoteCounts);

        //Get polling units by wardLevel results
        pollingUnitsWithResults = (long)resultRealTimeRepository.findAll().stream()
                .map(ResultRealTime::getPollingUnitCount).mapToInt(Integer::intValue).sum();

        List<LgaResult> lgaResults = new ArrayList<>();
        HashSet<String> lgaSet = new HashSet<>();
        HashMap<String,Integer> lgasWon = new HashMap<>();
        //sum by lga and get the winners
        // get lga with results the iterate and get the sum of each and get the winner
        lgaWithResultList.stream()
                        .forEach(lgaResults->{
                            int party1 = resultRealTimeRepository.findByLga(lgaResults.getLga())
                                    .stream().map(ResultRealTime::getParty_1).mapToInt(Integer::intValue).sum();
                            int party2 = resultRealTimeRepository.findByLga(lgaResults.getLga())
                                    .stream().map(ResultRealTime::getParty_1).mapToInt(Integer::intValue).sum();
                            int party3 = resultRealTimeRepository.findByLga(lgaResults.getLga())
                                    .stream().map(ResultRealTime::getParty_1).mapToInt(Integer::intValue).sum();
                            int party4 = resultRealTimeRepository.findByLga(lgaResults.getLga())
                                    .stream().map(ResultRealTime::getParty_1).mapToInt(Integer::intValue).sum();
                            int max = party1;
                            String winningParty = FIRST_PARTY;
                            if(max < party2){
                                max = party2;
                                winningParty = SECOND_PARTY;
                            }
                            if(max < party3){
                                max = party3;
                                winningParty = THIRD_PARTY;
                            }
                            if(max < party4){
                                max = party4;
                                winningParty = FOURTH_PARTY;
                            }
                            winningParty = politicalPartyRepository.findByCode(winningParty).getName();
                            Integer currentValue = lgasWon.getOrDefault(winningParty, 0);
                            lgasWon.put(winningParty,currentValue+1);
                            lgaResults.add(lgaResult);
                        });

        results.stream().filter(result -> result.getLga().getState().getId().equals(stateId))
                .forEach(result -> {
                    try {
                        if(lgaSet.add(result.getLga().getCode())){
                            LgaResult lgaResult = getLgaResult(result.getLga().getId());
                            String winningParty = lgaResult.getPartyResults().get(lgaResult.getPartyResults().size()-1).getPoliticalParty().getName();
                            Integer currentValue = lgasWon.getOrDefault(winningParty, 0);
                            lgasWon.put(winningParty,currentValue+1);
                            lgaResults.add(lgaResult);
                        }
                    } catch (NotFoundException e) {
                        e.printStackTrace();
                    }
                });
        List<PartyLgaResult> partyLgaResults = new ArrayList<>();
        for (Map.Entry<String, Integer> lgaWon:lgasWon.entrySet()) {
            PartyLgaResult partyLgaResult = new PartyLgaResult(lgaWon.getKey(), lgaWon.getValue());
            partyLgaResults.add(partyLgaResult);
        }
        List<PartyResult> partyResults1 = new ArrayList<>(3);
        // party1
        partyResults1.add();

        Double resultsReceived = (pollingUnitsWithResults * 100.0) / totalPollingUnits;//(totalVoteCounts *100.0) / totalAccreditedVotes;
        return new DashboardResponse("00", "Dashboard loaded.", totalStates,
                totalLgas, totalSenatorialDistricts, totalRegisteredVotes, totalAccreditedVotes,
                totalVoteCounts, totalWards, totalPollingUnits,
                lgaWithResults,
                wardsWithResults,
                pollingUnitsWithResults,
                resultsReceived,
                partyResults, lgaResults,partyLgaResults
        );
    }
*/
    private List<PartyResult> getPartyResults(int totalVoteCounts){

        List<PartyResult> partyResults = new ArrayList<>(3);
        int party1Votes = resultRealTimeRepository.findAll()
                .stream().map(ResultRealTime::getParty_1).mapToInt(Integer::intValue)
                .sum();

        int party2Votes = resultRealTimeRepository.findAll()
                .stream().map(ResultRealTime::getParty_2).mapToInt(Integer::intValue)
                .sum();
        int party3Votes = resultRealTimeRepository.findAll()
                .stream().map(ResultRealTime::getParty_3).mapToInt(Integer::intValue)
                .sum();
        int party4Votes = resultRealTimeRepository.findAll()
                .stream().map(ResultRealTime::getParty_4).mapToInt(Integer::intValue)
                .sum();
        PoliticalParty politicalParty1 = politicalPartyRepository.findByCode(FIRST_PARTY);
        PoliticalParty politicalParty2 = politicalPartyRepository.findByCode(SECOND_PARTY);
        PoliticalParty politicalParty3 = politicalPartyRepository.findByCode(THIRD_PARTY);
        PoliticalParty politicalParty4 = politicalPartyRepository.findByCode(FOURTH_PARTY);

        PartyResult partyResult1 = extractPartyResult(totalVoteCounts, party1Votes, politicalParty1);
        PartyResult partyResult2 = extractPartyResult(totalVoteCounts, party2Votes, politicalParty2);
        PartyResult partyResult3 = extractPartyResult(totalVoteCounts, party3Votes, politicalParty3);
        PartyResult partyResult4 = extractPartyResult(totalVoteCounts, party4Votes, politicalParty4);

        partyResults.add(partyResult1);
        partyResults.add(partyResult2);
        partyResults.add(partyResult3);
        partyResults.add(partyResult4);

        return partyResults;
    }

    private PartyResult extractPartyResult(int totalVoteCounts, int partyVotes, PoliticalParty politicalParty) {
        Double percent;
        PartyResult partyResult = new PartyResult();
        partyResult.setPoliticalParty(politicalParty);
        partyResult.setTotalVoteCount(partyVotes);
        percent = (partyVotes * 100.0) / totalVoteCounts;
        partyResult.setPercent(percent);
        return partyResult;
    }

    private Long lgaByState(State state){
        List<Lga> lga = lgaRepository.findByState(state);
        return (long) lga.size();
    }

    private Long lgaBySenatorialDisctrict(SenatorialDistrict senatorialDistrict){
        List<Lga> lga = lgaRepository.findBySenatorialDistrict(senatorialDistrict);
        return (long) lga.size();
    }


    private Long senatorialDistrictByState(State state){
        List<SenatorialDistrict> senatorialDistricts = senatorialDistrictRepository.findByState(state);
        return (long) senatorialDistricts.size();
    }

    private Integer getRegisteredVotersPerState(State state){
        List<ResultRealTime> results = resultRealTimeRepository.findAll();
        int totalResult = 0;

        totalResult += results.stream()
                .map(ResultRealTime::getRegisteredVotersCount).mapToInt(Integer::intValue).sum();

        return totalResult;
    }

    private Integer getRegisteredVotersPerSenatorialDistrict(SenatorialDistrict senatorialDistrict){
        List<ResultRealTime> results = resultRealTimeRepository.findBySenatorialDistrict(senatorialDistrict);
        int totalResult = 0;
        totalResult += results.stream()
            .map(ResultRealTime::getRegisteredVotersCount).mapToInt(Integer::intValue).sum();
        return totalResult;
    }

    private Integer getRegisteredVotersPerLga(Lga lga){
        List<ResultRealTime> results = resultRealTimeRepository.findByLga(lga);
        int totalResult = 0;
        totalResult += results.stream()
                .map(ResultRealTime::getRegisteredVotersCount).mapToInt(Integer::intValue).sum();
        return totalResult;
    }


    private Integer getAccreditedVotesPerState(State state){
        List<ResultRealTime> results = resultRealTimeRepository.findAll();
        int totalResult = 0;

        totalResult += results.stream()
                .map(ResultRealTime::getAccreditedVotersCount).mapToInt(Integer::intValue).sum();

        return totalResult;
    }

    private Integer getAccreditedVotesPerSenatorialDistrict(SenatorialDistrict senatorialDistrict){
        List<ResultRealTime> results = resultRealTimeRepository.findBySenatorialDistrict(senatorialDistrict);
        int totalResult = results.stream()
                    .map(ResultRealTime::getAccreditedVotersCount).mapToInt(Integer::intValue).sum();
        return totalResult;
    }

    private Integer getAccreditedVotesPerLga(Lga lga){
        List<ResultRealTime> results = resultRealTimeRepository.findByLga(lga);
        int totalResult = results.stream()
                .map(ResultRealTime::getAccreditedVotersCount).mapToInt(Integer::intValue).sum();
        return totalResult;
    }


    public Integer getWards(State state){
        return wardRepository.findByState(state).size();
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

    public Integer getWardsBySenatorialDistrict(SenatorialDistrict senatorialDistrict){
        List<Ward> wards = wardRepository.findAll();
        List<Lga> lgas = lgaRepository.findBySenatorialDistrict(senatorialDistrict);
        int totalResult = 0;

        if(lgas.size()>0){
            for (Lga lga:lgas) {
                totalResult += wards.stream()
                        .filter(ward ->  ward.getLga().getId().equals(lga.getId())).count();
            }
        }
        return totalResult;
    }

    public Integer getWardsByLga(Lga lga){
        List<Ward> wards = wardRepository.findAll();
        return (int) wards.stream()
                        .filter(ward ->  ward.getLga().getId().equals(lga.getId())).count();
    }

    public Integer getPollingUnitsByWard(State state){
        return pollingUnitRepository.findByState(state).size();
    }

    public Integer getPollingUnitsByWardBySenatorialDistrict(SenatorialDistrict senatorialDistrict){
        return pollingUnitRepository.findBySenatorialDistrict(senatorialDistrict).size();
    }

    public Integer getPollingUnitsByWardByLga(Lga lga){
        return pollingUnitRepository.findByLga(lga).size();
    }

    public Integer getVoteCountsByState(State state){
        List<ResultRealTime> resultRealTimeList = resultRealTimeRepository.findAll();
        return resultRealTimeList.stream()
                .map(ResultRealTime::getVoteCount)
                .mapToInt(Integer::intValue).sum();
    }

    public Integer getVoteCountsBySenatorialDistrict(SenatorialDistrict senatorialDistrict){
        List<ResultRealTime> resultRealTime = resultRealTimeRepository.findBySenatorialDistrict(senatorialDistrict);
        return resultRealTime.stream()
                .map(ResultRealTime::getVoteCount)
                .mapToInt(Integer::intValue).sum();
    }

    public Integer getVoteCountsByLga(Lga lga){
        List<ResultRealTime> resultRealTime = resultRealTimeRepository.findByLga(lga);
        return resultRealTime.stream()
            .map(ResultRealTime::getVoteCount)
            .mapToInt(Integer::intValue).sum();
    }

    public Long getStatePollingUnitsWithResult(State state){
        List<ResultRealTime> resultRealTime = resultRealTimeRepository.findAll();
        return pollingUnitCount(resultRealTime);
    }

    public Long getSenatorialDistrictPollingUnitsWithResult(SenatorialDistrict senatorialDistrict){
        List<ResultRealTime> resultRealTime = resultRealTimeRepository.findBySenatorialDistrict(senatorialDistrict);
        return pollingUnitCount(resultRealTime);
    }

    private Long pollingUnitCount(List<ResultRealTime> resultRealTime) {
        int pollingUnits = resultRealTime.stream()
                .map(ResultRealTime::getPollingUnitCount)
                .mapToInt(Integer::intValue).sum();
        return (long) pollingUnits;
    }

    private Long wardCount(List<ResultRealTime> resultRealTimeList) {
        HashSet wards = new HashSet();
        resultRealTimeList.stream()
                .forEach(resultRealTime -> wards.add(resultRealTime.getWard().getId()));
        return (long) wards.size();
    }

    private Long lgaCount(List<ResultRealTime> resultRealTimeList) {
        HashSet lgas = new HashSet();
        resultRealTimeList.stream()
                .forEach(resultRealTime -> lgas.add(resultRealTime.getLga().getId()));
        return (long) lgas.size();
    }

    private HashSet<ResultRealTime> lgasWithResult(List<ResultRealTime> resultRealTimeList) {
        HashSet lgas = new HashSet();
        resultRealTimeList.stream()
                .forEach(resultRealTime -> lgas.add(resultRealTime.getLga().getId()));
        return lgas;
    }

    public Long getLgaPollingUnitsWithResult(Lga lga){
        List<ResultRealTime> resultRealTime = resultRealTimeRepository.findByLga(lga);
        return pollingUnitCount(resultRealTime);
    }

    public Long getStateWardsWithResult(State state){
        List<ResultRealTime> resultRealTime = resultRealTimeRepository.findAll();
        return wardCount(resultRealTime);
    }

    public Long getSenatorialDistrictWardsWithResult(SenatorialDistrict senatorialDistrict){
        List<ResultRealTime> resultRealTime = resultRealTimeRepository.findBySenatorialDistrict(senatorialDistrict);
        return wardCount(resultRealTime);
    }

    public Long getWardsWithResult(Lga lga){
        List<ResultRealTime> resultRealTime = resultRealTimeRepository.findByLga(lga);
        return wardCount(resultRealTime);
    }

    public Long getStateLgasWithResult(State state){
        List<ResultRealTime> resultRealTime = resultRealTimeRepository.findAll();
        return lgaCount(resultRealTime);
    }

    public Long getSenatorialDistrcitLgasWithResult(SenatorialDistrict senatorialDistrict){
        List<ResultRealTime> resultRealTime = resultRealTimeRepository.findBySenatorialDistrict(senatorialDistrict);
        return lgaCount(resultRealTime);
    }

    public Long getLgasWithResult(Lga lga){
        List<ResultRealTime> resultRealTime = resultRealTimeRepository.findByLga(lga);
        return lgaCount(resultRealTime);
    }

    private State getState(Long id) throws NotFoundException {
        Optional<State> state = stateRepository.findById(id);
        if(!state.isPresent()){
            throw new NotFoundException("State not found.");
        }
        return state.get();
    }

    private SenatorialDistrict getSenatorialDistrict(Long id) throws NotFoundException {
        Optional<SenatorialDistrict> senatorialDistrict = senatorialDistrictRepository.findById(id);
        if(!senatorialDistrict.isPresent()){
            throw new NotFoundException("State not found.");
        }
        return senatorialDistrict.get();
    }

    private Lga getLga(Long id) throws NotFoundException {
        Optional<Lga> lga = lgaRepository.findById(id);
        if(!lga.isPresent()){
            throw new NotFoundException("Lga not found.");
        }
        return lga.get();
    }

    @Override
    public DashboardResponse getDashboardBySenatorialDistrict(Long senatorialDistrictId) throws NotFoundException {

        SenatorialDistrict senatorialDistrict = getSenatorialDistrict(senatorialDistrictId);

        Long totalStates = getTotalStates();
        Long totalLgas = lgaBySenatorialDisctrict(senatorialDistrict);
        Long totalSenatorialDistricts = 1L;
        Integer totalRegisteredVotes = getRegisteredVotersPerSenatorialDistrict(senatorialDistrict);
        Integer totalAccreditedVotes =  getAccreditedVotesPerSenatorialDistrict(senatorialDistrict);
        Long totalWards  = Long.valueOf(getWardsBySenatorialDistrict(senatorialDistrict));
        Long totalPollingUnits = Long.valueOf(getPollingUnitsByWardBySenatorialDistrict(senatorialDistrict));
        Integer totalVoteCounts = getVoteCountsBySenatorialDistrict(senatorialDistrict);
        Long lgaWithResults = getSenatorialDistrcitLgasWithResult(senatorialDistrict);
        Long wardsWithResults = getSenatorialDistrictWardsWithResult(senatorialDistrict);
        Long pollingUnitsWithResults = getSenatorialDistrictPollingUnitsWithResult(senatorialDistrict);

        Double resultsReceived = (totalVoteCounts *100.0) / totalAccreditedVotes;

        List<PoliticalParty> politicalParties = politicalPartyRepository.findAll();
        List<PartyResult> partyResults = new ArrayList<>();
        politicalParties
                .forEach(politicalParty -> {

                    List<Result> results = resultRepository.findAll();
                    AtomicReference<Integer> voteCount = new AtomicReference<>(0);
                    results.stream()
                            .filter(result -> result.getSenatorialDistrict().getId().equals(senatorialDistrict.getId()))
                            .map(Result::getResultPerParties).forEach(resultPerParties -> {
                        for (ResultPerParty resultPerparty: resultPerParties) {
                            if(resultPerparty.getPoliticalParty().getId().equals(politicalParty.getId())){
                                voteCount.updateAndGet(v -> v + resultPerparty.getVoteCount());
                            }
                        }
                    });
                    PartyResult partyResult = new PartyResult();
                    partyResult.setPoliticalParty(politicalParty);
                    partyResult.setTotalVoteCount(voteCount.get());
                    Double percent = (voteCount.get() * 100.0) / totalVoteCounts;
                    partyResult.setPercent(percent);
                    partyResults.add(partyResult);
                });
        partyResults.sort(Comparator.comparingInt(PartyResult::getTotalVoteCount));
        return new DashboardResponse("00", "Dashboard loaded for Senatorial District.", totalStates,
                totalLgas, totalSenatorialDistricts, totalRegisteredVotes, totalAccreditedVotes,
                totalVoteCounts, totalWards, totalPollingUnits,
                lgaWithResults,
                wardsWithResults,
                pollingUnitsWithResults,
                resultsReceived,
                partyResults
        );
    }


    public DashboardResponse getDashboardByLga(Long lgaId) throws NotFoundException {

        Lga lga = getLga(lgaId);

        Long totalStates = getTotalStates();
        Long totalLgas = getTotalLgas();
        Long totalSenatorialDistricts = 1L;
        Integer totalRegisteredVotes = getRegisteredVotersPerLga(lga);
        Integer totalAccreditedVotes =  getAccreditedVotesPerLga(lga);
        Long totalWards  = Long.valueOf(getWardsByLga(lga));
        Long totalPollingUnits = Long.valueOf(getPollingUnitsByWardByLga(lga));
        Integer totalVoteCounts = getVoteCountsByLga(lga);
        Long lgaWithResults = getLgasWithResult(lga);
        Long wardsWithResults = getWardsWithResult(lga);
        Long pollingUnitsWithResults = getLgaPollingUnitsWithResult(lga);

        Double resultsReceived = (totalVoteCounts *100.0) / totalAccreditedVotes;

        List<PoliticalParty> politicalParties = politicalPartyRepository.findAll();
        List<PartyResult> partyResults = new ArrayList<>();
        politicalParties
                .forEach(politicalParty -> {

                    List<Result> results = resultRepository.findAll();
                    AtomicReference<Integer> voteCount = new AtomicReference<>(0);
                    results.stream()
                            .filter(result -> result.getLga().getId().equals(lga.getId()))
                            .map(Result::getResultPerParties).forEach(resultPerParties -> {
                        for (ResultPerParty resultPerparty: resultPerParties) {
                            if(resultPerparty.getPoliticalParty().getId().equals(politicalParty.getId())){
                                voteCount.updateAndGet(v -> v + resultPerparty.getVoteCount());
                            }
                        }
                    });
                    PartyResult partyResult = new PartyResult();
                    partyResult.setPoliticalParty(politicalParty);
                    partyResult.setTotalVoteCount(voteCount.get());
                    Double percent = (voteCount.get() * 100.0) / totalVoteCounts;
                    partyResult.setPercent(percent);
                    partyResults.add(partyResult);
                });
        partyResults.sort(Comparator.comparingInt(PartyResult::getTotalVoteCount));
        return new DashboardResponse("00", "Dashboard loaded for LGA.", totalStates,
                totalLgas, totalSenatorialDistricts, totalRegisteredVotes, totalAccreditedVotes,
                totalVoteCounts, totalWards, totalPollingUnits,
                lgaWithResults,
                wardsWithResults,
                pollingUnitsWithResults,
                resultsReceived,partyResults

        );
    }

    public LgaResult getLgaResult(Long lgaId) throws NotFoundException {

        Lga lga = getLga(lgaId);
        Integer totalAccreditedVotes =  getAccreditedVotesPerLga(lga);

        Integer totalVoteCounts = getVoteCountsByLga(lga);
        List<PoliticalParty> politicalParties = politicalPartyRepository.findAll();
        List<PartyResult> partyResults = new ArrayList<>();
        politicalParties
                .forEach(politicalParty -> {

                    List<Result> results = resultRepository.findAll();
                    AtomicReference<Integer> voteCount = new AtomicReference<>(0);
                    results.stream()
                            .filter(result -> result.getLga().getId().equals(lga.getId()))
                            .map(Result::getResultPerParties).forEach(resultPerParties -> {
                        for (ResultPerParty resultPerparty: resultPerParties) {
                            if(resultPerparty.getPoliticalParty().getId().equals(politicalParty.getId())){
                                voteCount.updateAndGet(v -> v + resultPerparty.getVoteCount());
                            }
                        }
                    });
                    PartyResult partyResult = new PartyResult();
                    partyResult.setPoliticalParty(politicalParty);
                    partyResult.setTotalVoteCount(voteCount.get());
                    Double percent = (voteCount.get() * 100.0) / totalVoteCounts;
                    partyResult.setPercent(percent);
                    partyResults.add(partyResult);
                });
        partyResults.sort(Comparator.comparingInt(PartyResult::getTotalVoteCount));
        return new LgaResult(lga, partyResults);
    }

//    public Integer getPartyLgaResults(String party){
//        HashSet<String> lgasWon = new HashSet<>();
//        List<TopResult> topResults = topResults();
//        topResults.forEach(topResult -> {
//            if(topResult.getPoliticalParty().getName().equals(party)){
//                lgasWon.add(topResult.getLgaCode());
//            }
//        });
//
//        return lgasWon.size();
//    }
//
//    public List<TopResult> topResults(){
//        List<TopResult> topResults = new ArrayList<>();
//        List<Result> results = resultRepository.findAll();
//        results.stream().map(Result::getResultPerParties)
//                    .forEach(resultPerParties -> {
//                        List<ResultPerParty> rpp = new ArrayList<>(resultPerParties);
//                        rpp.sort(new SortByVotes());
//                        PoliticalParty politicalParty1 = rpp.get(0).getPoliticalParty();
//                        TopResult topResult = new TopResult();
//                        topResult.setPoliticalParty(rpp.get(0).getPoliticalParty());
//                        topResult.setLgaCode(rpp.get(0).getResult().getLga().getCode());
//                    });
//        return topResults;
//    }

    private class SortByVotes implements Comparator<ResultPerParty> {

        @Override
        public int compare(ResultPerParty o1, ResultPerParty o2) {
            return o2.getVoteCount()-o1.getVoteCount();
        }
    }
}
