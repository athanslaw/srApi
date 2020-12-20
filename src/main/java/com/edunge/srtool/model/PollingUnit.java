package com.edunge.srtool.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class PollingUnit extends AbstractBaseModel{
    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;

    @ManyToOne
    @JoinColumn(name = "senatorial_district_id")
    private SenatorialDistrict senatorialDistrict;

    @ManyToOne
    @JoinColumn(name = "lga_id")
    private Lga lga;

    @ManyToOne
    @JoinColumn(name = "ward_id")
    private Ward ward;

    @OneToMany(mappedBy = "pollingUnit")
    private Set<PartyAgent> partyAgents;

}
