package com.edunge.srtool.dto;

import com.edunge.srtool.model.AbstractBaseModel;

import javax.validation.constraints.NotNull;

public class ResultDto extends AbstractBaseModel {
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
    private Integer registeredVotersCount;
    @NotNull
    private Integer  accreditedVotersCount;
    private Integer apc, pdp, anpp, others;

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

    public Integer getRegisteredVotersCount() {
        return registeredVotersCount;
    }

    public void setRegisteredVotersCount(Integer registeredVotersCount) {
        this.registeredVotersCount = registeredVotersCount;
    }

    public Integer getAccreditedVotersCount() {
        return accreditedVotersCount;
    }

    public void setAccreditedVotersCount(Integer accreditedVotersCount) {
        this.accreditedVotersCount = accreditedVotersCount;
    }

    public Integer getApc() {
        return apc;
    }

    public void setApc(Integer apc) {
        this.apc = apc;
    }

    public Integer getPdp() {
        return pdp;
    }

    public void setPdp(Integer pdp) {
        this.pdp = pdp;
    }

    public Integer getAnpp() {
        return anpp;
    }

    public void setAnpp(Integer anpp) {
        this.anpp = anpp;
    }

    public Integer getOthers() {
        return others;
    }

    public void setOthers(Integer others) {
        this.others = others;
    }
}
