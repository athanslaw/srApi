package com.edunge.srtool.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class Lga extends AbstractBaseModel {
    @ManyToOne
    @JoinColumn(name="state_id", nullable=false)
    private State state;

    @ManyToOne
    @JoinColumn(name = "senatorial_district_id", nullable = false)
    private SenatorialDistrict senatorialDistrict;

    @OneToMany(mappedBy = "lga")
    private Set<PartyAgent> partyAgent;

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
}
