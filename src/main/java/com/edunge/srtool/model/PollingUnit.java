package com.edunge.srtool.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(indexes = {
        @Index(name = "idx_pu_state", columnList = "state_id"),
        @Index(name = "idx_pu_senatorial_district", columnList = "senatorial_district_id"),
        @Index(name = "idx_pu_lga", columnList = "lga_id"),
        @Index(name = "idx_pu_ward", columnList = "ward_id")
})
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
