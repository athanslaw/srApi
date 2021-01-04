package com.edunge.srtool.response;

import com.edunge.srtool.model.PoliticalParty;
import com.edunge.srtool.model.ResultPerParty;

public class PartyResult {
    private PoliticalParty politicalParty;
    private ResultPerParty resultPerParty;
    private Integer totalVoteCount;
    private Double percent;

    public PoliticalParty getPoliticalParty() {
        return politicalParty;
    }

    public void setPoliticalParty(PoliticalParty politicalParty) {
        this.politicalParty = politicalParty;
    }

    public ResultPerParty getResultPerParty() {
        return resultPerParty;
    }

    public void setResultPerParty(ResultPerParty resultPerParty) {
        this.resultPerParty = resultPerParty;
    }

    public Integer getTotalVoteCount() {
        return totalVoteCount;
    }

    public void setTotalVoteCount(Integer totalVoteCount) {
        this.totalVoteCount = totalVoteCount;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }
}
