package com.edunge.srtool.model;

import javax.persistence.*;
import java.util.Set;

@Entity
//@Table(name = "senatorial_district")
public class SenatorialDistrict extends AbstractBaseModel {
    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;

    @OneToMany(mappedBy="senatorial_district")
    Set<Lga> lgaSet;
}
