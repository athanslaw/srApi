package com.edunge.srtool.model;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractElectionDetails extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "election_id",nullable = false)
    private Election election;

    @ManyToOne
    @JoinColumn(name = "party_agent_id",nullable = false)
    private PartyAgent partyAgent;

    @ManyToOne
    @JoinColumn(name = "senatorial_district_id",nullable = false)
    private SenatorialDistrict senatorialDistrict;

    @ManyToOne
    @JoinColumn(name = "lga_id",nullable = false)
    private Lga lga;

    @ManyToOne
    @JoinColumn(name = "ward_id",nullable = false)
    private Ward ward;

    @ManyToOne
    @JoinColumn(name = "polling_unit_id",nullable = false)
    private PollingUnit pollingUnit;

    @ManyToOne
    @JoinColumn(name = "voting_level_id",nullable = false)
    private VotingLevel votingLevel;
}
