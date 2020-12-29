package com.edunge.srtool.response;

import com.edunge.srtool.model.PoliticalParty;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PoliticalPartyResponse extends BaseResponse{
    PoliticalParty politicalParty;
    List<PoliticalParty> politicalParties;

    public PoliticalPartyResponse(String code, String message, PoliticalParty politicalParty) {
        super(code, message);
        this.politicalParty = politicalParty;
    }

    public PoliticalPartyResponse(PoliticalParty politicalParty) {
        this.politicalParty = politicalParty;
    }

    public PoliticalPartyResponse(String code, String message) {
        super(code, message);
    }

    public PoliticalPartyResponse(String code, String message, List<PoliticalParty> objects) {
        super(code, message);
        this.politicalParties = objects;
    }

    public PoliticalParty getPoliticalParty() {
        return politicalParty;
    }

    public void setPoliticalParty(PoliticalParty politicalParty) {
        this.politicalParty = politicalParty;
    }

    public List<PoliticalParty> getPoliticalParties() {
        return politicalParties;
    }

    public void setPoliticalParties(List<PoliticalParty> politicalParties) {
        this.politicalParties = politicalParties;
    }
}
