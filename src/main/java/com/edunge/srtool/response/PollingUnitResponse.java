package com.edunge.srtool.response;

import com.edunge.srtool.model.PollingUnit;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PollingUnitResponse extends BaseResponse{
    PollingUnit pollingUnit;
    List<PollingUnit> pollingUnits;
    Integer count;

    public Integer getCount() {
        return count;
    }
    public PollingUnitResponse(String code, String message, PollingUnit pollingUnit) {
        super(code, message);
        this.pollingUnit = pollingUnit;
    }

    public PollingUnitResponse(PollingUnit pollingUnit) {
        this.pollingUnit = pollingUnit;
    }

    public PollingUnitResponse(String code, String message) {
        super(code, message);
    }

    public PollingUnitResponse(String code, String message, List<PollingUnit> pollingUnits) {
        super(code, message);
        this.pollingUnits = pollingUnits;
        this.count = pollingUnits.size();
    }

    public PollingUnit getPollingUnit() {
        return pollingUnit;
    }

    public void setPollingUnit(PollingUnit pollingUnit) {
        this.pollingUnit = pollingUnit;
    }

    public List<PollingUnit> getPollingUnits() {
        return pollingUnits;
    }

    public void setPollingUnits(List<PollingUnit> pollingUnits) {
        this.pollingUnits = pollingUnits;
    }
}
