package com.edunge.srtool.dto;

public class PoliticalPartyCandidateDto {
    private Long electionID;
    private Long politicalPartyId;

    private String firstName;
    private String lastName;
    private String imageUrl;

    public Long getElectionID() {
        return electionID;
    }

    public void setElectionID(Long electionID) {
        this.electionID = electionID;
    }

    public Long getPoliticalPartyId() {
        return politicalPartyId;
    }

    public void setPoliticalPartyId(Long politicalPartyId) {
        this.politicalPartyId = politicalPartyId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
