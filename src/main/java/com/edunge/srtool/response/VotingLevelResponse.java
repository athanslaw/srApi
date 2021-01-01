package com.edunge.srtool.response;

import com.edunge.srtool.model.VotingLevel;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VotingLevelResponse extends BaseResponse {
    VotingLevel votingLevel;
    List<VotingLevel> votingLevels;
    Integer count;

    public Integer getCount() {
        return count;
    }

    public VotingLevelResponse(String code, String message, VotingLevel politicalParty) {
        super(code, message);
        this.votingLevel = politicalParty;
    }

    public VotingLevelResponse(VotingLevel politicalParty) {
        this.votingLevel = politicalParty;
    }

    public VotingLevelResponse(String code, String message) {
        super(code, message);
    }

    public VotingLevelResponse(String code, String message, List<VotingLevel> objects) {
        super(code, message);
        this.votingLevels = objects;
        this.count = objects.size();
    }

    public VotingLevel getVotingLevel() {
        return votingLevel;
    }

    public void setVotingLevel(VotingLevel votingLevel) {
        this.votingLevel = votingLevel;
    }

    public List<VotingLevel> getVotingLevels() {
        return votingLevels;
    }

    public void setVotingLevels(List<VotingLevel> votingLevels) {
        this.votingLevels = votingLevels;
    }
}
