package com.edunge.srtool.dto;

import javax.validation.constraints.NotNull;

public class PoliticalPartyDto {
    @NotNull
    private String code;
    @NotNull
    private String name;
    private Long stateId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public @NotNull Long getStateId() {
        return stateId;
    }

    public void setStateId(@NotNull Long stateId) {
        this.stateId = stateId;
    }
}
