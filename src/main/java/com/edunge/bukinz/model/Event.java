package com.edunge.bukinz.model;

import javax.persistence.Entity;

@Entity
public class Event extends AbstractBaseModel{
    private String description;
    private Boolean status;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
