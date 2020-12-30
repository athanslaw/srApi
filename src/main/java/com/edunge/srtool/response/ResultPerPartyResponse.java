package com.edunge.srtool.response;

import com.edunge.srtool.model.ResultPerParty;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultPerPartyResponse extends BaseResponse{
    ResultPerParty resultPerParty;
    List<ResultPerParty> resultPerParties;

    public ResultPerPartyResponse(String code, String message, ResultPerParty resultPerParty) {
        super(code, message);
        this.resultPerParty = resultPerParty;
    }

    public ResultPerPartyResponse(ResultPerParty resultPerParty) {
        this.resultPerParty = resultPerParty;
    }

    public ResultPerPartyResponse(String code, String message) {
        super(code, message);
    }

    public ResultPerPartyResponse(String code, String message, List<ResultPerParty> resultPerParties) {
        super(code, message);
        this.resultPerParties = resultPerParties;
    }

    public ResultPerParty getResultPerParty() {
        return resultPerParty;
    }

    public void setResultPerParty(ResultPerParty resultPerParty) {
        this.resultPerParty = resultPerParty;
    }

    public List<ResultPerParty> getResultPerParties() {
        return resultPerParties;
    }

    public void setResultPerParties(List<ResultPerParty> resultPerParties) {
        this.resultPerParties = resultPerParties;
    }
}
