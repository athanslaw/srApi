package com.edunge.srtool.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class Ward extends AbstractBaseModel {

    @ManyToOne
    @JoinColumn(name = "state_id")
    @JsonIgnore
    private State state;

    @ManyToOne
    @JoinColumn(name = "senatorial_district_id")
    @JsonIgnore
    private SenatorialDistrict senatorialDistrict;

    @ManyToOne
    @JoinColumn(name = "lga_id")
    @JsonIgnore
    private Lga lga;

    @OneToMany(mappedBy = "ward")
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
}
