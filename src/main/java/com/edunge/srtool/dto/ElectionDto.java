package com.edunge.srtool.dto;

import com.edunge.srtool.model.AbstractBaseModel;

public class ElectionDto extends AbstractBaseModel {
    private String description;
    private Integer year;
    private String dateOfElection;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getDateOfElection() {
        return dateOfElection;
    }

    public void setDateOfElection(String dateOfElection) {
        this.dateOfElection = dateOfElection;
    }
}
