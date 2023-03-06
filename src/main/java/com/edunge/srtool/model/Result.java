package com.edunge.srtool.model;

import com.edunge.srtool.util.Utilities;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Result extends AbstractElectionDetails {
    @ManyToOne
    @JoinColumn(name = "party_agent_id",nullable = false)
    private PartyAgent partyAgent;

    @OneToMany(mappedBy = "result", fetch = FetchType.LAZY)
    Set<ResultPerParty> resultPerParties;

    private Integer accreditedVotersCount;
    private Integer registeredVotersCount;
    private Integer pollingUnitCount;
    private Integer voidVotes;


    public String getElectionTypeName() {
        return Utilities.electionTypeArray[Math.toIntExact(super.getElectionType())];
    }

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

    public Set<ResultPerParty> getResultPerParties() {
        return resultPerParties.stream().sorted((o1, o2) -> o1.getPoliticalParty().getName().compareTo(o2.getPoliticalParty().getName())).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public void setResultPerParties(Set<ResultPerParty> resultPerParties) {
        this.resultPerParties = resultPerParties;
    }

    public Integer getPollingUnitCount() {
        return pollingUnitCount;
    }

    public void setPollingUnitCount(Integer pollingUnitCount) {
        this.pollingUnitCount = pollingUnitCount;
    }

    public Integer getVoidVotes() {
        return voidVotes;
    }

    public void setVoidVotes(Integer voidVotes) {
        this.voidVotes = voidVotes;
    }
}
