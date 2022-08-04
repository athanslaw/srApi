package com.edunge.srtool.model;

import javax.persistence.Entity;

@Entity
public class PoliticalParty extends AbstractBaseModel {
    private String colorCode;
    private Long stateId;

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }
}
