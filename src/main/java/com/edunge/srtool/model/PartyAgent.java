package com.edunge.srtool.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class PartyAgent extends BaseModel{
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private String address;

    @ManyToOne
    @JoinColumn(name = "lga_id", nullable = false)
    private Lga lga;

    @ManyToOne
    @JoinColumn(name = "ward_id", nullable = false)
    private Ward ward;

    @ManyToOne
    @JoinColumn(name = "political_party_id", nullable = false)
    private PoliticalParty politicalParty;

    @ManyToOne
    @JoinColumn(name = "polling_unit_id", nullable = false)
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
}
