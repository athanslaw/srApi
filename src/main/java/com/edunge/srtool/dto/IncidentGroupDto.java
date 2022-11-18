package com.edunge.srtool.dto;

import com.edunge.srtool.model.AbstractBaseModel;

public class IncidentGroupDto extends AbstractBaseModel {
    private String description;
    private Integer year;

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

}
