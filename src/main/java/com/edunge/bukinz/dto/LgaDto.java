package com.edunge.bukinz.dto;

import com.edunge.bukinz.model.AbstractBaseModel;

import javax.validation.constraints.NotNull;

public class LgaDto extends AbstractBaseModel {
    @NotNull
    private Long stateId;
    @NotNull
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
