package com.edunge.srtool.dto;

import com.edunge.srtool.model.AbstractBaseModel;

public class LgaDto extends AbstractBaseModel {
    private Long stateId;
    private Long senatorialDistrictId;

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getSenatorialDistrictId() {
        return senatorialDistrictId;
    }

    public void setSenatorialDistrictId(Long senatorialDistrictId) {
        this.senatorialDistrictId = senatorialDistrictId;
    }
}
