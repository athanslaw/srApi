package com.edunge.srtool.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class State extends AbstractBaseModel {

    @ManyToOne
    @JoinColumn(name = "geo_political_zone_id")
    private GeoPoliticalZone geoPoliticalZone;
    @OneToMany(mappedBy="state")
    Set<Lga> lgaSet;

    @OneToMany(mappedBy="state")
    Set<PollingUnit> pollingUnits;

    @OneToMany(mappedBy="state")
    Set<Ward> wards;

    @OneToMany(mappedBy="state")
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
