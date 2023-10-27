package com.edunge.bukinz.dto;

import com.edunge.bukinz.model.AbstractBaseModel;

public class SenatorialDistrictDto extends AbstractBaseModel {
    private Long stateId;

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }
}
