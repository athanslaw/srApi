package com.edunge.srtool.dto;

import com.edunge.srtool.model.AbstractBaseModel;

public class ResultPerPartyDto extends AbstractBaseModel {
    private Long resultId, politicalPartyId, voteCount;

    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    public Long getPoliticalPartyId() {
        return politicalPartyId;
    }

    public void setPoliticalPartyId(Long politicalPartyId) {
        this.politicalPartyId = politicalPartyId;
    }

    public Long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Long voteCount) {
        this.voteCount = voteCount;
    }
}
