package com.edunge.srtool.model;

import javax.persistence.Entity;

@Entity
public class Incident extends AbstractElectionDetails {
    private String description;
    private String reportedLocation;
    private String phoneNumberToContact;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReportedLocation() {
        return reportedLocation;
    }

    public void setReportedLocation(String reportedLocation) {
        this.reportedLocation = reportedLocation;
    }

    public String getPhoneNumberToContact() {
        return phoneNumberToContact;
    }

    public void setPhoneNumberToContact(String phoneNumberToContact) {
        this.phoneNumberToContact = phoneNumberToContact;
    }
}
