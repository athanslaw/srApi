package com.edunge.srtool.response;

import com.edunge.srtool.model.LgaResult;
import com.edunge.srtool.model.PartyLgaResult;
import com.edunge.srtool.model.PartyStateResult;
import com.edunge.srtool.model.StateResult;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NationalDashboardResponse extends BaseResponse{
    Long totalStates;
    Long totalGeopoliticalZones;
    Long totalSenatorialDistricts;
    Integer totalRegisteredVotes;
    Integer totalAccreditedVotes;
    Integer totalVoteCounts;
    Integer totalVoidVotes;
    Long totalWards;
    Long totalLgas;
    Long totalPollingUnits;
    Long totalPoliticalParties;
    Double resultReceived;
    Long lgaWithResults;
    Long geoPoliticalZonesWithResults;
    Long statesWithResults;
    List<StateResult> stateResults;
    Long wardsWithResults;
    Long pollingUnitsWithResults;
    List<PartyResult> partyResult;
    List<PartyStateResult> partyStatesResults;


    public NationalDashboardResponse(String code, String message, Long totalGeopoliticalZones, Long totalStates, Long totalSenatorialDistricts,
                                     Integer totalRegisteredVotes, Integer totalAccreditedVotes, Integer totalVoidVotes, Integer totalVoteCounts, Long totalLgas,
                                     Long totalPollingUnits, Long geoPoliticalZonesWithResults, Long statesWithResults, Long lgaWithResults,
                                     Long wardsWithResults, Long pollingUnitsWithResults,
                                     Double resultsReceived, List<PartyResult> partyResults, List<StateResult> stateResults,
                                     List<PartyStateResult> partyStateResults) {
        super(code, message);
        this.totalStates = totalStates;
        this.totalGeopoliticalZones = totalGeopoliticalZones;
        this.totalSenatorialDistricts = totalSenatorialDistricts;
        this.totalRegisteredVotes = totalRegisteredVotes;
        this.totalAccreditedVotes = totalAccreditedVotes;
        this.totalVoteCounts = totalVoteCounts;
        this.totalVoidVotes = totalVoidVotes;
        this.totalWards = totalWards;
        this.totalPollingUnits = totalPollingUnits;
        this.resultReceived = resultsReceived;
        this.lgaWithResults = lgaWithResults;
        this.wardsWithResults = wardsWithResults;
        this.pollingUnitsWithResults = pollingUnitsWithResults;
        this.totalLgas = totalLgas;
        this.statesWithResults = statesWithResults;
        this.geoPoliticalZonesWithResults = geoPoliticalZonesWithResults;
        this.partyResult = partyResults;
        this.stateResults = stateResults;
        this.partyStatesResults = partyStateResults;
    }

    public List<StateResult> getStateResults() {
        return stateResults;
    }

    public void setStateResults(List<StateResult> stateResults) {
        this.stateResults = stateResults;
    }

    public NationalDashboardResponse() {
    }

    public Long getTotalStates() {
        return totalStates;
    }

    public void setTotalStates(Long totalStates) {
        this.totalStates = totalStates;
    }

    public Long getTotalSenatorialDistricts() {
        return totalSenatorialDistricts;
    }

    public void setTotalSenatorialDistricts(Long totalSenatorialDistricts) {
        this.totalSenatorialDistricts = totalSenatorialDistricts;
    }

    public Integer getTotalRegisteredVotes() {
        return totalRegisteredVotes;
    }

    public void setTotalRegisteredVotes(Integer totalRegisteredVotes) {
        this.totalRegisteredVotes = totalRegisteredVotes;
    }

    public Integer getTotalAccreditedVotes() {
        return totalAccreditedVotes;
    }

    public void setTotalAccreditedVotes(Integer totalAccreditedVotes) {
        this.totalAccreditedVotes = totalAccreditedVotes;
    }

    public Integer getTotalVoteCounts() {
        return totalVoteCounts;
    }

    public void setTotalVoteCounts(Integer totalVoteCounts) {
        this.totalVoteCounts = totalVoteCounts;
    }

    public Long getTotalWards() {
        return totalWards;
    }

    public void setTotalWards(Long totalWards) {
        this.totalWards = totalWards;
    }

    public Long getTotalPollingUnits() {
        return totalPollingUnits;
    }

    public void setTotalPollingUnits(Long totalPollingUnits) {
        this.totalPollingUnits = totalPollingUnits;
    }

    public Long getTotalPoliticalParties() {
        return totalPoliticalParties;
    }

    public void setTotalPoliticalParties(Long totalPoliticalParties) {
        this.totalPoliticalParties = totalPoliticalParties;
    }

    public Double getResultReceived() {
        return resultReceived;
    }

    public void setResultReceived(Double resultReceived) {
        this.resultReceived = resultReceived;
    }

    public List<PartyResult> getPartyResult() {
        return partyResult;
    }

    public void setPartyResult(List<PartyResult> partyResult) {
        this.partyResult = partyResult;
    }

    public Long getLgaWithResults() {
        return lgaWithResults;
    }

    public Long getTotalGeopoliticalZones() {
        return totalGeopoliticalZones;
    }

    public void setTotalGeopoliticalZones(Long totalGeopoliticalZones) {
        this.totalGeopoliticalZones = totalGeopoliticalZones;
    }

    public Long getGeoPoliticalZonesWithResults() {
        return geoPoliticalZonesWithResults;
    }

    public void setGeoPoliticalZonesWithResults(Long geoPoliticalZonesWithResults) {
        this.geoPoliticalZonesWithResults = geoPoliticalZonesWithResults;
    }

    public Long getStatesWithResults() {
        return statesWithResults;
    }

    public void setStatesWithResults(Long statesWithResults) {
        this.statesWithResults = statesWithResults;
    }

    public List<PartyStateResult> getPartyStatesResults() {
        return partyStatesResults;
    }

    public void setPartyStatesResults(List<PartyStateResult> partyStatesResults) {
        this.partyStatesResults = partyStatesResults;
    }

    public void setLgaWithResults(Long lgaWithResults) {
        this.lgaWithResults = lgaWithResults;
    }

    public Long getWardsWithResults() {
        return wardsWithResults;
    }

    public void setWardsWithResults(Long wardsWithResults) {
        this.wardsWithResults = wardsWithResults;
    }

    public Long getPollingUnitsWithResults() {
        return pollingUnitsWithResults;
    }

    public void setPollingUnitsWithResults(Long pollingUnitsWithResults) {
        this.pollingUnitsWithResults = pollingUnitsWithResults;
    }

    public Integer getTotalVoidVotes() {
        return totalVoidVotes;
    }

    public void setTotalVoidVotes(Integer totalVoidVotes) {
        this.totalVoidVotes = totalVoidVotes;
    }
}
