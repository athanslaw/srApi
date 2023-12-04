package com.edunge.srtool.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class PollingUnit extends AbstractBaseModel{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id")
    private State state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senatorial_district_id")
    private SenatorialDistrict senatorialDistrict;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lga_id")
    private Lga lga;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_id")
    private Ward ward;

    @OneToMany(mappedBy = "pollingUnit", fetch = FetchType.LAZY)
    private Set<PartyAgent> partyAgents;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public SenatorialDistrict getSenatorialDistrict() {
        return senatorialDistrict;
    }

    public void setSenatorialDistrict(SenatorialDistrict senatorialDistrict) {
        this.senatorialDistrict = senatorialDistrict;
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
}
