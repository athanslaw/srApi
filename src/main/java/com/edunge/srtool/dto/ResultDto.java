package com.edunge.srtool.dto;

import com.edunge.srtool.model.AbstractBaseModel;

public class ResultDto extends AbstractBaseModel {
    private Long electionId, votingLevelId, partyAgentId, senatorialDistrictId, lgaId, wardId, PollingUnitId;
    private Integer registeredVotersCount, accreditedCotersCount;

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

    public Integer getAccreditedCotersCount() {
        return accreditedCotersCount;
    }

    public void setAccreditedCotersCount(Integer accreditedCotersCount) {
        this.accreditedCotersCount = accreditedCotersCount;
    }
}
