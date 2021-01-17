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
        Integer totalRegisteredVotes = getRegisteredVotersPerState(state);
        Integer totalAccreditedVotes =  getAccreditedVotesPerState(state);
        Long totalWards  = Long.valueOf(getWardsByLocalGovernment(state));
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
        results.stream()
                .filter(result -> result.getLga().getState().getId().equals(state.getId()))
                .filter(result -> result.getVotingLevel().getCode().equals("Ward")).forEach(result -> {
            List<PollingUnit> pollingUnits = pollingUnitRepository.findByWard(result.getWard());
            wardLevelResult.updateAndGet(v -> v + pollingUnits.size()-1);
        });
        pollingUnitsWithResults+=wardLevelResult.get();

        //Get polling units by wardLevel results
        AtomicReference<Integer> lgaLevelResult = new AtomicReference<>(0);
        results.stream()
                .filter(result -> result.getLga().getState().getId().equals(state.getId()))
                .filter(result -> result.getVotingLevel().getCode().equals("LGA")).forEach(result -> {
            List<PollingUnit> pollingUnits = pollingUnitRepository.findByLga(result.getLga());
            lgaLevelResult.updateAndGet(v -> v + pollingUnits.size()-1);
        });
        pollingUnitsWithResults+=lgaLevelResult.get();

        List<LgaResult> lgaResults = new ArrayList<>();
        HashSet<String> lgaSet = new HashSet<>();
        HashMap<String,Integer> lgasWon = new HashMap<>();
        results.stream().filter(result -> result.getLga().getState().getId().equals(stateId))
                .forEach(result -> {
                    try {
                        if(lgaSet.add(result.getLga().getCode())){
                            LgaResult lgaResult = getLgaResult(result.getLga().getId());
                            String winningParty = lgaResult.getPartyResults().get(lgaResult.getPartyResults().size()-1).getPoliticalParty().getCode();
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

    private Integer getRegisteredVotersPerSenatorialDistrict(SenatorialDistrict senatorialDistrict){
        List<Result> results = resultRepository.findAll();
//        List<SenatorialDistrict> senatorialDistricts = senatorialDistrictRepository.findByState(state);
        int totalResult = 0;
        totalResult += results.stream()
            .filter(result -> result.getSenatorialDistrict().getId().equals(senatorialDistrict.getId()))
            .map(Result::getRegisteredVotersCount).mapToInt(Integer::intValue).sum();
        return totalResult;
    }

    private Integer getRegisteredVotersPerLga(Lga lga){
        List<Result> results = resultRepository.findAll();
        int totalResult = 0;
        totalResult += results.stream()
                .filter(result -> result.getLga().getId().equals(lga.getId()))
                .map(Result::getRegisteredVotersCount).mapToInt(Integer::intValue).sum();
        return totalResult;
    }


    private Integer getAccreditedVotesPerState(State state){
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

    private Integer getAccreditedVotesPerSenatorialDistrict(SenatorialDistrict senatorialDistrict){
        List<Result> results = resultRepository.findAll();
        int totalResult = 0;
            totalResult += results.stream()
                    .filter(result -> result.getSenatorialDistrict().getId().equals(senatorialDistrict.getId()))
                    .map(Result::getAccreditedVotersCount).mapToInt(Integer::intValue).sum();
        return totalResult;
    }

    private Integer getAccreditedVotesPerLga(Lga lga){
        List<Result> results = resultRepository.findAll();
        int totalResult = 0;
        totalResult += results.stream()
                .filter(result -> result.getLga().getId().equals(lga.getId()))
                .map(Result::getAccreditedVotersCount).mapToInt(Integer::intValue).sum();
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

    public Integer getPollingUnitsByWardBySenatorialDistrict(SenatorialDistrict senatorialDistrict){
        List<PollingUnit> pollingUnits = pollingUnitRepository.findAll();
        List<Ward> wards = wardRepository.findAll();
        List<Lga> lgas = lgaRepository.findBySenatorialDistrict(senatorialDistrict);
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

    public Integer getPollingUnitsByWardByLga(Lga lga){
        List<PollingUnit> pollingUnits = pollingUnitRepository.findAll();

        List<Ward> wards = wardRepository.findAll();
        return (int) pollingUnits.stream()
                                .filter(pollingUnit -> pollingUnit.getLga().getId().equals(lga.getId())).count();

    }

    public Integer getVoteCountsByState(State state){
        List<ResultPerParty> resultPerParties = resultPerPartyRepository.findAll();
        return resultPerParties.stream()
                .filter(resultPerParty -> resultPerParty.getResult().getLga().getState().getId().equals(state.getId()))
                .map(ResultPerParty::getVoteCount)
                .mapToInt(Integer::intValue).sum();
    }

    public Integer getVoteCountsBySenatorialDistrict(SenatorialDistrict senatorialDistrict){
        List<ResultPerParty> resultPerParties = resultPerPartyRepository.findAll();
        return resultPerParties.stream()
                .filter(resultPerParty -> resultPerParty.getResult().getSenatorialDistrict().getId().equals(senatorialDistrict.getId()))
                .map(ResultPerParty::getVoteCount)
                .mapToInt(Integer::intValue).sum();
    }

    public Integer getVoteCountsByLga(Lga lga){
        List<ResultPerParty> resultPerParties = resultPerPartyRepository.findAll();
        return resultPerParties.stream()
                .filter(resultPerParty -> resultPerParty.getResult().getLga().getId().equals(lga.getId()))
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

    public Long getSenatorialDistrictPollingUnitsWithResult(SenatorialDistrict senatorialDistrict){
        List<ResultPerParty> resultPerParties = resultPerPartyRepository.findAll();
        HashSet<String> pollingUnits = new HashSet<>();
        resultPerParties.stream()
                .filter(resultPerParty -> resultPerParty.getResult().getSenatorialDistrict().getId().equals(senatorialDistrict.getId()))
                .forEach(resultPerParty -> pollingUnits.add(resultPerParty.getResult().getPollingUnit().getCode()));
        return (long) pollingUnits.size();
    }

    public Long getLgaPollingUnitsWithResult(Lga lga){
        List<ResultPerParty> resultPerParties = resultPerPartyRepository.findAll();
        HashSet<String> pollingUnits = new HashSet<>();
        resultPerParties.stream()
                .filter(resultPerParty -> resultPerParty.getResult().getLga().getId().equals(lga.getId()))
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

    public Long getSenatorialDistrictWardsWithResult(SenatorialDistrict senatorialDistrict){
        List<ResultPerParty> resultPerParties = resultPerPartyRepository.findAll();
        HashSet<String> wards = new HashSet<>();
        resultPerParties.stream()
                .filter(resultPerParty -> resultPerParty.getResult().getSenatorialDistrict().getId().equals(senatorialDistrict.getId()))
                .forEach(resultPerParty -> wards.add(resultPerParty.getResult().getWard().getCode()));
        return (long) wards.size();
    }

    public Long getWardsWithResult(Lga lga){
        List<ResultPerParty> resultPerParties = resultPerPartyRepository.findAll();
        HashSet<String> wards = new HashSet<>();
        resultPerParties.stream()
                .filter(resultPerParty -> resultPerParty.getResult().getLga().getId().equals(lga.getId()))
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

    public Long getSenatorialDistrcitLgasWithResult(SenatorialDistrict senatorialDistrict){
        List<ResultPerParty> resultPerParties = resultPerPartyRepository.findAll();
        HashSet<String> lga = new HashSet<>();
        resultPerParties.stream()
                .filter(resultPerParty -> resultPerParty.getResult().getSenatorialDistrict().getId().equals(senatorialDistrict.getId()))
                .forEach(resultPerParty -> lga.add(resultPerParty.getResult().getLga().getCode()));
        return (long) lga.size();
    }

    public Long getLgasWithResult(Lga lga){
        List<ResultPerParty> resultPerParties = resultPerPartyRepository.findAll();
        HashSet<String> lgaSet = new HashSet<>();
        resultPerParties.stream()
                .filter(resultPerParty -> resultPerParty.getResult().getLga().getId().equals(lga.getId()))
                .forEach(resultPerParty -> lgaSet.add(resultPerParty.getResult().getLga().getCode()));
        return (long) lgaSet.size();
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
