package com.edunge.srtool.model;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractBaseModel extends BaseModel{
    private String code;
    private String name;

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
}
