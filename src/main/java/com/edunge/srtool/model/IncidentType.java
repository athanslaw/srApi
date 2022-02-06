package com.edunge.srtool.model;

import javax.persistence.Entity;

@Entity
public class IncidentType extends AbstractBaseModel{
    private int weight;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
