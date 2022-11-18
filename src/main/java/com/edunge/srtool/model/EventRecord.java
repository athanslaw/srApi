package com.edunge.srtool.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class EventRecord extends BaseModel{

    private String combinedKeys;
    private LocalDateTime timeStamp;
    private Long lga;
    private Long geoPoliticalZoneId;
    private Long eventId;
    private Long stateId;

    private Long ward;

    private Long pollingUnit;

    private Boolean eventStatus;
    private Long agentId;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getLga() {
        return lga;
    }

    public void setLga(Long lga) {
        this.lga = lga;
    }

    public Long getWard() {
        return ward;
    }

    public void setWard(Long ward) {
        this.ward = ward;
    }

    public Long getPollingUnit() {
        return pollingUnit;
    }

    public void setPollingUnit(Long pollingUnit) {
        this.pollingUnit = pollingUnit;
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

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Boolean getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(Boolean eventStatus) {
        this.eventStatus = eventStatus;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }
}
