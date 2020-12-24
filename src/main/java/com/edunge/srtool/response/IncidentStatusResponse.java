package com.edunge.srtool.response;

import com.edunge.srtool.model.IncidentStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class IncidentStatusResponse extends BaseResponse {
    IncidentStatus incidentStatus;
    List<IncidentStatus> incidentStatuses;

    public IncidentStatusResponse(String code, String message, IncidentStatus politicalParty) {
        super(code, message);
        this.incidentStatus = politicalParty;
    }

    public IncidentStatusResponse(IncidentStatus politicalParty) {
        this.incidentStatus = politicalParty;
    }

    public IncidentStatusResponse(String code, String message) {
        super(code, message);
    }

    public IncidentStatusResponse(String code, String message, List<IncidentStatus> objects) {
        super(code, message);
        this.incidentStatuses = objects;
    }

    public IncidentStatus getIncidentStatus() {
        return incidentStatus;
    }

    public void setIncidentStatus(IncidentStatus incidentStatus) {
        this.incidentStatus = incidentStatus;
    }

    public List<IncidentStatus> getIncidentStatuses() {
        return incidentStatuses;
    }

    public void setIncidentStatuses(List<IncidentStatus> incidentStatuses) {
        this.incidentStatuses = incidentStatuses;
    }
}
