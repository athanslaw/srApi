package com.edunge.srtool.dto;

public class ConfigDto {

    private String activeState;
    private String activeYear;
    private String electionLevel;

    public String getActiveState() {
        return activeState;
    }

    public void setActiveState(String activeState) {
        this.activeState = activeState;
    }

    public String getActiveYear() {
        return activeYear;
    }

    public void setActiveYear(String activeYear) {
        this.activeYear = activeYear;
    }

    public String getElectionLevel() {
        return electionLevel;
    }

    public void setElectionLevel(String electionLevel) {
        this.electionLevel = electionLevel;
    }
}
