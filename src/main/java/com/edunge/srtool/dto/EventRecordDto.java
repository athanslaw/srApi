package com.edunge.srtool.dto;

import com.edunge.srtool.model.AbstractBaseModel;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class EventRecordDto extends AbstractBaseModel {
    private Boolean eventStatus;
    @NotNull
    private Long  lgaId;
    @NotNull
    private Long  eventId;
    @NotNull
    private Long  wardId;
    @NotNull
    private Long pollingUnitId;
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
}
