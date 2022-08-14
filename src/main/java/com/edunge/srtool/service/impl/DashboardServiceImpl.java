package com.edunge.srtool.service.impl;

import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.*;
import com.edunge.srtool.repository.*;
import com.edunge.srtool.response.DashboardResponse;
import com.edunge.srtool.response.NationalDashboardResponse;
import com.edunge.srtool.response.PartyResult;
import com.edunge.srtool.service.DashboardService;
import com.edunge.srtool.service.GeoPoliticalZoneService;
import com.edunge.srtool.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final StateService stateService;
    private final GeoPoliticalZoneService geoPoliticalZoneService;
    private final LgaServiceImpl lgaService;
    private final SenatorialDistrictServiceImpl senatorialDistrictService;
    private  final WardServiceImpl wardService;
    private final PollingUnitServiceImpl pollingUnitService;
    private final PoliticalPartyServiceImpl politicalPartyService;
    private final ResultServiceImpl resultService;
    private final ResultRealTimeRepository resultRealTimeRepository;
    private final ResultRepository resultRepository;
    private static final String FIRST_PARTY = "party_1";
    private static final String SECOND_PARTY = "party_2";
    private static final String THIRD_PARTY = "party_3";
    private static final String FOURTH_PARTY = "party_4";
    private static final String FIFTH_PARTY = "party_5";
    private static final String SIXTH_PARTY = "party_6";

    private static final String VOTING_LEVEL_LGA = "LGA";
    private static final String VOTING_LEVEL_WARD = "Ward";

    @Autowired
    public DashboardServiceImpl(GeoPoliticalZoneService geoPoliticalZoneService, StateService stateService, LgaServiceImpl lgaService,
                                SenatorialDistrictServiceImpl senatorialDistrictService,
                                WardServiceImpl wardService, PollingUnitServiceImpl pollingUnitService,
                                PoliticalPartyServiceImpl politicalPartyService, ResultServiceImpl resultService,
                                ResultRealTimeRepository resultRealTimeRepository, ResultRepository resultRepository) {
        this.stateService = stateService;
        this.geoPoliticalZoneService = geoPoliticalZoneService;
        this.lgaService = lgaService;
        this.senatorialDistrictService = senatorialDistrictService;
        this.wardService = wardService;
        this.pollingUnitService = pollingUnitService;
        this.politicalPartyService = politicalPartyService;
        this.resultService = resultService;
        this.resultRealTimeRepository = resultRealTimeRepository;
        this.resultRepository = resultRepository;
    }

    private Long getTotalStates(){
        return stateService.countState();
    }
    private Long getTotalLgas(long stateId){
        return lgaService.countLgaByStateCode(stateId);
    }
    private Long getSenatorialDistrics(State state){
        return senatorialDistrictService.countSenatorialDistrictByState(state);
    }
    private Long getPollingUnits(State state){
        return pollingUnitService.countByState(state);
    }

    private Integer getTotalRegisteredVotes(List<ResultRealTime> results){
        return results.stream().map(ResultRealTime::getRegisteredVotersCount).mapToInt(Integer::intValue).sum();
    }

    private Integer getTotalAccreditedVotes(List<Result> results){
        return results.stream().map(Result::getAccreditedVotersCount).mapToInt(Integer::intValue).sum();
    }

    private Integer getTotalVotes(List<ResultRealTime> resultRealTimeList){
        return resultRealTimeList.stream().map(ResultRealTime::getVoteCount).mapToInt(Integer::intValue).sum();
    }

    private Integer getTotalVotes(Election election, Long electionType){
        return resultRealTimeRepository.findSumVoteCount(election, electionType).intValue();
    }
    private Integer getTotalVotesZone(Election election, GeoPoliticalZone geoPoliticalZone, Long electionType){
        return resultRealTimeRepository.findSumVoteCountZone(election, geoPoliticalZone, electionType).intValue();
    }

    public DashboardResponse getDefaultDashboard(Long electionType) throws NotFoundException{
        long totalStates = 36l;
        State state = getDefaultState();
        Long totalLgas = getTotalLgas(state.getId());
        Long totalSenatorialDistricts = getSenatorialDistrics(state);
        List<ResultRealTime> results = resultRealTimeRepository.findByStateIdAndElectionTypeAndElection(state.getId(), electionType, resultService.getElection());
        Integer totalRegisteredVotes = getTotalRegisteredVotes(results);
        Integer totalVoteCounts = getTotalVotes(results);
        Integer totalAccreditedVotes =  getAccreditedVotes(results);
        Long totalWards  = getWards(state).longValue();
        Long totalPollingUnits = getPollingUnits(state);
//        ((Count of Polling G + Total number Polling units in Ward C + Total number of polling units in LGA A) / Total number of polling units in the system) * 100
        Double resultsReceived = (totalVoteCounts *100.0) / totalAccreditedVotes;
        return new DashboardResponse("00", "Dashboard loaded", totalStates, totalLgas, totalSenatorialDistricts, totalRegisteredVotes, totalAccreditedVotes, totalVoteCounts, totalWards, totalPollingUnits, resultsReceived);
    }

    private ResultRealTime resultSummary(List<ResultRealTime> resultRealTime){

        int party1Sum;
        int party2Sum;
        int party3Sum;
        int party4Sum;
        int party5Sum;
        int party6Sum;
        int voteCount;
        int totalPU;
        party1Sum = resultRealTime.stream().mapToInt(ResultRealTime::getParty_1).sum();
        party2Sum = resultRealTime.stream().mapToInt(ResultRealTime::getParty_2).sum();
        party3Sum = resultRealTime.stream().mapToInt(ResultRealTime::getParty_3).sum();
        party4Sum = resultRealTime.stream().mapToInt(ResultRealTime::getParty_4).sum();
        party5Sum = resultRealTime.stream().mapToInt(ResultRealTime::getParty_5).sum();
        party6Sum = resultRealTime.stream().mapToInt(ResultRealTime::getParty_6).sum();
        voteCount = resultRealTime.stream().mapToInt(ResultRealTime::getVoteCount).sum();
        totalPU = resultRealTime.stream().mapToInt(ResultRealTime::getPollingUnitCount).sum();
        ResultRealTime resultRealTime1 = new ResultRealTime();
        resultRealTime1.setParty_1(party1Sum);
        resultRealTime1.setParty_2(party2Sum);
        resultRealTime1.setParty_3(party3Sum);
        resultRealTime1.setParty_4(party4Sum);
        resultRealTime1.setParty_5(party5Sum);
        resultRealTime1.setParty_6(party6Sum);
        resultRealTime1.setVoteCount(voteCount);
        resultRealTime1.setPollingUnitCount(totalPU);
        return resultRealTime1;
    }

    private List<ResultRealTime> resultSummaryGroupByLga(List<ResultRealTime> resultRealTime, State state) throws NotFoundException{
        List<Lga> lgas = getLgaByState(state);
        List<ResultRealTime> results = new ArrayList<>();
        lgas.forEach(lga->{
            ResultRealTime resultSummary = resultSummaryForLga(resultRealTime, lga);
            if(resultSummary != null) {
                ResultRealTime resultRealTime1 = new ResultRealTime();
                resultRealTime1.setParty_1(resultSummary.getParty_1());
                resultRealTime1.setLga(lga);
                resultRealTime1.setParty_2(resultSummary.getParty_2());
                resultRealTime1.setParty_3(resultSummary.getParty_3());
                resultRealTime1.setParty_4(resultSummary.getParty_4());
                resultRealTime1.setParty_5(resultSummary.getParty_5());
                resultRealTime1.setParty_6(resultSummary.getParty_6());
                resultRealTime1.setVoteCount(resultSummary.getVoteCount());
                resultRealTime1.setPollingUnitCount(resultSummary.getPollingUnitCount());
                results.add(resultRealTime1);
            }
        });
        return results;
    }

    private List<ResultRealTime> resultSummaryGroupByState(List<ResultRealTime> resultRealTime, List<State> states) throws NotFoundException{
        List<ResultRealTime> results = new ArrayList<>();
        states.forEach(state->{
            ResultRealTime resultSummary = resultSummaryForState(resultRealTime, state);
            if(resultSummary != null) {
                ResultRealTime resultRealTime1 = new ResultRealTime();
                resultRealTime1.setParty_1(resultSummary.getParty_1());
                resultRealTime1.setStateId(state.getId());
                resultRealTime1.setParty_2(resultSummary.getParty_2());
                resultRealTime1.setParty_3(resultSummary.getParty_3());
                resultRealTime1.setParty_4(resultSummary.getParty_4());
                resultRealTime1.setParty_5(resultSummary.getParty_5());
                resultRealTime1.setParty_6(resultSummary.getParty_6());
                resultRealTime1.setVoteCount(resultSummary.getVoteCount());
                resultRealTime1.setPollingUnitCount(resultSummary.getPollingUnitCount());
                results.add(resultRealTime1);
            }
        });
        return results;
    }

    private List<ResultRealTime> resultSummaryGroupByLga(List<ResultRealTime> resultRealTime, List<Lga> lgas) throws NotFoundException{
        List<ResultRealTime> results = new ArrayList<>();
        lgas.forEach(lga->{
            ResultRealTime resultSummary = resultSummaryForLga(resultRealTime, lga);
            if(resultSummary != null) {
                ResultRealTime resultRealTime1 = new ResultRealTime();
                resultRealTime1.setParty_1(resultSummary.getParty_1());
                resultRealTime1.setLga(lga);
                resultRealTime1.setParty_2(resultSummary.getParty_2());
                resultRealTime1.setParty_3(resultSummary.getParty_3());
                resultRealTime1.setParty_4(resultSummary.getParty_4());
                resultRealTime1.setParty_5(resultSummary.getParty_5());
                resultRealTime1.setParty_6(resultSummary.getParty_6());
                resultRealTime1.setVoteCount(resultSummary.getVoteCount());
                resultRealTime1.setPollingUnitCount(resultSummary.getPollingUnitCount());
                results.add(resultRealTime1);
            }
        });
        return results;
    }

    private List<ResultRealTime> resultSummaryGroupByState(List<ResultRealTime> resultRealTime) throws NotFoundException{
        List<ResultRealTime> results = new ArrayList<>();
        stateService.findAll().getStates().forEach(state -> {
            try {
                results.addAll(resultSummaryGroupByLga(resultRealTime, state));
            } catch (NotFoundException e) {
            }
        });
        return results;
    }

    private List<ResultRealTime> resultSummaryForDistrictGroupByLga(List<ResultRealTime> resultRealTime, SenatorialDistrict senatorialDistrict) throws NotFoundException{
        List<Lga> lgas = lgaService.findLgaBySenatorialDistrictCode(senatorialDistrict.getId()).getLgas();
        List<ResultRealTime> results = new ArrayList<>();
        lgas.forEach(lga->{
            ResultRealTime resultSummary = resultSummaryForLga(resultRealTime, lga);
            if(resultSummary != null) {
                ResultRealTime resultRealTime1 = new ResultRealTime();
                resultRealTime1.setParty_1(resultSummary.getParty_1());
                resultRealTime1.setLga(lga);
                resultRealTime1.setParty_2(resultSummary.getParty_2());
                resultRealTime1.setParty_3(resultSummary.getParty_3());
                resultRealTime1.setParty_4(resultSummary.getParty_4());
                resultRealTime1.setParty_5(resultSummary.getParty_5());
                resultRealTime1.setParty_6(resultSummary.getParty_6());
                resultRealTime1.setVoteCount(resultSummary.getVoteCount());
                resultRealTime1.setPollingUnitCount(resultSummary.getPollingUnitCount());

                results.add(resultRealTime1);
            }
        });
        return results;
    }

    private ResultRealTime resultSummaryForState(List<ResultRealTime> resultRealTime, State state){
        AtomicInteger party1Sum= new AtomicInteger();
        AtomicInteger party2Sum = new AtomicInteger();
        AtomicInteger party3Sum= new AtomicInteger();
        AtomicInteger party4Sum= new AtomicInteger();
        AtomicInteger party5Sum= new AtomicInteger();
        AtomicInteger party6Sum= new AtomicInteger();
        AtomicInteger voteCount= new AtomicInteger();
        AtomicInteger totalPU= new AtomicInteger();

        party1Sum.set(0);
        party2Sum.set(0);
        party3Sum.set(0);
        party4Sum.set(0);
        party5Sum.set(0);
        party6Sum.set(0);
        voteCount.set(0);
        totalPU.set(0);
        resultRealTime.stream().filter(result->result.getStateId().equals(state.getId()))
                .forEach(result -> {
                party1Sum.addAndGet(result.getParty_1());
                party2Sum.addAndGet(result.getParty_2());
                party3Sum.addAndGet(result.getParty_3());
                party4Sum.addAndGet(result.getParty_4());
                party5Sum.addAndGet(result.getParty_4());
                party6Sum.addAndGet(result.getParty_4());
                voteCount.addAndGet(result.getVoteCount());
                totalPU.addAndGet(result.getPollingUnitCount());
            });
        if(voteCount.get()>0) {
            ResultRealTime resultRealTime1 = new ResultRealTime();
            resultRealTime1.setParty_1(party1Sum.get());
            resultRealTime1.setStateId(state.getId());
            resultRealTime1.setParty_2(party2Sum.get());
            resultRealTime1.setParty_3(party3Sum.get());
            resultRealTime1.setParty_4(party4Sum.get());
            resultRealTime1.setParty_5(party5Sum.get());
            resultRealTime1.setParty_6(party6Sum.get());
            resultRealTime1.setVoteCount(voteCount.get());
            resultRealTime1.setPollingUnitCount(totalPU.get());
            return resultRealTime1;
        }
        return null;
    }

    private ResultRealTime resultSummaryForLga(List<ResultRealTime> resultRealTime, Lga lga){
        AtomicInteger party1Sum= new AtomicInteger();
        AtomicInteger party2Sum = new AtomicInteger();
        AtomicInteger party3Sum= new AtomicInteger();
        AtomicInteger party4Sum= new AtomicInteger();
        AtomicInteger party5Sum= new AtomicInteger();
        AtomicInteger party6Sum= new AtomicInteger();
        AtomicInteger voteCount= new AtomicInteger();
        AtomicInteger totalPU= new AtomicInteger();

        party1Sum.set(0);
        party2Sum.set(0);
        party3Sum.set(0);
        party4Sum.set(0);
        party5Sum.set(0);
        party6Sum.set(0);
        voteCount.set(0);
        totalPU.set(0);
        resultRealTime.stream().filter(result->result.getLga().equals(lga))
                .forEach(result -> {
                party1Sum.addAndGet(result.getParty_1());
                party2Sum.addAndGet(result.getParty_2());
                party3Sum.addAndGet(result.getParty_3());
                party4Sum.addAndGet(result.getParty_4());
                party5Sum.addAndGet(result.getParty_4());
                party6Sum.addAndGet(result.getParty_4());
                voteCount.addAndGet(result.getVoteCount());
                totalPU.addAndGet(result.getPollingUnitCount());
            });
        if(voteCount.get()>0) {
            ResultRealTime resultRealTime1 = new ResultRealTime();
            resultRealTime1.setParty_1(party1Sum.get());
            resultRealTime1.setLga(lga);
            resultRealTime1.setParty_2(party2Sum.get());
            resultRealTime1.setParty_3(party3Sum.get());
            resultRealTime1.setParty_4(party4Sum.get());
            resultRealTime1.setParty_5(party5Sum.get());
            resultRealTime1.setParty_6(party6Sum.get());
            resultRealTime1.setVoteCount(voteCount.get());
            resultRealTime1.setPollingUnitCount(totalPU.get());
            return resultRealTime1;
        }
        return null;
    }

    private StateResult getResultsGroupByState(List<ResultRealTime> resultRealTime, State state) throws NotFoundException{
        ResultRealTime result = resultSummaryForState(resultRealTime, state);

        int party1Sum;
        int party2Sum;
        int party3Sum;
        int party4Sum;
        int party5Sum;
        int party6Sum;
        int voteCount;

        if(result != null) {
            party1Sum = result.getParty_1();
            party2Sum = result.getParty_2();
            party3Sum = result.getParty_3();
            party4Sum = result.getParty_4();
            party5Sum = result.getParty_5();
            party6Sum = result.getParty_6();
            voteCount = result.getVoteCount();

            if (voteCount > 0) {
                int totalVoteCounts = party1Sum + party2Sum + party3Sum + party4Sum + party5Sum + party6Sum;
                List<PartyResult> partyResults = new ArrayList<>();

                // party 1
                PoliticalParty party1 = politicalPartyService.findPoliticalPartyByCodeAndDefaultState(FIRST_PARTY, state).getPoliticalParty();
                partyResults.add(this.extractPartyResult(totalVoteCounts, party1Sum, party1));

                // party 2
                PoliticalParty party2 = politicalPartyService.findPoliticalPartyByCodeAndDefaultState(SECOND_PARTY, state).getPoliticalParty();
                partyResults.add(this.extractPartyResult(totalVoteCounts, party2Sum, party2));

                // party 3
                PoliticalParty party3 = politicalPartyService.findPoliticalPartyByCodeAndDefaultState(THIRD_PARTY, state).getPoliticalParty();
                partyResults.add(this.extractPartyResult(totalVoteCounts, party3Sum, party3));

                // party 4
                PoliticalParty party4 = politicalPartyService.findPoliticalPartyByCodeAndDefaultState(FOURTH_PARTY, state).getPoliticalParty();
                partyResults.add(this.extractPartyResult(totalVoteCounts, party4Sum, party4));

                // party 5
                PoliticalParty party5 = politicalPartyService.findPoliticalPartyByCodeAndDefaultState(FIFTH_PARTY, state).getPoliticalParty();
                partyResults.add(this.extractPartyResult(totalVoteCounts, party5Sum, party5));
// party 4
                PoliticalParty party6 = politicalPartyService.findPoliticalPartyByCodeAndDefaultState(SIXTH_PARTY, state).getPoliticalParty();
                partyResults.add(this.extractPartyResult(totalVoteCounts, party6Sum, party6));

                partyResults.sort(Comparator.comparingInt(PartyResult::getTotalVoteCount));
                return new StateResult(state, partyResults);
            }
        }

        return null;
    }

    private LgaResult getResultsGroupByLga(List<ResultRealTime> resultRealTime, Lga lga) throws NotFoundException{
        ResultRealTime result = resultSummaryForLga(resultRealTime, lga);
        State state = lga.getState();

        int party1Sum;
        int party2Sum;
        int party3Sum;
        int party4Sum;
        int party5Sum;
        int party6Sum;
        int voteCount;

        if(result != null) {
            party1Sum = result.getParty_1();
            party2Sum = result.getParty_2();
            party3Sum = result.getParty_3();
            party4Sum = result.getParty_4();
            party5Sum = result.getParty_5();
            party6Sum = result.getParty_6();
            voteCount = result.getVoteCount();

            if (voteCount > 0) {
                int totalVoteCounts = party1Sum + party2Sum + party3Sum + party4Sum + party5Sum + party6Sum;
                List<PartyResult> partyResults = new ArrayList<>();

                // party 1
                PoliticalParty party1 = politicalPartyService.findPoliticalPartyByCodeAndDefaultState(FIRST_PARTY, state).getPoliticalParty();
                partyResults.add(this.extractPartyResult(totalVoteCounts, party1Sum, party1));

                // party 2
                PoliticalParty party2 = politicalPartyService.findPoliticalPartyByCodeAndDefaultState(SECOND_PARTY, state).getPoliticalParty();
                partyResults.add(this.extractPartyResult(totalVoteCounts, party2Sum, party2));

                // party 3
                PoliticalParty party3 = politicalPartyService.findPoliticalPartyByCodeAndDefaultState(THIRD_PARTY, state).getPoliticalParty();
                partyResults.add(this.extractPartyResult(totalVoteCounts, party3Sum, party3));

                // party 4
                PoliticalParty party4 = politicalPartyService.findPoliticalPartyByCodeAndDefaultState(FOURTH_PARTY, state).getPoliticalParty();
                partyResults.add(this.extractPartyResult(totalVoteCounts, party4Sum, party4));

                // party 5
                PoliticalParty party5 = politicalPartyService.findPoliticalPartyByCodeAndDefaultState(FIFTH_PARTY, state).getPoliticalParty();
                partyResults.add(this.extractPartyResult(totalVoteCounts, party5Sum, party5));
// party 4
                PoliticalParty party6 = politicalPartyService.findPoliticalPartyByCodeAndDefaultState(SIXTH_PARTY, state).getPoliticalParty();
                partyResults.add(this.extractPartyResult(totalVoteCounts, party6Sum, party6));

                partyResults.sort(Comparator.comparingInt(PartyResult::getTotalVoteCount));
                return new LgaResult(lga, partyResults);
            }
        }

        return null;
    }

    private List<LgaResult> getResultsGroupByLgaSenatorialDistrict(List<ResultRealTime> resultRealTime, SenatorialDistrict senatorialDistrict) throws NotFoundException{
        List<LgaResult> lgaResults = new ArrayList<>();
        List<ResultRealTime> resultRealTime1 = resultSummaryForDistrictGroupByLga(resultRealTime, senatorialDistrict);

        List<Lga> lgas = lgaService.findLgaBySenatorialDistrictCode(senatorialDistrict.getId()).getLgas();

        lgas.forEach(lga -> {
            LgaResult lgaResult = null;
            try {
                lgaResult = this.getResultsGroupByLga(resultRealTime1, lga);
            } catch (NotFoundException e) {}
            if(lgaResult != null) {
                lgaResults.add(lgaResult);
            }
        });

        return lgaResults;
    }

    private List<LgaResult> getResultsGroupByLgaState(List<ResultRealTime> resultRealTime, State state) throws NotFoundException{
        List<LgaResult> lgaResults = new ArrayList<>();
        List<Lga> lgas = lgaService.findLgaByStateCode(state.getId()).getLgas();
        List<ResultRealTime> resultRealTime1 = resultSummaryGroupByLga(resultRealTime, lgas);

        lgas.forEach(lga -> {
            LgaResult lgaResult = null;
            try {
                lgaResult = this.getResultsGroupByLga(resultRealTime1, lga);
            } catch (NotFoundException e) {
            }
            if(lgaResult != null) {
                lgaResults.add(lgaResult);
            }
        });

        return lgaResults;
    }

    private List<StateResult> getResultsGroupByState(List<ResultRealTime> resultRealTime) throws NotFoundException{
        List<StateResult> stateResults = new ArrayList<>();

        List<State> states = stateService.findAll().getStates();
        List<ResultRealTime> resultRealTime1 = resultSummaryGroupByState(resultRealTime, states);

        states.forEach(state -> {
            StateResult stateResult = null;
            try {
                stateResult = this.getResultsGroupByState(resultRealTime1, state);
            } catch (NotFoundException e) {
            }
            if(stateResult != null) {
                stateResults.add(stateResult);
            }
        });

        return stateResults;
    }

    private List<LgaResult> getResultsGroupByLga(List<ResultRealTime> resultRealTime) throws NotFoundException{
        List<LgaResult> lgaResults = new ArrayList<>();

        List<Lga> lgas = lgaService.findAll().getLgas();
        List<ResultRealTime> resultRealTime1 = resultSummaryGroupByLga(resultRealTime, lgas);

        lgas.forEach(lga -> {
            LgaResult lgaResult = null;
            try {
                lgaResult = this.getResultsGroupByLga(resultRealTime1, lga);
            } catch (NotFoundException e) {
            }
            if(lgaResult != null) {
                lgaResults.add(lgaResult);
            }
        });

        return lgaResults;
    }

    private List<PartyResult> processPartyResults(ResultRealTime resultSummary, State state){
        List<PartyResult> partyResults = new ArrayList<>();

        int totalVoteCounts = resultSummary.getParty_1() + resultSummary.getParty_2() +resultSummary.getParty_3()+resultSummary.getParty_4()+resultSummary.getParty_5()+resultSummary.getParty_6();
        // party 1
        PoliticalParty party1 = politicalPartyService.findPoliticalPartyByCodeAndDefaultState(FIRST_PARTY, state).getPoliticalParty();
        partyResults.add(this.extractPartyResult(totalVoteCounts, resultSummary.getParty_1(), party1));

        // party 2
        PoliticalParty party2 = politicalPartyService.findPoliticalPartyByCodeAndDefaultState(SECOND_PARTY, state).getPoliticalParty();
        partyResults.add(this.extractPartyResult(totalVoteCounts, resultSummary.getParty_2(), party2));

        // party 3
        PoliticalParty party3 = politicalPartyService.findPoliticalPartyByCodeAndDefaultState(THIRD_PARTY, state).getPoliticalParty();;
        partyResults.add(this.extractPartyResult(totalVoteCounts, resultSummary.getParty_3(), party3));

        // party 4
        PoliticalParty party4 = politicalPartyService.findPoliticalPartyByCodeAndDefaultState(FOURTH_PARTY, state).getPoliticalParty();;
        partyResults.add(this.extractPartyResult(totalVoteCounts, resultSummary.getParty_4(), party4));

        // party 5
        PoliticalParty party5 = politicalPartyService.findPoliticalPartyByCodeAndDefaultState(FIFTH_PARTY, state).getPoliticalParty();;
        partyResults.add(this.extractPartyResult(totalVoteCounts, resultSummary.getParty_5(), party5));

        // party 6
        PoliticalParty party6 = politicalPartyService.findPoliticalPartyByCodeAndDefaultState(SIXTH_PARTY, state).getPoliticalParty();;
        partyResults.add(this.extractPartyResult(totalVoteCounts, resultSummary.getParty_6(), party6));
        return partyResults;
    }

    private HashMap<String, String> partyMap(List<PartyResult> partyResults){
        HashMap<String, String> parties = new HashMap<>();
        partyResults.forEach(partyResult ->
            parties.put(partyResult.getPoliticalParty().getCode(),partyResult.getPoliticalParty().getName()));
        return parties;
    }

    private HashMap<String, Integer> processPartiesWon(List<ResultRealTime> resultRealTimeList, State state) throws NotFoundException{
        List<ResultRealTime> resultSummary = resultSummaryGroupByLga(resultRealTimeList, state);
        return processPartiesWonGeneric(resultSummary);
    }

    private HashMap<String, Integer> processPartiesWonGeneric(List<ResultRealTime> resultSummary){
        AtomicInteger party1Sum = new AtomicInteger();
        AtomicInteger party2Sum = new AtomicInteger();
        AtomicInteger party3Sum = new AtomicInteger();
        AtomicInteger party4Sum = new AtomicInteger();
        AtomicInteger party5Sum = new AtomicInteger();
        AtomicInteger party6Sum = new AtomicInteger();
        HashMap<String, Integer> partyWon = new HashMap<>();

        resultSummary.forEach(result -> {
            if(result.getParty_1() >= result.getParty_2() && result.getParty_1() >= result.getParty_3()
            && result.getParty_1() >= result.getParty_4() && result.getParty_1() >= result.getParty_5() && result.getParty_1() >= result.getParty_6()){
                party1Sum.addAndGet(1);
            }
            else if(result.getParty_2() >= result.getParty_1() && result.getParty_2() >= result.getParty_3()
                    && result.getParty_2() >= result.getParty_4() && result.getParty_2() >= result.getParty_5() && result.getParty_2() >= result.getParty_6()){
                party2Sum.addAndGet(1);
            }
            else if(result.getParty_3() >= result.getParty_2() && result.getParty_3() >= result.getParty_1()
                    && result.getParty_3() >= result.getParty_4() && result.getParty_3() >= result.getParty_5() && result.getParty_3() >= result.getParty_6()){
                party3Sum.addAndGet(1);
            }
            else if(result.getParty_4() >= result.getParty_2()&& result.getParty_4() >= result.getParty_3()
                    && result.getParty_4() >= result.getParty_1() && result.getParty_4() >= result.getParty_6() && result.getParty_4() >= result.getParty_4()){
                party4Sum.addAndGet(1);
            }
            else if(result.getParty_5() >= result.getParty_2()&& result.getParty_5() >= result.getParty_3()
                    && result.getParty_5() >= result.getParty_1() && result.getParty_5() >= result.getParty_4() && result.getParty_5() >= result.getParty_6()){
                party5Sum.addAndGet(1);
            }
            else if(result.getParty_6() >= result.getParty_2()&& result.getParty_6() >= result.getParty_3()
                    && result.getParty_6() >= result.getParty_1() && result.getParty_6() >= result.getParty_4() && result.getParty_6() >= result.getParty_5()){
                party6Sum.addAndGet(1);
            }
        });

        partyWon.put(FIRST_PARTY, party1Sum.get());
        partyWon.put(SECOND_PARTY, party2Sum.get());
        partyWon.put(THIRD_PARTY, party3Sum.get());
        partyWon.put(FOURTH_PARTY, party4Sum.get());
        partyWon.put(FIFTH_PARTY, party5Sum.get());
        partyWon.put(SIXTH_PARTY, party6Sum.get());

        return partyWon;
    }

    private HashMap<String, Integer> processPartiesWon(List<ResultRealTime> resultRealTimeList) throws NotFoundException{
        List<ResultRealTime> resultSummary = resultSummaryGroupByState(resultRealTimeList);
        return processPartiesWonGeneric(resultSummary);
    }

    private HashMap<String, Integer> processPartiesWonSenatorialDistrict(List<ResultRealTime> resultRealTimeList, SenatorialDistrict senatorialDistrict) throws NotFoundException{
        List<ResultRealTime> resultSummary = resultSummaryForDistrictGroupByLga(resultRealTimeList, senatorialDistrict);
        return processPartiesWonGeneric(resultSummary);
    }

    private HashMap<String, Integer> processPartiesWonLga(List<ResultRealTime> resultRealTimeList, Lga lga){
        int party1Sum = 0;
        int party2Sum = 0;
        int party3Sum = 0;
        int party4Sum = 0;
        int party5Sum = 0;
        int party6Sum = 0;
        HashMap<String, Integer> partyWonLga = new HashMap<>();
        ResultRealTime result = resultSummaryForLga(resultRealTimeList, lga);

        if(result != null){
            if(result.getParty_1() >= result.getParty_2() && result.getParty_1() >= result.getParty_3()
                    && result.getParty_1() >= result.getParty_4() && result.getParty_1() >= result.getParty_5() && result.getParty_1() >= result.getParty_6()){
                party1Sum = 1;
            }
            else if(result.getParty_2() >= result.getParty_1() && result.getParty_2() >= result.getParty_3()
                    && result.getParty_2() >= result.getParty_4() && result.getParty_2() >= result.getParty_5() && result.getParty_2() >= result.getParty_6()){
                party2Sum = 1;
            }
            else if(result.getParty_3() >= result.getParty_2() && result.getParty_3() >= result.getParty_1()
                    && result.getParty_3() >= result.getParty_4() && result.getParty_3() >= result.getParty_5() && result.getParty_3() >= result.getParty_6()){
                party3Sum = 1;
            }
            else if(result.getParty_4() >= result.getParty_2()&& result.getParty_4() >= result.getParty_3()
                    && result.getParty_4() >= result.getParty_1() && result.getParty_4() >= result.getParty_6() && result.getParty_4() >= result.getParty_4()){
                party4Sum = 1;
            }
            else if(result.getParty_5() >= result.getParty_2()&& result.getParty_5() >= result.getParty_3()
                    && result.getParty_5() >= result.getParty_1() && result.getParty_5() >= result.getParty_4() && result.getParty_5() >= result.getParty_6()){
                party5Sum = 1;
            }
            else if(result.getParty_6() >= result.getParty_2()&& result.getParty_6() >= result.getParty_3()
                    && result.getParty_6() >= result.getParty_1() && result.getParty_6() >= result.getParty_4() && result.getParty_6() >= result.getParty_5()){
                party6Sum = 1;
            }
        }

        partyWonLga.put(FIRST_PARTY, party1Sum);
        partyWonLga.put(SECOND_PARTY, party2Sum);
        partyWonLga.put(THIRD_PARTY, party3Sum);
        partyWonLga.put(FOURTH_PARTY, party4Sum);
        partyWonLga.put(FIFTH_PARTY, party5Sum);
        partyWonLga.put(SIXTH_PARTY, party6Sum);

        return partyWonLga;
    }

    public DashboardResponse getDashboardByState(Long electionType) throws NotFoundException {
        State state = getDefaultState();
        return getDashboardByState(state.getId(), electionType);
    }

    public DashboardResponse getDashboardByState(Long stateId, Long electionType) throws NotFoundException {
        State state = stateService.findStateById(stateId).getState();
        Long totalStates = getTotalStates();
        Election election = resultService.getElection();
        List<ResultRealTime> resultRealTimeList = resultRealTimeRepository.findByStateIdAndElectionTypeAndElection(state.getId(), electionType, election);
        Long totalLgas = lgaByState(state);
        Long totalSenatorialDistricts = senatorialDistrictByState(state);
        Integer totalRegisteredVotes = getRegisteredVoters(resultRealTimeList);
        Integer totalAccreditedVotes =  getAccreditedVotes(resultRealTimeList);
        Long totalWards  = Long.valueOf(getWards(state));
        Long totalPollingUnits = Long.valueOf(getPollingUnitsByState(state));
        Integer totalVoteCounts = getTotalVotes(resultRealTimeList);
        Long lgaWithResults = getLgasWithResult(resultRealTimeList);
        Long wardsWithResults = getWardsWithResult(resultRealTimeList);

        ResultRealTime resultSummary = this.resultSummary(resultRealTimeList);
        List<PartyResult> partyResults = this.processPartyResults(resultSummary, state);
        long pollingUnitsWithResults = resultSummary.getPollingUnitCount();

        HashMap<String, String> partyMap = partyMap(partyResults);
        // lga won
        HashMap<String,Integer> lgasWon = processPartiesWon(resultRealTimeList, state);
        List<LgaResult> lgaResults = getResultsGroupByLgaState(resultRealTimeList, state);
        List<PartyLgaResult> partyLgaResults = new ArrayList<>();
        for (Map.Entry<String, Integer> lgaWon:lgasWon.entrySet()) {
            PartyLgaResult partyLgaResult = new PartyLgaResult(partyMap.get(lgaWon.getKey()), lgaWon.getValue());
            partyLgaResults.add(partyLgaResult);
        }
        Double resultsReceived = pollingUnitsWithResults>0?(pollingUnitsWithResults * 100.0 / totalPollingUnits) : 0;
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

    @Override
    public NationalDashboardResponse getDashboardByCountry(Long electionType) throws NotFoundException{
        Election election = resultService.getElection();
        Long totalStates = getTotalStates();
        Long totalZones = countGeoPoliticalZone();
        List<ResultRealTime> resultRealTimeList = resultRealTimeRepository.findByElectionAndElectionType(election, electionType);
        Long totalSenatorialDistricts = senatorialDistrict(); // totalZone
        Integer totalRegisteredVotes = getRegisteredVoters(election, electionType); // global by electionId
        Integer totalAccreditedVotes =  getAccreditedVotes(election, electionType); // global by electionId
        Long totalPollingUnits = Long.valueOf(countPollingUnit()); // global by electionId
        Long totalLgas = lgaService.countLga();
        Integer totalVoteCounts = getTotalVotes(election, electionType); // global by electionId
        Long lgasWithResults = getLgasWithResult(resultRealTimeList); // global by electionId;
        Long wardsWithResults = getWardsWithResult(resultRealTimeList); // global by electionId; stateWithResults
        Long statesWithResults = getStatesWithResult(resultRealTimeList); // global by electionId; stateWithResults
        Long zonesWithResults = getZonesWithResult(resultRealTimeList); // global by electionId; stateWithResults

        ResultRealTime resultSummary = this.resultSummary(resultRealTimeList);
        State state = resultRealTimeList.isEmpty()?
                new State(){{setId(resultRealTimeList.get(0).getStateId());}}
                :new State(){{setId(13L);}};
        List<PartyResult> partyResults = this.processPartyResults(resultSummary, state);
        long pollingUnitsWithResults = resultSummary.getPollingUnitCount();

        HashMap<String, String> partyMap = partyMap(partyResults);
        // lga won
        HashMap<String,Integer> statesWon = processPartiesWon(resultRealTimeList);
        List<StateResult> stateResults = getResultsGroupByState(resultRealTimeList);
        List<PartyStateResult> partyStateResults = new ArrayList<>();
        for (Map.Entry<String, Integer> stateWon:statesWon.entrySet()) {
            PartyStateResult partyStateResult = new PartyStateResult(partyMap.get(stateWon.getKey()), stateWon.getValue());
            partyStateResults.add(partyStateResult);
        }
        Double resultsReceived = pollingUnitsWithResults>0?(pollingUnitsWithResults * 100.0 / totalPollingUnits) : 0;
        return new NationalDashboardResponse("00", "Dashboard loaded.", totalZones, totalStates, totalSenatorialDistricts,
                totalRegisteredVotes, totalAccreditedVotes, totalVoteCounts, totalLgas,
                totalPollingUnits, zonesWithResults, statesWithResults, lgasWithResults, wardsWithResults, pollingUnitsWithResults,
                resultsReceived, partyResults, stateResults,partyStateResults
        );
    }

    @Override
    public NationalDashboardResponse getDashboardByZone(Long zoneId, Long electionType) throws NotFoundException{
        return new NationalDashboardResponse();
    }
    /*
    @Override
    public NationalDashboardResponse getDashboardByZone(Long zoneId, Long electionType) throws NotFoundException{
        Election election = resultService.getElection();
        Long totalStates = getTotalStatesByZone(zoneId);
        Long totalZones = 1L;
        List<ResultRealTime> resultRealTimeList = resultRealTimeRepository.findByElectionAndElectionTypeAndGeoPoliticalZone(election, electionType, zoneId);
        Long totalSenatorialDistricts = senatorialDistrict(); // totalZone
        Integer totalRegisteredVotes = getRegisteredVoters(election, electionType, zone); // global by electionId
        Integer totalAccreditedVotes =  getAccreditedVotes(election, electionType); // global by electionId
        Long totalPollingUnits = Long.valueOf(countPollingUnit()); // global by electionId
        Long totalLgas = lgaService.countLga();
        Integer totalVoteCounts = getTotalVotes(election, electionType); // global by electionId
        Long lgasWithResults = getLgasWithResult(resultRealTimeList); // global by electionId;
        Long wardsWithResults = getWardsWithResult(resultRealTimeList); // global by electionId; stateWithResults
        Long statesWithResults = getStatesWithResult(resultRealTimeList); // global by electionId; stateWithResults
        Long zonesWithResults = getZonesWithResult(resultRealTimeList); // global by electionId; stateWithResults

        ResultRealTime resultSummary = this.resultSummary(resultRealTimeList);
        State state = resultRealTimeList.isEmpty()?
                new State(){{setId(resultRealTimeList.get(0).getStateId());}}
                :new State(){{setId(13L);}};
        List<PartyResult> partyResults = this.processPartyResults(resultSummary, state);
        long pollingUnitsWithResults = resultSummary.getPollingUnitCount();

        HashMap<String, String> partyMap = partyMap(partyResults);
        // lga won
        HashMap<String,Integer> statesWon = processPartiesWon(resultRealTimeList);
        List<StateResult> stateResults = getResultsGroupByState(resultRealTimeList);
        List<PartyStateResult> partyStateResults = new ArrayList<>();
        for (Map.Entry<String, Integer> stateWon:statesWon.entrySet()) {
            PartyStateResult partyStateResult = new PartyStateResult(partyMap.get(stateWon.getKey()), stateWon.getValue());
            partyStateResults.add(partyStateResult);
        }
        Double resultsReceived = pollingUnitsWithResults>0?(pollingUnitsWithResults * 100.0 / totalPollingUnits) : 0;
        return new NationalDashboardResponse("00", "Dashboard loaded.", totalZones, totalStates, totalSenatorialDistricts,
                totalRegisteredVotes, totalAccreditedVotes, totalVoteCounts, totalLgas,
                totalPollingUnits, zonesWithResults, statesWithResults, lgasWithResults, wardsWithResults, pollingUnitsWithResults,
                resultsReceived, partyResults, stateResults,partyStateResults
        );
    }
*/
    @Override
    public NationalDashboardResponse getDashboardByStateGlobal(Long stateId, Long electionType){
        return new NationalDashboardResponse();
    }
    private PartyResult extractPartyResult(int totalVoteCounts, int partyVotes, PoliticalParty politicalParty) {
        double percent;
        PartyResult partyResult = new PartyResult();
        partyResult.setPoliticalParty(politicalParty);
        partyResult.setTotalVoteCount(partyVotes);
        percent = (partyVotes * 100.0) / totalVoteCounts;
        partyResult.setPercent(percent);
        partyResult.setResultPerParty(new ResultPerParty(null, politicalParty, partyVotes));
        return partyResult;
    }
    private Long lgaByState(State state){
        return lgaService.countLgaByStateCode(state.getId());
    }
    private List<GeoPoliticalZone> getZones(){
        return geoPoliticalZoneService.findAll();
    }
    private Long countLga(){
        return lgaService.countLga();
    }
    private Long countPollingUnit(){
        return pollingUnitService.countPollingUnit();
    }
    private Long countWard(){
        return wardService.countWard();
    }
    private Long countSenatorialDistrict(){
        return senatorialDistrictService.countSenatorialDistrict();
    }
    private Long countGeoPoliticalZone(){
        return geoPoliticalZoneService.countGeoPoliticalZone();
    }
    private List<Lga> getLgaByState(State state) throws NotFoundException{
        return lgaService.findLgaByStateCode(state.getId()).getLgas();
    }
    private Long lgaBySenatorialDistrict(SenatorialDistrict senatorialDistrict){
        return lgaService.countLgaBySenatorialDistrict(senatorialDistrict.getId());
    }
    private Long senatorialDistrictByState(State state){
        return senatorialDistrictService.countSenatorialDistrictByState(state);
    }
    private Long senatorialDistrict(){
        return senatorialDistrictService.countSenatorialDistrict();
    }
    private Integer getRegisteredVoters(Election electionId, Long electionType){
        return resultRealTimeRepository.findSumRegisteredVotes(electionId, electionType).intValue();
    }

    private Integer getAccreditedVotes(Election election, Long electionType){
        return resultRealTimeRepository.findSumAccreditedVoters(election, electionType).intValue();
    }
    private Integer getRegisteredVoters(List<ResultRealTime> results){
        int totalResult = 0;
        totalResult += results.stream()
            .map(ResultRealTime::getRegisteredVotersCount).mapToInt(Integer::intValue).sum();
        return totalResult;
    }


    private Integer getAccreditedVotes(List<ResultRealTime> results){
        return results.stream()
                    .map(ResultRealTime::getAccreditedVotersCount).mapToInt(Integer::intValue).sum();
    }


    public Integer getWards(State state){
        return (int)wardService.countWardByState(state);
    }

    public Integer getWardsBySenatorialDistrict(SenatorialDistrict senatorialDistrict){
        return (int)wardService.countWardBySenatorialDistrict(senatorialDistrict);
    }

    public Integer getWardsByLga(Lga lga){
        return (int)wardService.countWardByLga(lga);
    }

    public Integer getPollingUnitsByState(State state){
        return (int)pollingUnitService.countByState(state);
    }

    public Integer getPollingUnitsBySenatorialDistrict(SenatorialDistrict senatorialDistrict){
        return (int)pollingUnitService.countBySenatorialDistrict(senatorialDistrict);
    }

    public Integer getPollingUnitsByLga(Lga lga){
        return (int)pollingUnitService.countByLga(lga);
    }


    public Integer getVoteCounts(List<ResultRealTime> resultRealTime){
        return resultRealTime.stream()
                .map(ResultRealTime::getVoteCount)
                .mapToInt(Integer::intValue).sum();
    }

    private Long lgaCount(List<ResultRealTime> resultRealTimeList) {
        HashSet<Long> lgas = new HashSet<>();
        resultRealTimeList
                .forEach(resultRealTime -> lgas.add(resultRealTime.getLga().getId()));
        return (long) lgas.size();
    }

    private Long stateCount(List<ResultRealTime> resultRealTimeList) {
        HashSet<Long> states = new HashSet<>();
        resultRealTimeList
                .forEach(resultRealTime -> states.add(resultRealTime.getStateId()));
        return (long) states.size();
    }

    private Long zoneCount(List<ResultRealTime> resultRealTimeList) {
        HashSet<Long> zones = new HashSet<>();
        resultRealTimeList
                .forEach(resultRealTime -> zones.add(resultRealTime.getGeoPoliticalZoneId()));
        return (long) zones.size();
    }

    public Long getWardsWithResult(List<ResultRealTime> resultRealTime){
        AtomicInteger count = new AtomicInteger();
        List<Long> wards = new ArrayList<>();
        resultRealTime.forEach(result->{
            // check by level
            if(result.getVotingLevel().getCode().equals(VOTING_LEVEL_LGA)){
                // get total number of wards in lga
                count.addAndGet(getWardsByLga(result.getLga()));
            }
            else if(result.getVotingLevel().getCode().equals(VOTING_LEVEL_WARD)){
                count.addAndGet(1);
                wards.add(result.getWard().getId());
            }
            else{
                if(!wards.contains(result.getWard().getId())) {
                    wards.add(result.getWard().getId());
                    count.addAndGet((1));
                }
            }
        });
        return (long)count.get();
    }

    public Long getLgasWithResult(List<ResultRealTime> resultRealTime){
        return lgaCount(resultRealTime);
    }

    public Long getZonesWithResult(List<ResultRealTime> resultRealTime){
        return zoneCount(resultRealTime);
    }
    public Long getStatesWithResult(List<ResultRealTime> resultRealTime){
        return stateCount(resultRealTime);
    }

    private State getDefaultState() throws NotFoundException {
        State state = stateService.getDefaultState().getState();
        if(state == null){
            throw new NotFoundException("State not found.");
        }
        return state;
    }

    private SenatorialDistrict getSenatorialDistrict(Long id) throws NotFoundException {
        SenatorialDistrict senatorialDistrict = senatorialDistrictService.findSenatorialDistrictById(id).getSenatorialDistrict();
        if(senatorialDistrict == null){
            throw new NotFoundException("State not found.");
        }
        return senatorialDistrict;
    }

    private Lga getLga(Long id) throws NotFoundException {
        Lga lga = lgaService.findLgaById(id).getLga();
        if(lga == null){
            throw new NotFoundException("Lga not found.");
        }
        return lga;
    }

    @Override
    public DashboardResponse getDashboardBySenatorialDistrict(Long senatorialDistrictId, Long electionType) throws NotFoundException {
        SenatorialDistrict senatorialDistrict = getSenatorialDistrict(senatorialDistrictId);
        Election election = resultService.getElection();
        Long totalLgas = lgaBySenatorialDistrict(senatorialDistrict);
        Long totalSenatorialDistricts = 1L;
        List<ResultRealTime> resultRealTimeList = resultRealTimeRepository.findBySenatorialDistrictAndElectionTypeAndElection(senatorialDistrict, electionType, election);
        Integer totalRegisteredVotes = getRegisteredVoters(resultRealTimeList);
        Integer totalAccreditedVotes =  getAccreditedVotes(resultRealTimeList);
        Long totalWards  = Long.valueOf(getWardsBySenatorialDistrict(senatorialDistrict));
        Long totalPollingUnits = Long.valueOf(getPollingUnitsBySenatorialDistrict(senatorialDistrict));
        Integer totalVoteCounts = getVoteCounts(resultRealTimeList);
        Long lgaWithResults = lgaCount(resultRealTimeList);
        Long wardsWithResults = getWardsWithResult(resultRealTimeList);
        ResultRealTime resultSummary = this.resultSummary(resultRealTimeList);
        List<PartyResult> partyResults = this.processPartyResults(resultSummary, senatorialDistrict.getState());

        long pollingUnitsWithResults = resultSummary.getPollingUnitCount();

        HashMap<String, String> partyMap = partyMap(partyResults);
        // lga won
        HashMap<String,Integer> lgasWon = processPartiesWonSenatorialDistrict(resultRealTimeList, senatorialDistrict);
        List<LgaResult> lgaResults = getResultsGroupByLgaSenatorialDistrict(resultRealTimeList, senatorialDistrict);
        List<PartyLgaResult> partyLgaResults = new ArrayList<>();
        for (Map.Entry<String, Integer> lgaWon:lgasWon.entrySet()) {
            PartyLgaResult partyLgaResult = new PartyLgaResult(partyMap.get(lgaWon.getKey()), lgaWon.getValue());
            partyLgaResults.add(partyLgaResult);
        }
        Double resultsReceived = pollingUnitsWithResults>0?(pollingUnitsWithResults * 100.0 / totalPollingUnits) : 0;

        return new DashboardResponse("00", "Dashboard loaded.", 1L,
                totalLgas, totalSenatorialDistricts, totalRegisteredVotes, totalAccreditedVotes,
                totalVoteCounts, totalWards, totalPollingUnits,
                lgaWithResults,
                wardsWithResults,
                pollingUnitsWithResults,
                resultsReceived,
                partyResults, lgaResults,partyLgaResults
        );
    }


    public DashboardResponse getDashboardByLga(Long lgaId, Long electionType) throws NotFoundException {
        Lga lga = getLga(lgaId);
        Long totalLgas = 1L;
        Long totalSenatorialDistricts = 1L;
        Election election = resultService.getElection();
        List<ResultRealTime> resultRealTimeList = resultRealTimeRepository.findByLgaAndElectionTypeAndElection(lga, electionType, election);
        Integer totalRegisteredVotes = getRegisteredVoters(resultRealTimeList);
        Integer totalAccreditedVotes =  getAccreditedVotes(resultRealTimeList);
        Long totalWards  = Long.valueOf(getWardsByLga(lga));
        Long totalPollingUnits = Long.valueOf(getPollingUnitsByLga(lga));
        Integer totalVoteCounts = getVoteCounts(resultRealTimeList);
        Long lgaWithResults = getLgasWithResult(resultRealTimeList);
        Long wardsWithResults = getWardsWithResult(resultRealTimeList);
        ResultRealTime resultSummary = this.resultSummary(resultRealTimeList);
        List<PartyResult> partyResults = this.processPartyResults(resultSummary, lga.getState());

        long pollingUnitsWithResults = resultSummary.getPollingUnitCount();

        HashMap<String, String> partyMap = partyMap(partyResults);
        // lga won
        HashMap<String,Integer> lgasWon = processPartiesWonLga(resultRealTimeList, lga);
        List<LgaResult> lgaResults = new ArrayList<>();
        LgaResult lgaResult = this.getResultsGroupByLga(resultRealTimeList, lga);
        if(lgaResult != null) {
            lgaResults.add(lgaResult);
        }
        List<PartyLgaResult> partyLgaResults = new ArrayList<>();
        for (Map.Entry<String, Integer> lgaWon:lgasWon.entrySet()) {
            PartyLgaResult partyLgaResult = new PartyLgaResult(partyMap.get(lgaWon.getKey()), lgaWon.getValue());
            partyLgaResults.add(partyLgaResult);
        }
        Double resultsReceived = pollingUnitsWithResults>0?(pollingUnitsWithResults * 100.0 / totalPollingUnits) : 0;

        return new DashboardResponse("00", "Dashboard loaded.", 1L,
                totalLgas, totalSenatorialDistricts, totalRegisteredVotes, totalAccreditedVotes,
                totalVoteCounts, totalWards, totalPollingUnits,
                lgaWithResults,
                wardsWithResults,
                pollingUnitsWithResults,
                resultsReceived,
                partyResults, lgaResults,partyLgaResults
        );
    }

}
