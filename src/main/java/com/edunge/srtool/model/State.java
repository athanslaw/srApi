package com.edunge.srtool.model;

import javax.persistence.*;

@Entity
@Table(name="state", indexes = {
        @Index(name = "idx_code", columnList = "code")
})
public class State extends AbstractBaseModel {

    @ManyToOne
    @JoinColumn(name = "geo_political_zone_id")
    private GeoPoliticalZone geoPoliticalZone;
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
    public GeoPoliticalZone getGeoPoliticalZone() {
        return geoPoliticalZone;
    }

    public void setGeoPoliticalZone(GeoPoliticalZone geoPoliticalZone) {
        this.geoPoliticalZone = geoPoliticalZone;
    }
}
