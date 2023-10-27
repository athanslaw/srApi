package com.edunge.bukinz.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import java.time.LocalDateTime;

@Entity
//@NamedQuery(name = "Incident.findByWardAndPollingUnit",
//        query = "SELECT * FROM incident WHERE ward_id=?1 AND polling_unit_id=?2 ORDER BY timeStamp DESC LIMIT 1")
public class Incident extends BaseModel{
    @ManyToOne
    @JoinColumn(name = "lga_id",nullable = false)
    private Lga lga;


    private Long geoPoliticalZoneId;

    private String combinedKeys;
    private LocalDateTime timeStamp;
    private int weight;
    private Long stateId;
    private Long incidentGroupId;

    @ManyToOne
    @JoinColumn(name = "ward_id",nullable = false)
    private Ward ward;

    @ManyToOne
    @JoinColumn(name = "polling_unit_id",nullable = false)
    private PollingUnit pollingUnit;

    @ManyToOne
    @JoinColumn(name = "incident_level_id",nullable = false)
    private IncidentLevel incidentLevel;

    @ManyToOne
    @JoinColumn(name = "incident_status_id",nullable = false)
    private IncidentStatus incidentStatus;

    @ManyToOne
    @JoinColumn(name = "incident_type_id",nullable = false)
    private IncidentType incidentType;

    private String description;
    private String reportedLocation;
    private String phoneNumberToContact;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReportedLocation() {
        return reportedLocation;
    }

    public void setReportedLocation(String reportedLocation) {
        this.reportedLocation = reportedLocation;
    }

    public String getPhoneNumberToContact() {
        return phoneNumberToContact;
    }

    public void setPhoneNumberToContact(String phoneNumberToContact) {
        this.phoneNumberToContact = phoneNumberToContact;
    }

    public Lga getLga() {
        return lga;
    }

    public void setLga(Lga lga) {
        this.lga = lga;
    }

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    public PollingUnit getPollingUnit() {
        return pollingUnit;
    }

    public void setPollingUnit(PollingUnit pollingUnit) {
        this.pollingUnit = pollingUnit;
    }

    public IncidentLevel getIncidentLevel() {
        return incidentLevel;
    }

    public void setIncidentLevel(IncidentLevel incidentLevel) {
        this.incidentLevel = incidentLevel;
    }

    public IncidentStatus getIncidentStatus() {
        return incidentStatus;
    }

    public void setIncidentStatus(IncidentStatus incidentStatus) {
        this.incidentStatus = incidentStatus;
    }

    public IncidentType getIncidentType() {
        return incidentType;
    }

    public void setIncidentType(IncidentType incidentType) {
        this.incidentType = incidentType;
    }

    public String getCombinedKeys() {
        return combinedKeys;
    }

    public void setCombinedKeys(String combinedKeys) {
        this.combinedKeys = combinedKeys;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getGeoPoliticalZoneId() {
        return geoPoliticalZoneId;
    }

    public void setGeoPoliticalZoneId(Long geoPoliticalZoneId) {
        this.geoPoliticalZoneId = geoPoliticalZoneId;
    }

    public Long getIncidentGroupId() {
        return incidentGroupId;
    }

    public void setIncidentGroupId(Long incidentGroupId) {
        this.incidentGroupId = incidentGroupId;
    }
}
