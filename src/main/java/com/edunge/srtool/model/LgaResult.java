package com.edunge.srtool.model;

import com.edunge.srtool.response.PartyResult;

import java.util.List;

public class LgaResult{
    private Lga lga;
    private List<PartyResult> partyResults;

    public LgaResult(Lga lga, List<PartyResult> partyResults) {
        this.lga = lga;
        this.partyResults = partyResults;
    }

    public Lga getLga() {
        return lga;
    }

    public List<PartyResult> getPartyResults() {
        return partyResults;
    }
}
