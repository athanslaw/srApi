package com.edunge.srtool.model;

import javax.persistence.*;

@Entity
public class Config extends AbstractBaseModel {
    private String activeState;
    private String activeYear;

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
}
