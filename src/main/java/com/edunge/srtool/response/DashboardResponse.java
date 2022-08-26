package com.edunge.srtool.response;

import com.edunge.srtool.model.LgaResult;
import com.edunge.srtool.model.PartyLgaResult;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashboardResponse extends BaseResponse{
    Long totalStates;
    Long totalLgas;
    Long totalSenatorialDistricts;
    Integer totalRegisteredVotes;
    Integer totalAccreditedVotes;
    Integer totalVoteCounts;
    Integer totalVoidVotes;
    Long totalWards;
    Long totalPollingUnits;
    Long totalPoliticalParties;
    Double resultReceived;
    Long lgaWithResults;
    Long wardsWithResults;
    Long pollingUnitsWithResults;
    List<LgaResult> lgaResults;
    List<PartyResult> partyResult;
    List<PartyLgaResult> partyLgaResults;

    public DashboardResponse(String code, String message, Long totalStates, Long totalLgas, Long totalSenatorialDistricts, Integer totalRegisteredVotes, Integer totalAccreditedVotes, Integer totalVoteCounts, Long totalWards, Long totalPollingUnits) {
        super(code, message);
        this.totalStates = totalStates;
        this.totalLgas = totalLgas;
        this.totalSenatorialDistricts = totalSenatorialDistricts;
        this.totalRegisteredVotes = totalRegisteredVotes;
        this.totalAccreditedVotes = totalAccreditedVotes;
        this.totalVoteCounts = totalVoteCounts;
        this.totalWards = totalWards;
        this.totalPollingUnits = totalPollingUnits;
    }

    public DashboardResponse(String code, String message, Long totalStates, Long totalLgas, Long totalSenatorialDistricts, Integer totalRegisteredVotes, Integer totalAccreditedVotes, Integer totalVoidVotes, Integer totalVoteCounts, Long totalWards, Long totalPollingUnits, Long lgaWithResults, Long wardsWithResults, Long pollingUnitsWithResults, Double resultsReceived, List<PartyResult> partyResults, List<LgaResult> lgaResults, List<PartyLgaResult> partyLgaResults) {
        super(code, message);
        this.totalStates = totalStates;
        this.totalLgas = totalLgas;
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
        this.partyResult = partyResults;
        this.lgaResults = lgaResults;
        this.partyLgaResults = partyLgaResults;
    }

    public List<LgaResult> getLgaResults() {
        return lgaResults;
    }

    public void setLgaResults(List<LgaResult> lgaResults) {
        this.lgaResults = lgaResults;
    }

    public DashboardResponse(String code, String message, Long totalStates, Long totalLgas, Long totalSenatorialDistricts, Integer totalRegisteredVotes, Integer totalAccreditedVotes, Integer totalVoteCounts, Long totalWards, Long totalPollingUnits, Double resultsReceived) {
        super(code, message);
        this.totalStates = totalStates;
        this.totalLgas = totalLgas;
        this.totalSenatorialDistricts = totalSenatorialDistricts;
        this.totalRegisteredVotes = totalRegisteredVotes;
        this.totalAccreditedVotes = totalAccreditedVotes;
        this.totalVoteCounts = totalVoteCounts;
        this.totalWards = totalWards;
        this.totalPollingUnits = totalPollingUnits;
        this.resultReceived = resultsReceived;
    }

    public DashboardResponse() {
    }

    public DashboardResponse(String code, String message, Long totalStates, Long totalLgas, Long totalSenatorialDistricts, Integer totalRegisteredVotes, Integer totalAccreditedVotes, Integer totalVoteCounts, Long totalWards, Long totalPollingUnits, Long lgaWithResults, Long wardsWithResults, Long pollingUnitsWithResults, Double resultsReceived, List<PartyResult> partyResult) {
        super(code, message);
        this.totalStates = totalStates;
        this.totalLgas = totalLgas;
        this.totalSenatorialDistricts = totalSenatorialDistricts;
        this.totalRegisteredVotes = totalRegisteredVotes;
        this.totalAccreditedVotes = totalAccreditedVotes;
        this.totalVoteCounts = totalVoteCounts;
        this.totalWards = totalWards;
        this.totalPollingUnits = totalPollingUnits;
        this.resultReceived = resultsReceived;
        this.lgaWithResults = lgaWithResults;
        this.wardsWithResults = wardsWithResults;
        this.pollingUnitsWithResults = pollingUnitsWithResults;
        this.partyResult = partyResult;
    }

    public DashboardResponse(String code, String message, Long totalStates, Long totalLgas, Long totalSenatorialDistricts, Integer totalRegisteredVotes, Integer totalAccreditedVotes, Integer totalVoteCounts, Long totalWards, Long totalPollingUnits, Long lgaWithResults, Long wardsWithResults, Long pollingUnitsWithResults, Double resultsReceived, List<PartyResult> partyResult, List<LgaResult> lgaResults) {
        super(code, message);
        this.totalStates = totalStates;
        this.totalLgas = totalLgas;
        this.totalSenatorialDistricts = totalSenatorialDistricts;
        this.totalRegisteredVotes = totalRegisteredVotes;
        this.totalAccreditedVotes = totalAccreditedVotes;
        this.totalVoteCounts = totalVoteCounts;
        this.totalWards = totalWards;
        this.totalPollingUnits = totalPollingUnits;
        this.resultReceived = resultsReceived;
        this.lgaWithResults = lgaWithResults;
        this.wardsWithResults = wardsWithResults;
        this.pollingUnitsWithResults = pollingUnitsWithResults;
        this.partyResult = partyResult;
        this.lgaResults = lgaResults;
    }

    public Long getTotalStates() {
        return totalStates;
    }

    public void setTotalStates(Long totalStates) {
        this.totalStates = totalStates;
    }

    public Long getTotalLgas() {
        return totalLgas;
    }

    public void setTotalLgas(Long totalLgas) {
        this.totalLgas = totalLgas;
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

    public Long getWardsWithResults() {
        return wardsWithResults;
    }

    public Long getPollingUnitsWithResults() {
        return pollingUnitsWithResults;
    }

    public List<PartyLgaResult> getPartyLgaResults() {
        return partyLgaResults;
    }

    public Integer getTotalVoidVotes() {
        return totalVoidVotes;
    }

    public void setTotalVoidVotes(Integer totalVoidVotes) {
        this.totalVoidVotes = totalVoidVotes;
    }
}
