package com.edunge.srtool.model;

import com.edunge.srtool.response.PartyResult;

import java.util.List;

public class StateResult {
    private State state;
    private List<PartyResult> partyResults;

    public StateResult(State state, List<PartyResult> partyResults) {
        this.state = state;
        this.partyResults = partyResults;
    }

    public State getState() {
        return state;
    }

    public List<PartyResult> getPartyResults() {
        return partyResults;
    }
}
