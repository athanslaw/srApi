package com.edunge.srtool.response;

import com.edunge.srtool.model.Incident;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class IncidentResponse extends BaseResponse{
    Incident incident;
    List<Incident> incidents;
    Integer count;

    public Integer getCount() {
        return count;
    }
    public IncidentResponse(String code, String message, Incident incident) {
        super(code, message);
        this.incident = incident;
    }

    public IncidentResponse(Incident incident) {
        this.incident = incident;
    }

    public IncidentResponse(String code, String message) {
        super(code, message);
    }

    public IncidentResponse(String code, String message, List<Incident> incidents) {
        super(code, message);
        this.incidents = incidents;
        this.count = incidents.size();
    }

    public Incident getIncident() {
        return incident;
    }

    public void setIncident(Incident incident) {
        this.incident = incident;
    }

    public List<Incident> getIncidents() {
        return incidents;
    }

    public void setIncidents(List<Incident> incidents) {
        this.incidents = incidents;
    }
}
