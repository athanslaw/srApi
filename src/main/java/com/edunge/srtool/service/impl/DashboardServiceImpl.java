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
import java.util.concurrent.atomic.AtomicInteger;

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
    private static final String FIRST_PARTY = "party_1";
    private static final String SECOND_PARTY = "party_2";
    private static final String THIRD_PARTY = "party_3";
    private static final String FOURTH_PARTY = "party_4";

    private static final String VOTING_LEVEL_LGA = "LGA";
    private static final String VOTING_LEVEL_WARD = "Ward";

    @Autowired
    public DashboardServiceImpl(StateRepository stateRepository, LgaRepository lgaRepository,
                                SenatorialDistrictRepository senatorialDistrictRepository,
                                WardRepository wardRepository, PollingUnitRepository pollingUnitRepository,
                                PoliticalPartyRepository politicalPartyRepository, ResultRepository resultRepository,
                                ResultRealTimeRepository resultRealTimeRepository) {
        this.stateRepository = stateRepository;
        this.lgaRepository = lgaRepository;
        this.senatorialDistrictRepository = senatorialDistrictRepository;
        this.wardRepository = wardRepository;
        this.pollingUnitRepository = pollingUnitRepository;
        this.politicalPartyRepository = politicalPartyRepository;
        this.resultRepository = resultRepository;
        this.resultRealTimeRepository = resultRealTimeRepository;
    }

    private Long getTotalStates(){
        return stateRepository.count();
    }

    private Long getTotalLgas(){
        return lgaRepository.count();
    }

    private Long getSenatorialDistrics(State state){
        return (long)senatorialDistrictRepository.findByState(state).size();
    }

    private Long getPollingUnits(State state){
        return (long)pollingUnitRepository.findByState(state).size();
    }

    private Integer getTotalRegisteredVotes(State state){
        List<Result> results = resultRepository.findByStateId(state.getId());
        return results.stream().map(Result::getRegisteredVotersCount).mapToInt(Integer::intValue).sum();
    }

    private Integer getTotalAccreditedVotes(State state){
        List<Result> results = resultRepository.findByStateId(state.getId());
        return results.stream().map(Result::getAccreditedVotersCount).mapToInt(Integer::intValue).sum();
    }

    private Integer getTotalVotes(State state){
        List<ResultRealTime> resultRealTimeList = resultRealTimeRepository.findByStateId(state.getId());
        return resultRealTimeList.stream().map(ResultRealTime::getVoteCount).mapToInt(Integer::intValue).sum();
    }

    public DashboardResponse getDefaultDashboard() throws NotFoundException{
        Long totalStates = getTotalStates();
        State state = getDefaultState();
        Long totalLgas = getTotalLgas();
        Long totalSenatorialDistricts = getSenatorialDistrics(state);
        Integer totalRegisteredVotes = getTotalRegisteredVotes(state);
        Integer totalAccreditedVotes = getTotalAccreditedVotes(state);
        Integer totalVoteCounts = getTotalVotes(state);
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
        int voteCount;
        int totalPU;
        party1Sum = resultRealTime.stream().mapToInt(ResultRealTime::getParty_1).sum();
        party2Sum = resultRealTime.stream().mapToInt(ResultRealTime::getParty_2).sum();
        party3Sum = resultRealTime.stream().mapToInt(ResultRealTime::getParty_3).sum();
        party4Sum = resultRealTime.stream().mapToInt(ResultRealTime::getParty_4).sum();
        voteCount = resultRealTime.stream().mapToInt(ResultRealTime::getVoteCount).sum();
        totalPU = resultRealTime.stream().mapToInt(ResultRealTime::getPollingUnitCount).sum();
        ResultRealTime resultRealTime1 = new ResultRealTime();
        resultRealTime1.setParty_1(party1Sum);
        resultRealTime1.setParty_2(party2Sum);
        resultRealTime1.setParty_3(party3Sum);
        resultRealTime1.setParty_4(party4Sum);
        resultRealTime1.setVoteCount(voteCount);
        resultRealTime1.setPollingUnitCount(totalPU);
        return resultRealTime1;
    }

    private List<ResultRealTime> resultSummaryGroupByLga(List<ResultRealTime> resultRealTime, State state){
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
                resultRealTime1.setVoteCount(resultSummary.getVoteCount());
                resultRealTime1.setPollingUnitCount(resultSummary.getPollingUnitCount());
                results.add(resultRealTime1);
            }
        });
        return results;
    }

    private List<ResultRealTime> resultSummaryForDistrictGroupByLga(List<ResultRealTime> resultRealTime, SenatorialDistrict senatorialDistrict){
        List<Lga> lgas = lgaRepository.findBySenatorialDistrict(senatorialDistrict);
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
                resultRealTime1.setVoteCount(resultSummary.getVoteCount());
                resultRealTime1.setPollingUnitCount(resultSummary.getPollingUnitCount());

                results.add(resultRealTime1);
            }
        });
        return results;
    }

    private ResultRealTime resultSummaryForLga(List<ResultRealTime> resultRealTime, Lga lga){
        AtomicInteger party1Sum= new AtomicInteger();
        AtomicInteger party2Sum = new AtomicInteger();
        AtomicInteger party3Sum= new AtomicInteger();
        AtomicInteger party4Sum= new AtomicInteger();
        AtomicInteger voteCount= new AtomicInteger();
        AtomicInteger totalPU= new AtomicInteger();

        party1Sum.set(0);
        party2Sum.set(0);
        party3Sum.set(0);
        party4Sum.set(0);
        voteCount.set(0);
        totalPU.set(0);
        resultRealTime.stream().filter(result->result.getLga().equals(lga))
                .forEach(result -> {
                party1Sum.addAndGet(result.getParty_1());
                party2Sum.addAndGet(result.getParty_2());
                party3Sum.addAndGet(result.getParty_3());
                party4Sum.addAndGet(result.getParty_4());
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
            resultRealTime1.setVoteCount(voteCount.get());
            resultRealTime1.setPollingUnitCount(totalPU.get());
            return resultRealTime1;
        }
        return null;
    }

    private LgaResult getResultsGroupByLga(List<ResultRealTime> resultRealTime, Lga lga){
        ResultRealTime result = resultSummaryForLga(resultRealTime, lga);

        int party1Sum;
        int party2Sum;
        int party3Sum;
        int party4Sum;
        int voteCount;

        if(result != null) {
            party1Sum = result.getParty_1();
            party2Sum = result.getParty_2();
            party3Sum = result.getParty_3();
            party4Sum = result.getParty_4();
            voteCount = result.getVoteCount();

            if (voteCount > 0) {
                int totalVoteCounts = party1Sum + party2Sum + party3Sum + party4Sum;
                List<PartyResult> partyResults = new ArrayList<>();

                // party 1
                PoliticalParty party1 = politicalPartyRepository.findByCode(FIRST_PARTY);
                partyResults.add(this.extractPartyResult(totalVoteCounts, party1Sum, party1));

                // party 2
                PoliticalParty party2 = politicalPartyRepository.findByCode(SECOND_PARTY);
                partyResults.add(this.extractPartyResult(totalVoteCounts, party2Sum, party2));

                // party 3
                PoliticalParty party3 = politicalPartyRepository.findByCode(THIRD_PARTY);
                partyResults.add(this.extractPartyResult(totalVoteCounts, party3Sum, party3));

                // party 4
                PoliticalParty party4 = politicalPartyRepository.findByCode(FOURTH_PARTY);
                partyResults.add(this.extractPartyResult(totalVoteCounts, party4Sum, party4));

                partyResults.sort(Comparator.comparingInt(PartyResult::getTotalVoteCount));
                return new LgaResult(lga, partyResults);
            }
        }

        return null;
    }

    private List<LgaResult> getResultsGroupByLgaSenatorialDistrict(List<ResultRealTime> resultRealTime, SenatorialDistrict senatorialDistrict){
        List<LgaResult> lgaResults = new ArrayList<>();
        List<ResultRealTime> resultRealTime1 = resultSummaryForDistrictGroupByLga(resultRealTime, senatorialDistrict);

        List<Lga> lgas = lgaRepository.findBySenatorialDistrict(senatorialDistrict);

        lgas.forEach(lga -> {
            LgaResult lgaResult = this.getResultsGroupByLga(resultRealTime1, lga);
            if(lgaResult != null) {
                lgaResults.add(lgaResult);
            }
        });

        return lgaResults;
    }

    private List<LgaResult> getResultsGroupByLgaState(List<ResultRealTime> resultRealTime, State state){
        List<LgaResult> lgaResults = new ArrayList<>();
        List<ResultRealTime> resultRealTime1 = resultSummaryGroupByLga(resultRealTime, state);

        List<Lga> lgas = lgaRepository.findByState(state);

        lgas.forEach(lga -> {
            LgaResult lgaResult = this.getResultsGroupByLga(resultRealTime1, lga);
            if(lgaResult != null) {
                lgaResults.add(lgaResult);
            }
        });

        return lgaResults;
    }

    private List<PartyResult> processPartyResults(ResultRealTime resultSummary){
        List<PartyResult> partyResults = new ArrayList<>();
        int totalVoteCounts = resultSummary.getParty_1() + resultSummary.getParty_2() +resultSummary.getParty_3()+resultSummary.getParty_4();

        // party 1
        PoliticalParty party1 = politicalPartyRepository.findByCode(FIRST_PARTY);
        partyResults.add(this.extractPartyResult(totalVoteCounts, resultSummary.getParty_1(), party1));

        // party 2
        PoliticalParty party2 = politicalPartyRepository.findByCode(SECOND_PARTY);
        partyResults.add(this.extractPartyResult(totalVoteCounts, resultSummary.getParty_2(), party2));

        // party 3
        PoliticalParty party3 = politicalPartyRepository.findByCode(THIRD_PARTY);
        partyResults.add(this.extractPartyResult(totalVoteCounts, resultSummary.getParty_3(), party3));

        // party 4
        PoliticalParty party4 = politicalPartyRepository.findByCode(FOURTH_PARTY);
        partyResults.add(this.extractPartyResult(totalVoteCounts, resultSummary.getParty_4(), party4));

        return partyResults;
    }

    private HashMap<String, String> partyMap(List<PartyResult> partyResults){
        HashMap<String, String> parties = new HashMap<>();
        partyResults.forEach(partyResult ->
            parties.put(partyResult.getPoliticalParty().getCode(),partyResult.getPoliticalParty().getName()));
        return parties;
    }

    private HashMap<String, Integer> processPartiesWon(List<ResultRealTime> resultRealTimeList, State state){
        AtomicInteger party1Sum = new AtomicInteger();
        AtomicInteger party2Sum = new AtomicInteger();
        AtomicInteger party3Sum = new AtomicInteger();
        AtomicInteger party4Sum = new AtomicInteger();
        HashMap<String, Integer> partyWonLga = new HashMap<>();
        List<ResultRealTime> resultSummary = resultSummaryGroupByLga(resultRealTimeList, state);

        resultSummary.forEach(result -> {
            if(result.getParty_1() >= result.getParty_2() && result.getParty_1() >= result.getParty_3()
            && result.getParty_1() >= result.getParty_4()){
                party1Sum.addAndGet(1);
            }
            else if(result.getParty_2() >= result.getParty_1() && result.getParty_2() >= result.getParty_3()
                    && result.getParty_2() >= result.getParty_4()){
                party2Sum.addAndGet(1);
            }
            else if(result.getParty_3() >= result.getParty_2() && result.getParty_3() >= result.getParty_1()
                    && result.getParty_3() >= result.getParty_4()){
                party3Sum.addAndGet(1);
            }
            else if(result.getParty_4() >= result.getParty_2()&& result.getParty_4() >= result.getParty_3()
                    && result.getParty_4() >= result.getParty_1()){
                party4Sum.addAndGet(1);
            }
        });

        partyWonLga.put(FIRST_PARTY, party1Sum.get());
        partyWonLga.put(SECOND_PARTY, party2Sum.get());
        partyWonLga.put(THIRD_PARTY, party3Sum.get());
        partyWonLga.put(FOURTH_PARTY, party4Sum.get());

        return partyWonLga;
    }

    private HashMap<String, Integer> processPartiesWonSenatorialDistrict(List<ResultRealTime> resultRealTimeList, SenatorialDistrict senatorialDistrict){
        AtomicInteger party1Sum = new AtomicInteger();
        AtomicInteger party2Sum = new AtomicInteger();
        AtomicInteger party3Sum = new AtomicInteger();
        AtomicInteger party4Sum = new AtomicInteger();
        HashMap<String, Integer> partyWonLga = new HashMap<>();
        List<ResultRealTime> resultSummary = resultSummaryForDistrictGroupByLga(resultRealTimeList, senatorialDistrict);

        resultSummary.forEach(result -> {
            if(result.getParty_1()>=result.getParty_2()&& result.getParty_1() >= result.getParty_3()
            && result.getParty_1() >= result.getParty_4()){
                party1Sum.addAndGet(1);
            }
            else if(result.getParty_2()>=result.getParty_1()&& result.getParty_2() >= result.getParty_3()
                    && result.getParty_2() >= result.getParty_4()){
                party2Sum.addAndGet(1);
            }
            else if(result.getParty_3() >= result.getParty_2()&& result.getParty_3() >= result.getParty_1()
                    && result.getParty_3() >= result.getParty_4()){
                party3Sum.addAndGet(1);
            }
            else if(result.getParty_4() >= result.getParty_2()&& result.getParty_4() >= result.getParty_3()
                    && result.getParty_4() >= result.getParty_1()){
                party4Sum.addAndGet(1);
            }
        });

        partyWonLga.put(FIRST_PARTY, party1Sum.get());
        partyWonLga.put(SECOND_PARTY, party2Sum.get());
        partyWonLga.put(THIRD_PARTY, party3Sum.get());
        partyWonLga.put(FOURTH_PARTY, party4Sum.get());

        return partyWonLga;
    }

    private HashMap<String, Integer> processPartiesWonLga(List<ResultRealTime> resultRealTimeList, Lga lga){
        int party1Sum = 0;
        int party2Sum = 0;
        int party3Sum = 0;
        int party4Sum = 0;
        HashMap<String, Integer> partyWonLga = new HashMap<>();
        ResultRealTime result = resultSummaryForLga(resultRealTimeList, lga);

        if(result != null){
            if(result.getParty_1()>=result.getParty_2()&& result.getParty_1() >= result.getParty_3()
            && result.getParty_1() >= result.getParty_4()){
                party1Sum = 1;
            }
            else if(result.getParty_2()>=result.getParty_1()&& result.getParty_2() >= result.getParty_3()
                    && result.getParty_2() >= result.getParty_4()){
                party2Sum = 1;
            }
            else if(result.getParty_3() >= result.getParty_2()&& result.getParty_3() >= result.getParty_1()
                    && result.getParty_3() >= result.getParty_4()){
                party3Sum = 1;
            }
            else if(result.getParty_4() >= result.getParty_2()&& result.getParty_4() >= result.getParty_3()
                    && result.getParty_4() >= result.getParty_1()){
                party4Sum = 1;
            }
        }

        partyWonLga.put(FIRST_PARTY, party1Sum);
        partyWonLga.put(SECOND_PARTY, party2Sum);
        partyWonLga.put(THIRD_PARTY, party3Sum);
        partyWonLga.put(FOURTH_PARTY, party4Sum);

        return partyWonLga;
    }

    public DashboardResponse getDashboardByState() throws NotFoundException {
        State state = getDefaultState();
        Long totalStates = getTotalStates();
        Long totalLgas = lgaByState(state);
        Long totalSenatorialDistricts = senatorialDistrictByState(state);
        Integer totalRegisteredVotes = getRegisteredVotersPerState(state);
        Integer totalAccreditedVotes =  getAccreditedVotesPerState(state);
        Long totalWards  = Long.valueOf(getWards(state));
        Long totalPollingUnits = Long.valueOf(getPollingUnitsByState(state));
        Integer totalVoteCounts = getTotalVotes(state);
        Long lgaWithResults = getStateLgasWithResult(state);
        Long wardsWithResults = getStateWardsWithResult(state);

        List<ResultRealTime> resultRealTimeList = resultRealTimeRepository.findByStateId(state.getId());
        ResultRealTime resultSummary = this.resultSummary(resultRealTimeList);
        List<PartyResult> partyResults = this.processPartyResults(resultSummary);

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
        List<Lga> lga = lgaRepository.findByState(state);
        return (long) lga.size();
    }

    private List<Lga> getLgaByState(State state){
        return lgaRepository.findByState(state);
    }

    private Long lgaBySenatorialDistrict(SenatorialDistrict senatorialDistrict){
        List<Lga> lga = lgaRepository.findBySenatorialDistrict(senatorialDistrict);
        return (long) lga.size();
    }


    private Long senatorialDistrictByState(State state){
        List<SenatorialDistrict> senatorialDistricts = senatorialDistrictRepository.findByState(state);
        return (long) senatorialDistricts.size();
    }

    private Integer getRegisteredVotersPerState(State state){
        List<ResultRealTime> results = resultRealTimeRepository.findByStateId(state.getId());
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
        List<ResultRealTime> results = resultRealTimeRepository.findByStateId(state.getId());
        int totalResult = 0;

        totalResult += results.stream()
                .map(ResultRealTime::getAccreditedVotersCount).mapToInt(Integer::intValue).sum();

        return totalResult;
    }

    private Integer getAccreditedVotesPerSenatorialDistrict(SenatorialDistrict senatorialDistrict){
        List<ResultRealTime> results = resultRealTimeRepository.findBySenatorialDistrict(senatorialDistrict);
        return results.stream()
                    .map(ResultRealTime::getAccreditedVotersCount).mapToInt(Integer::intValue).sum();
    }

    private Integer getAccreditedVotesPerLga(Lga lga){
        List<ResultRealTime> results = resultRealTimeRepository.findByLga(lga);
        return results.stream()
                .map(ResultRealTime::getAccreditedVotersCount).mapToInt(Integer::intValue).sum();
    }


    public Integer getWards(State state){
        return wardRepository.findByState(state).size();
    }

    public Integer getWardsBySenatorialDistrict(SenatorialDistrict senatorialDistrict){
        return wardRepository.findBySenatorialDistrict(senatorialDistrict).size();
    }

    public Integer getWardsByLga(Lga lga){
        return wardRepository.findByLga(lga).size();
    }

    public Integer getPollingUnitsByState(State state){
        return pollingUnitRepository.findByState(state).size();
    }

    public Integer getPollingUnitsBySenatorialDistrict(SenatorialDistrict senatorialDistrict){
        return pollingUnitRepository.findBySenatorialDistrict(senatorialDistrict).size();
    }

    public Integer getPollingUnitsByLga(Lga lga){
        return pollingUnitRepository.findByLga(lga).size();
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

    private Long lgaCount(List<ResultRealTime> resultRealTimeList) {
        HashSet<Long> lgas = new HashSet<>();
        resultRealTimeList
                .forEach(resultRealTime -> lgas.add(resultRealTime.getLga().getId()));
        return (long) lgas.size();
    }

    public Long getStateWardsWithResult(State state){
        List<ResultRealTime> resultRealTime = resultRealTimeRepository.findByStateId(state.getId());
        AtomicInteger count = new AtomicInteger();
        List<String> wards = new ArrayList<>();
        resultRealTime.forEach(result->{
            // check by level
            if(result.getVotingLevel().getCode().equals(VOTING_LEVEL_LGA)){
                // get total number of wards in lga
                count.addAndGet(wardRepository.findByLga(result.getLga()).size());
            }
            else if(result.getVotingLevel().getCode().equals(VOTING_LEVEL_WARD)){
                count.addAndGet(1);
                wards.add(result.getWard().getCode());
            }
            else{
                if(!wards.contains(result.getWard().getCode())) {
                    wards.add(result.getWard().getCode());
                    count.addAndGet((1));
                }
            }
        });
        return (long)count.get();
    }

    public Long getSenatorialDistrictWardsWithResult(SenatorialDistrict senatorialDistrict){
        List<ResultRealTime> resultRealTime = resultRealTimeRepository.findBySenatorialDistrict(senatorialDistrict);
        AtomicInteger count = new AtomicInteger();
        List<String> wards = new ArrayList<>();
        resultRealTime.forEach(result->{
            // check by level
            if(result.getVotingLevel().getCode().equals(VOTING_LEVEL_LGA)){
                // get total number of wards in lga
                count.addAndGet(wardRepository.findByLga(result.getLga()).size());
            }
            else if(result.getVotingLevel().getCode().equals(VOTING_LEVEL_WARD)){
                count.addAndGet(1);
                wards.add(result.getWard().getCode());
            }
            else{
                if(!wards.contains(result.getWard().getCode())) {
                    wards.add(result.getWard().getCode());
                    count.addAndGet((1));
                }
            }
        });
        return (long)count.get();
    }

    public Long getWardsWithResult(Lga lga){
        List<ResultRealTime> resultRealTime = resultRealTimeRepository.findByLga(lga);
        AtomicInteger count = new AtomicInteger();
        List<String> wards = new ArrayList<>();
        resultRealTime.forEach(result->{
            // check by level
            if(result.getVotingLevel().getCode().equals(VOTING_LEVEL_LGA)){
                // get total number of wards in lga
                count.addAndGet(wardRepository.findByLga(result.getLga()).size());
            }
            else if(result.getVotingLevel().getCode().equals(VOTING_LEVEL_WARD)){
                count.addAndGet(1);
                wards.add(result.getWard().getCode());
            }
            else{
                if(!wards.contains(result.getWard().getCode())) {
                    wards.add(result.getWard().getCode());
                    count.addAndGet((1));
                }
            }
        });
        return (long)count.get();
    }

    public Long getStateLgasWithResult(State state){
        List<ResultRealTime> resultRealTime = resultRealTimeRepository.findByStateId(state.getId());
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

    private State getDefaultState() throws NotFoundException {
        State state = stateRepository.findByDefaultState(true);
        if(state == null){
            throw new NotFoundException("State not found.");
        }
        return state;
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
        Long totalLgas = lgaBySenatorialDistrict(senatorialDistrict);
        Long totalSenatorialDistricts = 1L;
        Integer totalRegisteredVotes = getRegisteredVotersPerSenatorialDistrict(senatorialDistrict);
        Integer totalAccreditedVotes =  getAccreditedVotesPerSenatorialDistrict(senatorialDistrict);
        Long totalWards  = Long.valueOf(getWardsBySenatorialDistrict(senatorialDistrict));
        Long totalPollingUnits = Long.valueOf(getPollingUnitsBySenatorialDistrict(senatorialDistrict));
        Integer totalVoteCounts = getVoteCountsBySenatorialDistrict(senatorialDistrict);
        Long lgaWithResults = getSenatorialDistrcitLgasWithResult(senatorialDistrict);
        Long wardsWithResults = getSenatorialDistrictWardsWithResult(senatorialDistrict);
        List<ResultRealTime> resultRealTimeList = resultRealTimeRepository.findBySenatorialDistrict(senatorialDistrict);
        ResultRealTime resultSummary = this.resultSummary(resultRealTimeList);
        List<PartyResult> partyResults = this.processPartyResults(resultSummary);

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


    public DashboardResponse getDashboardByLga(Long lgaId) throws NotFoundException {
        Lga lga = getLga(lgaId);
        Long totalLgas = 1L;
        Long totalSenatorialDistricts = 1L;
        Integer totalRegisteredVotes = getRegisteredVotersPerLga(lga);
        Integer totalAccreditedVotes =  getAccreditedVotesPerLga(lga);
        Long totalWards  = Long.valueOf(getWardsByLga(lga));
        Long totalPollingUnits = Long.valueOf(getPollingUnitsByLga(lga));
        Integer totalVoteCounts = getVoteCountsByLga(lga);
        Long lgaWithResults = getLgasWithResult(lga);
        Long wardsWithResults = getWardsWithResult(lga);
        List<ResultRealTime> resultRealTimeList = resultRealTimeRepository.findByLga(lga);
        ResultRealTime resultSummary = this.resultSummary(resultRealTimeList);
        List<PartyResult> partyResults = this.processPartyResults(resultSummary);

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
