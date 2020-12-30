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

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public PoliticalParty getPoliticalParty() {
        return politicalParty;
    }

    public void setPoliticalParty(PoliticalParty politicalParty) {
        this.politicalParty = politicalParty;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }
}
