package com.edunge.srtool.response;

import com.edunge.srtool.model.PoliticalPartyCandidate;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PoliticalPartyCandidateResponse extends BaseResponse{
    PoliticalPartyCandidate politicalPartyCandidate;
    List<PoliticalPartyCandidate> politicalPartyCandidates;
    Integer count;

    public Integer getCount() {
        return count;
    }

    public PoliticalPartyCandidateResponse(String code, String message, PoliticalPartyCandidate politicalPartyCandidate) {
        super(code, message);
        this.politicalPartyCandidate = politicalPartyCandidate;
    }

    public PoliticalPartyCandidateResponse(PoliticalPartyCandidate politicalPartyCandidate) {
        this.politicalPartyCandidate = politicalPartyCandidate;
    }

    public PoliticalPartyCandidateResponse(String code, String message) {
        super(code, message);
    }

    public PoliticalPartyCandidateResponse(String code, String message, List<PoliticalPartyCandidate> politicalPartyCandidates) {
        super(code, message);
        this.politicalPartyCandidates = politicalPartyCandidates;
        this.count = politicalPartyCandidates.size();
    }

    public PoliticalPartyCandidate getPoliticalPartyCandidate() {
        return politicalPartyCandidate;
    }

    public void setPoliticalPartyCandidate(PoliticalPartyCandidate politicalPartyCandidate) {
        this.politicalPartyCandidate = politicalPartyCandidate;
    }

    public List<PoliticalPartyCandidate> getPoliticalPartyCandidates() {
        return politicalPartyCandidates;
    }

    public void setPoliticalPartyCandidates(List<PoliticalPartyCandidate> politicalPartyCandidates) {
        this.politicalPartyCandidates = politicalPartyCandidates;
    }
}
