package com.edunge.srtool.dto;

import com.edunge.srtool.model.BaseModel;

public class PartyAgentDto extends BaseModel {
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private String address;
    private String lgaId;
    private String wardId;
    private String pollingUnitId;
    private Long politicalPartyId;

    private String lgaName;
    private String wardName;
    private String pollingUnitName;
    private Long stateId;
    private String pwd;
    private String role;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLgaId() {
        return lgaId;
    }

    public void setLgaId(String lgaId) {
        this.lgaId = lgaId;
    }

    public String getWardId() {
        return wardId;
    }

    public void setWardId(String wardId) {
        this.wardId = wardId;
    }

    public String getPollingUnitId() {
        return pollingUnitId;
    }

    public void setPollingUnitId(String pollingUnitId) {
        this.pollingUnitId = pollingUnitId;
    }

    public Long getPoliticalPartyId() {
        return politicalPartyId;
    }

    public void setPoliticalPartyId(Long politicalPartyId) {
        this.politicalPartyId = politicalPartyId;
    }

    public String getLgaName() {
        return lgaName;
    }

    public void setLgaName(String lgaName) {
        this.lgaName = lgaName;
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

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
