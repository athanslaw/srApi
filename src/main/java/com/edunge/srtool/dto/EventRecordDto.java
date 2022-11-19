package com.edunge.srtool.dto;

import com.edunge.srtool.model.AbstractBaseModel;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class EventRecordDto extends AbstractBaseModel {
    private Boolean eventStatus;
    private Long  lga;
    @NotNull
    private Long  eventId;
    @NotNull
    private Long pollingUnit;
    @NotNull
    private String description;
    @NotNull
    private Long agentId;
    private String combinedKeys;
    private LocalDateTime timeStamp;
    private long stateId;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public long getStateId() {
        return stateId;
    }

    public void setStateId(long stateId) {
        this.stateId = stateId;
    }

    public Boolean getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(Boolean eventStatus) {
        this.eventStatus = eventStatus;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Long getLga() {
        return lga;
    }

    public void setLga(Long lga) {
        this.lga = lga;
    }

    public Long getPollingUnit() {
        return pollingUnit;
    }

    public void setPollingUnit(Long pollingUnit) {
        this.pollingUnit = pollingUnit;
    }
}
