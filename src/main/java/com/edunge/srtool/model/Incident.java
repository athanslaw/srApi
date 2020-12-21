package com.edunge.srtool.model;

import javax.persistence.Entity;

@Entity
public class Incident extends AbstractElectionDetails {
    private String description;
    private String reportedLocation;
    private String phoneNumberToContact;
}
