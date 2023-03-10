package com.edunge.srtool.model;

import javax.persistence.Entity;

@Entity
public class IncidentGroup extends AbstractBaseModel{
    private String description;
    private Integer year;
    private Boolean status;

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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
