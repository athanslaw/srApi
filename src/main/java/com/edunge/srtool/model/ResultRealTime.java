package com.edunge.srtool.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ResultRealTime extends AbstractElectionDetails {
    @ManyToOne
    @JoinColumn(name = "party_agent_id",nullable = false)
    private PartyAgent partyAgent;

    private Integer accreditedVotersCount;
    private Integer registeredVotersCount;
    private Integer party_1;
    private Integer party_2;
    private Integer party_3;
    private Integer party_4;
    private Integer voteCount;
    private Long result;
    private Integer pollingUnitCount;

    public PartyAgent getPartyAgent() {
        return partyAgent;
    }

    public void setPartyAgent(PartyAgent partyAgent) {
        this.partyAgent = partyAgent;
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

    public Integer getPollingUnitCount() {
        return pollingUnitCount;
    }

    public void setPollingUnitCount(Integer pollingUnitCount) {
        this.pollingUnitCount = pollingUnitCount;
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

    public Long getResult() {
        return result;
    }

    public void setResult(Long result) {
        this.result = result;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public String toString() {
        return "ResultRealTime{" +
                "partyAgent=" + partyAgent +
                ", accreditedVotersCount=" + accreditedVotersCount +
                ", registeredVotersCount=" + registeredVotersCount +
                ", party_1=" + party_1 +
                ", party_2=" + party_2 +
                ", party_3=" + party_3 +
                ", party_4=" + party_4 +
                ", voteCount=" + voteCount +
                ", result=" + result +
                ", pollingUnitCount=" + pollingUnitCount +
                '}';
    }
}
