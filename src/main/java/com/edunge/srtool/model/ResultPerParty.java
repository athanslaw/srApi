package com.edunge.srtool.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ResultPerParty extends BaseModel{
    @ManyToOne
    @JoinColumn(name = "result_id",nullable = false)
    private Result result;

    @ManyToOne
    @JoinColumn(name = "political_party_id",nullable = false)
    private PoliticalParty politicalParty;

    private Integer voteCount;

}
