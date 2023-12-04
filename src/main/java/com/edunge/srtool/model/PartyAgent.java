package com.edunge.srtool.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class PartyAgent extends BaseModel{
    private String firstname;
    private String lastname;
    private String phone;
    @Column(nullable = true)
    private String email;
    private String address;
    private String role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lga_id", nullable = false)
    @JsonIgnore
    private Lga lga;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_id", nullable = false)
    @JsonIgnore
    private Ward ward;

    @ManyToOne
    @JoinColumn(name = "political_party_id", nullable = true)
    @JsonIgnore
    private PoliticalParty politicalParty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "polling_unit_id", nullable = false)
    @JsonIgnore
    private PollingUnit pollingUnit;

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

    public PoliticalParty getPoliticalParty() {
        return politicalParty;
    }

    public void setPoliticalParty(PoliticalParty politicalParty) {
        this.politicalParty = politicalParty;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "PartyAgent{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", lga=" + lga +
                ", ward=" + ward +
                ", politicalParty=" + politicalParty +
                ", pollingUnit=" + pollingUnit +
                '}';
    }
}
