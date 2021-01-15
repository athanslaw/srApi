package com.edunge.srtool.response;

import com.edunge.srtool.model.Lga;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class IncidentReport {
    private String incidentType;
    private Integer count;
    private Double percent;
    private Lga lga;
    private Integer totalCount;
    public IncidentReport(String type, Integer count, Double percent) {
        this.incidentType = type;
        this.count = count;
        this.percent = percent;
    }

    public IncidentReport(Lga lga, String type, Integer count, Double percent, Integer totalCount) {
        this.incidentType = type;
        this.count = count;
        this.percent = percent;
        this.lga = lga;
        this.totalCount = totalCount;
    }

    public IncidentReport(Lga lga, String type, Integer count, Double percent) {
        this.incidentType = type;
        this.count = count;
        this.percent = percent;
        this.lga = lga;
    }

    public String getIncidentType() {
        return incidentType;
    }

    public void setIncidentType(String incidentType) {
        this.incidentType = incidentType;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public Lga getLga() {
        return lga;
    }

    public void setLga(Lga lga) {
        this.lga = lga;
    }
}
