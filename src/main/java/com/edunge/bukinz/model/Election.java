package com.edunge.bukinz.model;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class Election extends AbstractBaseModel{
    private String description;
    private Integer year;
    private Date dateOfElection;
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

    public Date getDateOfElection() {
        return dateOfElection;
    }

    public void setDateOfElection(Date dateOfElection) {
        this.dateOfElection = dateOfElection;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Election{" +
                "description='" + description + '\'' +
                ", year=" + year +
                ", dateOfElection=" + dateOfElection +
                '}';
    }
}
