package com.edunge.srtool.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Lga extends AbstractBaseModel {
    @ManyToOne
    @JoinColumn(name="state_id", nullable=false)
    private State state;

    @ManyToOne
    @JoinColumn(name = "senatorial_district_id", nullable = false)
    private SenatorialDistrict senatorialDistrict;
}
