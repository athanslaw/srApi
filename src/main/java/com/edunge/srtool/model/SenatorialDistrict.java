package com.edunge.srtool.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
public class SenatorialDistrict extends AbstractBaseModel {
    @ManyToOne
    @JoinColumn(name = "state_id")
    @JsonIgnore
    private State state;

    @OneToMany(mappedBy="senatorialDistrict")
    Set<Lga> lgaSet;

    @OneToMany(mappedBy="senatorialDistrict")
    Set<Ward> wards;

    @OneToMany(mappedBy="senatorialDistrict")
    Set<PollingUnit> pollingUnits;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
