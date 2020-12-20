package com.edunge.srtool.model;

import javax.persistence.Entity;

@Entity
public class Election extends AbstractBaseModel{
    private String description;
    private String year;
    private String dateOfElection;
}
