package com.edunge.bukinz.dto;

import com.edunge.bukinz.model.AbstractBaseModel;

public class IncidentGroupDto extends AbstractBaseModel {
    private String code;
    private String description;
    private Integer year;

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

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }
}
