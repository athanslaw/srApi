package com.edunge.srtool.dto;

import com.edunge.srtool.model.AbstractBaseModel;

import javax.validation.constraints.NotNull;

public class IncidentDto extends AbstractBaseModel {
    @NotNull
    private Long electionId;
    @NotNull
    private Long votingLevelId;
    @NotNull
    private Long partyAgentId;
    @NotNull
    private Long  senatorialDistrictId;
    @NotNull
    private Long  lgaId;
    @NotNull
    private Long  wardId;
    @NotNull
    private Long  PollingUnitId;
    @NotNull
    private String description;
    @NotNull
    private String reportedLocation;
    @NotNull
    private String phoneNumberToContact;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReportedLocation() {
        return reportedLocation;
    }

    public void setReportedLocation(String reportedLocation) {
        this.reportedLocation = reportedLocation;
    }

    public String getPhoneNumberToContact() {
        return phoneNumberToContact;
    }

    public void setPhoneNumberToContact(String phoneNumberToContact) {
        this.phoneNumberToContact = phoneNumberToContact;
    }

    public Long getElectionId() {
        return electionId;
    }

    public void setElectionId(Long electionId) {
        this.electionId = electionId;
    }

    public Long getVotingLevelId() {
        return votingLevelId;
    }

    public void setVotingLevelId(Long votingLevelId) {
        this.votingLevelId = votingLevelId;
    }

    public Long getPartyAgentId() {
        return partyAgentId;
    }

    public void setPartyAgentId(Long partyAgentId) {
        this.partyAgentId = partyAgentId;
    }

    public Long getSenatorialDistrictId() {
        return senatorialDistrictId;
    }

    public void setSenatorialDistrictId(Long senatorialDistrictId) {
        this.senatorialDistrictId = senatorialDistrictId;
    }

    public Long getLgaId() {
        return lgaId;
    }

    public void setLgaId(Long lgaId) {
        this.lgaId = lgaId;
    }

    public Long getWardId() {
        return wardId;
    }

    public void setWardId(Long wardId) {
        this.wardId = wardId;
    }

    public Long getPollingUnitId() {
        return PollingUnitId;
    }

    public void setPollingUnitId(Long pollingUnitId) {
        PollingUnitId = pollingUnitId;
    }
}
