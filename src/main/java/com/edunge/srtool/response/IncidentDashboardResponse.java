package com.edunge.srtool.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IncidentDashboardResponse extends BaseResponse{
    private Integer incidentCount;
    private List<IncidentReport> incidentReports;
    private List<IncidentReport> lgaIncidentReports;

    public IncidentDashboardResponse(String code, String name, List<IncidentReport> incidentReports) {
        super(code, name);
        this.incidentReports = incidentReports;
    }

    public IncidentDashboardResponse(String code, String message, List<IncidentReport> incidentReports, List<IncidentReport> lgaIncidentReports) {
        super(code, message);
        this.incidentReports = incidentReports;
        this.lgaIncidentReports = lgaIncidentReports;
    }

    public Integer getIncidentCount() {
        return incidentCount;
    }

    public IncidentDashboardResponse(String code, String message) {
        super(code, message);
    }

    public IncidentDashboardResponse() {
    }

    public void setIncidentCount(Integer incidentCount) {
        this.incidentCount = incidentCount;
    }

    public List<IncidentReport> getIncidentReports() {
        return incidentReports;
    }

    public void setIncidentReports(List<IncidentReport> incidentReports) {
        this.incidentReports = incidentReports;
    }

    public List<IncidentReport> getLgaIncidentReports() {
        return lgaIncidentReports;
    }

    public void setLgaIncidentReports(List<IncidentReport> lgaIncidentReports) {
        this.lgaIncidentReports = lgaIncidentReports;
    }
}
