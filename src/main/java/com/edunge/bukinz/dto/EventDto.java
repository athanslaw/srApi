package com.edunge.bukinz.dto;

import com.edunge.bukinz.model.AbstractBaseModel;

public class EventDto extends AbstractBaseModel {
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
