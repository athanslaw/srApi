package com.edunge.srtool.response;

import com.edunge.srtool.model.PoliticalParty;
import com.edunge.srtool.model.ResultPerParty;

public class PartyResult {
    private PoliticalParty politicalParty;
    private ResultPerParty resultPerParty;
    private Integer totalVoteCount;
    private Integer percent;

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

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }
}
