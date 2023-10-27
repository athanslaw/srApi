package com.edunge.bukinz.dto;

import com.edunge.bukinz.model.BaseModel;

import javax.validation.constraints.NotNull;

public class ResultPerPartyDto extends BaseModel {
    @NotNull
    private Long resultId;
    @NotNull
    private Long politicalPartyId;
    @NotNull
    private Integer voteCount;

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

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }
}
