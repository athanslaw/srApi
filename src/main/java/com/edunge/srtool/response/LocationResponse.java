package com.edunge.srtool.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocationResponse extends BaseResponse {
    private String lgaId;
    private Long senatorialDistrictId;
    String lgaName;
    String senatorialDistrictName;

    public String getLgaId() {
        return lgaId;
    }

    public void setLgaId(String lgaId) {
        this.lgaId = lgaId;
    }

    public Long getSenatorialDistrictId() {
        return senatorialDistrictId;
    }

    public void setSenatorialDistrictId(Long senatorialDistrictId) {
        this.senatorialDistrictId = senatorialDistrictId;
    }

    public LocationResponse(String code, String message) {
        super(code, message);
    }

    public LocationResponse(String code, String message, String lgaId, Long senatorialDistrictId,
                            String lgaName, String senatorialDistrictName) {
        super(code, message);
        this.senatorialDistrictId = senatorialDistrictId;
        this.lgaId = lgaId;
        this.lgaName = lgaName;
        this.senatorialDistrictName = senatorialDistrictName;
    }

    public String getLgaName() {
        return lgaName;
    }

    public String getSenatorialDistrictName() {
        return senatorialDistrictName;
    }
}
