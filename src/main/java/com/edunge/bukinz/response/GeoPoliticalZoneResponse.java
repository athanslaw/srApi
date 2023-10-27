package com.edunge.bukinz.response;

import com.edunge.bukinz.model.GeoPoliticalZone;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeoPoliticalZoneResponse extends BaseResponse {
    GeoPoliticalZone geoPoliticalZone;
    List<GeoPoliticalZone> geoPoliticalZoneList;
    Integer count;

    public Integer getCount() {
        return count;
    }

    public GeoPoliticalZoneResponse(String code, String message, GeoPoliticalZone geoPoliticalZone) {
        super(code, message);
        this.geoPoliticalZone = geoPoliticalZone;
    }

    public GeoPoliticalZoneResponse(GeoPoliticalZone geoPoliticalZone) {
        this.geoPoliticalZone = geoPoliticalZone;
    }

    public GeoPoliticalZoneResponse(String code, String message) {
        super(code, message);
    }

    public GeoPoliticalZoneResponse(String code, String message, List<GeoPoliticalZone> geoPoliticalZoneList) {
        super(code, message);
        this.geoPoliticalZoneList = geoPoliticalZoneList;
        this.count = geoPoliticalZoneList.size();
    }

    public GeoPoliticalZone getGeoPoliticalZone() {
        return geoPoliticalZone;
    }

    public void setGeoPoliticalZone(GeoPoliticalZone geoPoliticalZone) {
        this.geoPoliticalZone = geoPoliticalZone;
    }

    public List<GeoPoliticalZone> getGeoPoliticalZoneList() {
        return geoPoliticalZoneList;
    }

    public void setGeoPoliticalZoneList(List<GeoPoliticalZone> geoPoliticalZoneList) {
        this.geoPoliticalZoneList = geoPoliticalZoneList;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
