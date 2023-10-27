package com.edunge.bukinz.model;

import javax.persistence.*;

@Entity
public class Config extends AbstractBaseModel {
    private String activeState;
    private String activeYear;
    private String electionLevel; // state, senatorial, lga, polling unit

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
