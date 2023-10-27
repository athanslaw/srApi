package com.edunge.bukinz.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class SenatorialDistrict extends AbstractBaseModel {
    @ManyToOne
    @JoinColumn(name = "state_id")
    @JsonIgnore
    private State state;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
