package com.edunge.srtool.response;

import com.edunge.srtool.model.Election;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ElectionResponse extends BaseResponse{
    Election election;
    List<Election> elections;

    public ElectionResponse(String code, String message, Election election) {
        super(code, message);
        this.election = election;
    }

    public ElectionResponse(Election election) {
        this.election = election;
    }

    public ElectionResponse(String code, String message) {
        super(code, message);
    }

    public ElectionResponse(String code, String message, List<Election> elections) {
        super(code, message);
        this.elections = elections;
    }

    public Election getElection() {
        return election;
    }

    public void setElection(Election election) {
        this.election = election;
    }

    public List<Election> getElections() {
        return elections;
    }

    public void setElections(List<Election> elections) {
        this.elections = elections;
    }
}
