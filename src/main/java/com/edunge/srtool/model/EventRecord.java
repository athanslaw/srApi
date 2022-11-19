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
    private String lgaName;
    private Long geoPoliticalZoneId;
    private String geoPoliticalZoneName;
    private Long senatorialDistrictId;
    private String senatorialDistrictName;
    private Long eventId;
    private Long stateId;
    private String stateName;

    private Long ward;
    private String wardName;

    private Long pollingUnit;
    private String pollingUnitName;

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

    public String getLgaName() {
        return lgaName;
    }

    public void setLgaName(String lgaName) {
        this.lgaName = lgaName;
    }

    public String getGeoPoliticalZoneName() {
        return geoPoliticalZoneName;
    }

    public void setGeoPoliticalZoneName(String geoPoliticalZoneName) {
        this.geoPoliticalZoneName = geoPoliticalZoneName;
    }

    public Long getSenatorialDistrictId() {
        return senatorialDistrictId;
    }

    public void setSenatorialDistrictId(Long senatorialDistrictId) {
        this.senatorialDistrictId = senatorialDistrictId;
    }

    public String getSenatorialDistrictName() {
        return senatorialDistrictName;
    }

    public void setSenatorialDistrictName(String senatorialDistrictName) {
        this.senatorialDistrictName = senatorialDistrictName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getPollingUnitName() {
        return pollingUnitName;
    }

    public void setPollingUnitName(String pollingUnitName) {
        this.pollingUnitName = pollingUnitName;
    }
}
