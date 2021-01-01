package com.edunge.srtool.response;

import com.edunge.srtool.model.SenatorialDistrict;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SenatorialDistrictResponse extends BaseResponse {
    SenatorialDistrict senatorialDistrict;
    List<SenatorialDistrict> senatorialDistricts;
    Integer count;

    public Integer getCount() {
        return count;
    }

    public SenatorialDistrictResponse(String code, String message, SenatorialDistrict senatorialDistrict) {
        super(code, message);
        this.senatorialDistrict = senatorialDistrict;
    }

    public SenatorialDistrictResponse(SenatorialDistrict senatorialDistrict) {
        this.senatorialDistrict = senatorialDistrict;
    }

    public SenatorialDistrictResponse(String code, String message) {
        super(code, message);
    }

    public SenatorialDistrictResponse(String code, String message, List<SenatorialDistrict> senatorialDistricts) {
        super(code, message);
        this.senatorialDistricts = senatorialDistricts;
        this.count = senatorialDistricts.size();
    }

    public SenatorialDistrict getSenatorialDistrict() {
        return senatorialDistrict;
    }

    public void setSenatorialDistrict(SenatorialDistrict senatorialDistrict) {
        this.senatorialDistrict = senatorialDistrict;
    }

    public List<SenatorialDistrict> getSenatorialDistricts() {
        return senatorialDistricts;
    }

    public void setSenatorialDistricts(List<SenatorialDistrict> senatorialDistricts) {
        this.senatorialDistricts = senatorialDistricts;
    }
}
