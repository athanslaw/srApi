package com.edunge.bukinz.model;

public class PartyLgaResult {
    private String partyName;
    private Integer lgaCount;

    public PartyLgaResult(String party, Integer lgawon) {
        this.partyName = party;
        this.lgaCount = lgawon;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public Integer getLgaCount() {
        return lgaCount;
    }

    public void setLgaCount(Integer lgaCount) {
        this.lgaCount = lgaCount;
    }
}
