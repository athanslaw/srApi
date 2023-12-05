package com.edunge.srtool.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(indexes = {
        @Index(name = "idx_ward_state", columnList = "state_id"),
        @Index(name = "idx_ward_senatorial_district", columnList = "senatorial_district_id"),
        @Index(name = "idx_ward_lga", columnList = "lga_id")
})
public class Ward extends AbstractBaseModel {

    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;

    @ManyToOne
    @JoinColumn(name = "senatorial_district_id")
    private SenatorialDistrict senatorialDistrict;

    @ManyToOne
    @JoinColumn(name = "lga_id")
    private Lga lga;
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
