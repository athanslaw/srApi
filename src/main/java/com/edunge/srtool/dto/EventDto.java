package com.edunge.srtool.dto;

import com.edunge.srtool.model.AbstractBaseModel;

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
