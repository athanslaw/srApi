package com.edunge.bukinz.response;

import com.edunge.bukinz.model.Ward;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WardResponse extends BaseResponse{
    Ward ward;
    List<Ward> wards;
    Integer count;

    public Integer getCount() {
        return count;
    }

    public WardResponse(String code, String message, Ward ward) {
        super(code, message);
        this.ward = ward;
    }

    public WardResponse(Ward ward) {
        this.ward = ward;
    }

    public WardResponse(String code, String message) {
        super(code, message);
    }

    public WardResponse(String code, String message, List<Ward> wards) {
        super(code, message);
        this.wards = wards;
        this.count = wards.size();
    }

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    public List<Ward> getWards() {
        return wards;
    }

    public void setWards(List<Ward> wards) {
        this.wards = wards;
    }
}
