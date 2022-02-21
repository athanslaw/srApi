package com.edunge.srtool.model;

import javax.persistence.Entity;

@Entity
public class PoliticalParty extends AbstractBaseModel {
    private String colorCode;

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
}
