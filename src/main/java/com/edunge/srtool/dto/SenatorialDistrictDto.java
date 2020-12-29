package com.edunge.srtool.dto;

import com.edunge.srtool.model.AbstractBaseModel;

public class SenatorialDistrictDto extends AbstractBaseModel {
    private Long stateId;

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }
}
