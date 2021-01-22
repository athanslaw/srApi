package com.edunge.srtool.service.impl;

import com.edunge.srtool.model.Result;
import com.edunge.srtool.repository.ElectionRepository;
import com.edunge.srtool.repository.ResultRepository;
import com.edunge.srtool.service.DownloadService;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class DownloadServiceImpl implements DownloadService {
    private final ElectionRepository electionRepository;
    private final ResultRepository resultRepository;
    private final ResultPerPartyServiceImpl resultPerPartyService;

    public DownloadServiceImpl(ElectionRepository electionRepository, ResultRepository resultRepository, ResultPerPartyServiceImpl resultPerPartyService) {
        this.electionRepository = electionRepository;
        this.resultRepository = resultRepository;
        this.resultPerPartyService = resultPerPartyService;
    }
    private static class ElectionResult{
        private final String partyName;
        private final String partyVote;

        public ElectionResult(String name, String voteCount) {
            this.partyName = name;
            this.partyVote = voteCount;
        }

        public String getPartyName() {
            return partyName;
        }

        public String getPartyVote() {
            return partyVote;
        }
    }


    private static class ResultDownload{
        private Integer accreditedVotersCount;
        private Integer registeredVotersCount;
        private String firstname;
        private String lastname;
        private String phone;
        private String senatorialDistrictCode;
        private String senatorialDistrictName;
        private String lgaCode;
        private String lgaName;
        private String wardCode;
        private String wardName;
        private String pollingUnitCode;
        private String pollingUnitName;
        private String votingLevel;
        private List<ElectionResult> electionResults;

        public List<ElectionResult> getElectionResults() {
            return electionResults;
        }

        public void setElectionResults(List<ElectionResult> electionResults) {
            this.electionResults = electionResults;
        }

        public Integer getAccreditedVotersCount() {
            return accreditedVotersCount;
        }

        public void setAccreditedVotersCount(Integer accreditedVotersCount) {
            this.accreditedVotersCount = accreditedVotersCount;
        }

        public Integer getRegisteredVotersCount() {
            return registeredVotersCount;
        }

        public void setRegisteredVotersCount(Integer registeredVotersCount) {
            this.registeredVotersCount = registeredVotersCount;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getSenatorialDistrictCode() {
            return senatorialDistrictCode;
        }

        public void setSenatorialDistrictCode(String senatorialDistrictCode) {
            this.senatorialDistrictCode = senatorialDistrictCode;
        }

        public String getSenatorialDistrictName() {
            return senatorialDistrictName;
        }

        public void setSenatorialDistrictName(String senatorialDistrictName) {
            this.senatorialDistrictName = senatorialDistrictName;
        }

        public String getLgaCode() {
            return lgaCode;
        }

        public void setLgaCode(String lgaCode) {
            this.lgaCode = lgaCode;
        }

        public String getLgaName() {
            return lgaName;
        }

        public void setLgaName(String lgaName) {
            this.lgaName = lgaName;
        }

        public String getWardCode() {
            return wardCode;
        }

        public void setWardCode(String wardCode) {
            this.wardCode = wardCode;
        }

        public String getWardName() {
            return wardName;
        }

        public void setWardName(String wardName) {
            this.wardName = wardName;
        }

        public String getPollingUnitCode() {
            return pollingUnitCode;
        }

        public void setPollingUnitCode(String pollingUnitCode) {
            this.pollingUnitCode = pollingUnitCode;
        }

        public String getPollingUnitName() {
            return pollingUnitName;
        }

        public void setPollingUnitName(String pollingUnitName) {
            this.pollingUnitName = pollingUnitName;
        }

        public String getVotingLevel() {
            return votingLevel;
        }

        public void setVotingLevel(String votingLevel) {
            this.votingLevel = votingLevel;
        }
    }

    private List<ResultDownload> getResults(){
        List<Result> results = resultRepository.findAll();
        List<ResultDownload> resultDownloads = new ArrayList<>();
        results.forEach(result -> {
            ResultDownload resultDownload = new ResultDownload();
            resultDownload.setAccreditedVotersCount(result.getAccreditedVotersCount());
            resultDownload.setRegisteredVotersCount(result.getRegisteredVotersCount());
            resultDownload.setFirstname(result.getPartyAgent().getFirstname());
            resultDownload.setLastname(result.getPartyAgent().getLastname());
            resultDownload.setPhone(result.getPartyAgent().getPhone());
            resultDownload.setLgaCode(result.getLga().getCode());
            resultDownload.setLgaName(result.getLga().getName());
            resultDownload.setWardCode(result.getWard().getCode());
            resultDownload.setWardName(result.getWard().getName());
            resultDownload.setPollingUnitCode(result.getPollingUnit().getCode());
            resultDownload.setPollingUnitName(result.getPollingUnit().getName());
            resultDownload.setSenatorialDistrictCode(result.getSenatorialDistrict().getName());
            resultDownload.setSenatorialDistrictName(result.getSenatorialDistrict().getName());
            List<ElectionResult> electionResults = new ArrayList<>();
            result.getResultPerParties().forEach(resultPerParty -> {
                electionResults.add(new ElectionResult(resultPerParty.getPoliticalParty().getName(),resultPerParty.getVoteCount().toString()));
            });
            resultDownload.setElectionResults(electionResults);
        });
        return resultDownloads;
    }

    public static void writeResults(PrintWriter writer, List<ElectionResult> results){
        try{

        }
        catch (CsvException ex){

        }
    }

}
