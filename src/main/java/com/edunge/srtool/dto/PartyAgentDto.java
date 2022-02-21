package com.edunge.srtool.dto;

import com.edunge.srtool.model.BaseModel;

public class PartyAgentDto extends BaseModel {
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private String address;
    private Long lgaId;
    private Long wardId;
    private Long pollingUnitId;
    private Long politicalPartyId;

    private String lgaName;
    private String wardName;
    private String pollingUnitName;

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
}
