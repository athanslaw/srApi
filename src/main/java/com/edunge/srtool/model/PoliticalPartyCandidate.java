package com.edunge.srtool.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class PoliticalPartyCandidate extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "election_id",nullable = false)
    private Election election;

    @ManyToOne
    @JoinColumn(name = "political_party_id",nullable = false)
    private PoliticalParty politicalParty;

    private String firstName;
    private String lastName;
    private String imageUrl;

}
