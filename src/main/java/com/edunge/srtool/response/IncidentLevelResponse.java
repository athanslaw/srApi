package com.edunge.srtool.response;

import com.edunge.srtool.model.IncidentLevel;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class IncidentLevelResponse extends BaseResponse {
    IncidentLevel incidentLevel;
    List<IncidentLevel> incidentLevels;

    public IncidentLevelResponse(String code, String message, IncidentLevel politicalParty) {
        super(code, message);
        this.incidentLevel = politicalParty;
    }

    public IncidentLevelResponse(IncidentLevel politicalParty) {
        this.incidentLevel = politicalParty;
    }

    public IncidentLevelResponse(String code, String message) {
        super(code, message);
    }

    public IncidentLevelResponse(String code, String message, List<IncidentLevel> objects) {
        super(code, message);
        this.incidentLevels = objects;
    }

    public IncidentLevel getIncidentLevel() {
        return incidentLevel;
    }

    public void setIncidentLevel(IncidentLevel incidentLevel) {
        this.incidentLevel = incidentLevel;
    }

    public List<IncidentLevel> getIncidentLevels() {
        return incidentLevels;
    }

    public void setIncidentLevels(List<IncidentLevel> incidentLevels) {
        this.incidentLevels = incidentLevels;
    }
}
