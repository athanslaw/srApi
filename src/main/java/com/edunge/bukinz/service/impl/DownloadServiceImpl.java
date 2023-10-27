package com.edunge.bukinz.service.impl;

import com.edunge.bukinz.model.Result;
import com.edunge.bukinz.model.ResultPerParty;
import com.edunge.bukinz.service.DownloadService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class DownloadServiceImpl implements DownloadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadService.class);

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


    public static class ResultDownload{
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
        private String partyOne;
        private String partyOneVotes;
        private String partyTwo;
        private String partyTwoVotes;
        private String partyThree;
        private String partyThreeVotes;
        private String others;
        private String othersVote;

        public Integer getAccreditedVotersCount() {
            return accreditedVotersCount;
        }

        public String getPartyOne() {
            return partyOne;
        }

        public void setPartyOne(String partyOne) {
            this.partyOne = partyOne;
        }

        public String getPartyOneVotes() {
            return partyOneVotes;
        }

        public void setPartyOneVotes(String partyOneVotes) {
            this.partyOneVotes = partyOneVotes;
        }

        public String getPartyTwo() {
            return partyTwo;
        }

        public void setPartyTwo(String partyTwo) {
            this.partyTwo = partyTwo;
        }

        public String getPartyTwoVotes() {
            return partyTwoVotes;
        }

        public void setPartyTwoVotes(String partyTwoVotes) {
            this.partyTwoVotes = partyTwoVotes;
        }

        public String getPartyThree() {
            return partyThree;
        }

        public void setPartyThree(String partyThree) {
            this.partyThree = partyThree;
        }

        public String getPartyThreeVotes() {
            return partyThreeVotes;
        }

        public void setPartyThreeVotes(String partyThreeVotes) {
            this.partyThreeVotes = partyThreeVotes;
        }

        public String getOthers() {
            return others;
        }

        public void setOthers(String others) {
            this.others = others;
        }

        public String getOthersVote() {
            return othersVote;
        }

        public void setOthersVote(String othersVote) {
            this.othersVote = othersVote;
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


    public static void writeResults(PrintWriter writer, List<ResultDownload> results){
        try{
            ColumnPositionMappingStrategy<ResultDownload> mappingStrategy = new ColumnPositionMappingStrategy<>();
            mappingStrategy.setType(ResultDownload.class);
            String[] columns = new String[]{"accreditedVotersCount",
                    "registeredVotersCount",
                    "firstname",
                    "lastname",
                    "phone",
                    "senatorialDistrictCode",
                    "senatorialDistrictName",
                    "lgaCode",
                    "lgaName",
                    "wardCode",
                    "wardName",
                    "pollingUnitCode",
                    "pollingUnitName",
                    "votingLevel",
                    "partyOne",
                    "partyOneVotes",
                    "partyTwo",
                    "partyTwoVotes",
                    "partyThree",
                    "partyThreeVotes",
                    "others",
                    "othersVote"};
            mappingStrategy.setColumnMapping(columns);
            mappingStrategy.generateHeader();
            String headerFromArray = Arrays.toString(columns).substring(1,Arrays.toString(columns).length()-1);
            writer.println(headerFromArray);
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withMappingStrategy(mappingStrategy)
                    .withSeparator(',')
                    .build();
            beanToCsv.write(results);
        }
        catch (CsvException ex){
            LOGGER.error("Error mapping CSV", ex);
        }
    }

}
