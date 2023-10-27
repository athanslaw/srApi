package com.edunge.bukinz.dto;

import com.edunge.bukinz.model.AbstractBaseModel;

import javax.validation.constraints.NotNull;

public class ResultDto extends AbstractBaseModel {
    @NotNull
    private Long electionId;
    @NotNull
    private Long votingLevelId;

    private Long partyAgentId;
    @NotNull
    private Long  senatorialDistrictId;

    private Long  lgaId;

    private Long  wardId;

    private Long  PollingUnitId;
    private Long  electionType;
    @NotNull
    private Integer registeredVotersCount;
    @NotNull
    private Integer  accreditedVotersCount;
    private Integer party_1, party_2, party_3, party_4, party_5, party_6, result_id;
    private Integer voidVotes;

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

    public Integer getParty_1() {
        return party_1;
    }

    public void setParty_1(Integer party_1) {
        this.party_1 = party_1;
    }

    public Integer getParty_2() {
        return party_2;
    }

    public void setParty_2(Integer party_2) {
        this.party_2 = party_2;
    }

    public Integer getParty_3() {
        return party_3;
    }

    public void setParty_3(Integer party_3) {
        this.party_3 = party_3;
    }

    public Integer getParty_4() {
        return party_4;
    }

    public void setParty_4(Integer party_4) {
        this.party_4 = party_4;
    }

    public Integer getParty_5() {
        return party_5;
    }

    public void setParty_5(Integer party_5) {
        this.party_5 = party_5;
    }

    public Integer getParty_6() {
        return party_6;
    }

    public void setParty_6(Integer party_6) {
        this.party_6 = party_6;
    }

    public Integer getResult_id() {
        return result_id;
    }

    public void setResult_id(Integer result_id) {
        this.result_id = result_id;
    }

    public Long getElectionType() {
        return electionType;
    }

    public void setElectionType(Long electionType) {
        this.electionType = electionType;
    }

    public Integer getVoidVotes() {
        return voidVotes;
    }

    public void setVoidVotes(Integer voidVotes) {
        this.voidVotes = voidVotes;
    }
}
