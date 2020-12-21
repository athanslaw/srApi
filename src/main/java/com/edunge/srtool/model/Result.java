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
}
