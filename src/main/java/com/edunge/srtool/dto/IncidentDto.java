package com.edunge.srtool.dto;

import com.edunge.srtool.model.AbstractBaseModel;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class IncidentDto extends AbstractBaseModel {
    @NotNull
    private Long incidentStatusId;
    @NotNull
    private Long incidentLevelId;
    @NotNull
    private Long incidentTypeId;
    @NotNull
    private Long  lgaId;
    @NotNull
    private Long  wardId;
    @NotNull
    private Long pollingUnitId;
    @NotNull
    private String description;
    @NotNull
    private String reportedLocation;
    @NotNull
    private String phoneNumberToContact;
    private String combinedKeys;
    private int severity;
    private LocalDateTime timeStamp;
    private long weight;
    private long stateId;
    private long incidentGroupId;

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

    public Long getIncidentStatusId() {
        return incidentStatusId;
    }

    public void setIncidentStatusId(Long incidentStatusId) {
        this.incidentStatusId = incidentStatusId;
    }

    public Long getIncidentLevelId() {
        return incidentLevelId;
    }

    public void setIncidentLevelId(Long incidentLevelId) {
        this.incidentLevelId = incidentLevelId;
    }

    public Long getIncidentTypeId() {
        return incidentTypeId;
    }

    public void setIncidentTypeId(Long incidentTypeId) {
        this.incidentTypeId = incidentTypeId;
    }


    public Long getLgaId() {
        return lgaId;
    }

    public void setLgaId(Long lgaId) {
        this.lgaId = lgaId;
    }

    public Long getWardId() {
        return wardId;
    }

    public void setWardId(Long wardId) {
        this.wardId = wardId;
    }

    public Long getPollingUnitId() {
        return pollingUnitId;
    }

    public void setPollingUnitId(Long pollingUnitId) {
        this.pollingUnitId = pollingUnitId;
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

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }

    public long getStateId() {
        return stateId;
    }

    public void setStateId(long stateId) {
        this.stateId = stateId;
    }

    public long getIncidentGroupId() {
        return incidentGroupId;
    }

    public void setIncidentGroupId(long incidentGroupId) {
        this.incidentGroupId = incidentGroupId;
    }
}
