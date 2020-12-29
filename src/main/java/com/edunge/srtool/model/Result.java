package com.edunge.srtool.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Result extends AbstractElectionDetails {
    @ManyToOne
    @JoinColumn(name = "party_agent_id",nullable = false)
    private PartyAgent partyAgent;

    private Integer accreditedVotersCount;
    private Integer registeredVotersCount;

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
}
