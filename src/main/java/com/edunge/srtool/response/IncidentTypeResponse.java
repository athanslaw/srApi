package com.edunge.srtool.response;

import com.edunge.srtool.model.IncidentType;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class IncidentTypeResponse extends BaseResponse {
    IncidentType incidentType;
    List<IncidentType> incidentTypes;
    Integer count;

    public Integer getCount() {
        return count;
    }
    public IncidentTypeResponse(String code, String message, IncidentType politicalParty) {
        super(code, message);
        this.incidentType = politicalParty;
    }

    public IncidentTypeResponse(IncidentType politicalParty) {
        this.incidentType = politicalParty;
    }

    public IncidentTypeResponse(String code, String message) {
        super(code, message);
    }

    public IncidentTypeResponse(String code, String message, List<IncidentType> objects) {
        super(code, message);
        this.incidentTypes = objects;
        this.count = objects.size();
    }

    public IncidentType getIncidentType() {
        return incidentType;
    }

    public void setIncidentType(IncidentType incidentType) {
        this.incidentType = incidentType;
    }

    public List<IncidentType> getIncidentTypes() {
        return incidentTypes;
    }

    public void setIncidentTypes(List<IncidentType> incidentTypes) {
        this.incidentTypes = incidentTypes;
    }
}
