package com.edunge.srtool.model;

public class PartyStateResult {
    private String partyName;
    private Integer stateCount;

    public PartyStateResult(String party, Integer stateWon) {
        this.partyName = party;
        this.stateCount = stateWon;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public Integer getStateCount() {
        return stateCount;
    }

    public void setStateCount(Integer stateCount) {
        this.stateCount = stateCount;
    }
}
