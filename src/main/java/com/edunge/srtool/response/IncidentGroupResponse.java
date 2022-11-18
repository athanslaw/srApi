package com.edunge.srtool.response;

import com.edunge.srtool.model.IncidentGroup;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class IncidentGroupResponse extends BaseResponse{
    IncidentGroup incidentGroup;
    List<IncidentGroup> incidentGroups;
    Integer count;

    public Integer getCount() {
        return count;
    }
    public IncidentGroupResponse(String code, String message, IncidentGroup incidentGroup) {
        super(code, message);
        this.incidentGroup = incidentGroup;
    }

    public IncidentGroupResponse(IncidentGroup incidentGroup) {
        this.incidentGroup = incidentGroup;
    }

    public IncidentGroupResponse(String code, String message) {
        super(code, message);
    }

    public IncidentGroupResponse(String code, String message, List<IncidentGroup> incidentGroups) {
        super(code, message);
        this.count  = incidentGroups.size();
        this.incidentGroups = incidentGroups;
    }

    public IncidentGroup getIncidentGroup() {
        return incidentGroup;
    }

    public void setIncidentGroup(IncidentGroup incidentGroup) {
        this.incidentGroup = incidentGroup;
    }

    public List<IncidentGroup> getIncidentGroups() {
        return incidentGroups;
    }

    public void setIncidentGroups(List<IncidentGroup> incidentGroups) {
        this.incidentGroups = incidentGroups;
    }
}
