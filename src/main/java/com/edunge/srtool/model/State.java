package com.edunge.srtool.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class State extends AbstractBaseModel {

    @ManyToOne
    @JoinColumn(name = "geo_political_zone_id")
    private GeoPoliticalZone geoPoliticalZone;
    @OneToMany(mappedBy = "state", fetch = FetchType.LAZY)
    Set<Lga> lgaSet;

    @OneToMany(mappedBy = "state", fetch = FetchType.LAZY)
    Set<PollingUnit> pollingUnits;

    @OneToMany(mappedBy = "state", fetch = FetchType.LAZY)
    Set<Ward> wards;

    @OneToMany(mappedBy = "state", fetch = FetchType.LAZY)
    Set<SenatorialDistrict> senatorialDistricts;
    private String svgUrl;
    private boolean defaultState;

    public String getSvgUrl() {
        return svgUrl;
    }

    public void setSvgUrl(String svgUrl) {
        this.svgUrl = svgUrl;
    }

    public boolean isDefaultState() {
        return defaultState;
    }

    public void setDefaultState(boolean defaultState) {
        this.defaultState = defaultState;
    }

    public Set<SenatorialDistrict> getSenatorialDistricts() {
        return senatorialDistricts;
    }

    public void setSenatorialDistricts(Set<SenatorialDistrict> senatorialDistricts) {
        this.senatorialDistricts = senatorialDistricts;
    }

    public GeoPoliticalZone getGeoPoliticalZone() {
        return geoPoliticalZone;
    }

    public void setGeoPoliticalZone(GeoPoliticalZone geoPoliticalZone) {
        this.geoPoliticalZone = geoPoliticalZone;
    }
}
