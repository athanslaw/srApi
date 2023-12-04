package com.edunge.srtool.model;

import javax.persistence.*;

@Entity
@Table(name="result_real_time", indexes = {
        @Index(name = "idx_electionId", columnList = "election_id"),
        @Index(name = "idx_electionType", columnList = "election_type"),
        @Index(name = "idx_electionIdStateId", columnList = "election_id, state_id"),
        @Index(name = "idx_stateId", columnList = "state_id"),
        @Index(name = "idx_District", columnList = "geo_political_zone_id"),
        @Index(name = "idx_lgaId", columnList = "lga_id"),
        @Index(name = "idx_electionIdElectionTypeStateId", columnList = "election_id, election_type, state_id"),
        @Index(name = "idx_electionIdZoneId", columnList = "election_id, geo_political_zone_id"),
        @Index(name = "idx_electionIdDistrictId", columnList = "election_id, senatorial_district_id"),
        @Index(name = "idx_electionIdElectionType", columnList = "election_id, election_type"),
        @Index(name = "idx_electionIdAndLgaAndVotingLevelAndElectionType", columnList = "election_id, lga_id, voting_level_id, election_type"),
        @Index(name = "idx_electionIdLgaId", columnList = "election_id, lga_id")
})
public class ResultRealTime extends BaseModel {

    private Integer accreditedVotersCount;
    private Integer registeredVotersCount;
    private Integer party_1;
    private Integer party_2;
    private Integer party_3;
    private Integer party_4;
    private Integer party_5;
    private Integer party_6;
    private Integer voteCount;
    private Long result;
    private Integer pollingUnitCount;
    private Integer voidVotes;

    @ManyToOne
    @JoinColumn(name = "election_id",nullable = false)
    private Election election;

    @ManyToOne
    @JoinColumn(name = "party_agent_id",nullable = false)
    private PartyAgent partyAgent;

    @Column(name="geo_political_zone_id")
    private Long geoPoliticalZoneId;

    @ManyToOne
    @JoinColumn(name = "senatorial_district_id",nullable = false)
    private SenatorialDistrict senatorialDistrict;

    @ManyToOne
    @JoinColumn(name = "lga_id")
    private Lga lga;

    @ManyToOne
    @JoinColumn(name = "ward_id")
    private Ward ward;

    @ManyToOne
    @JoinColumn(name = "polling_unit_id")
    private PollingUnit pollingUnit;

    @ManyToOne
    @JoinColumn(name = "voting_level_id",nullable = false)
    private VotingLevel votingLevel;

    @Column(name="state_id")
    private Long stateId;
    @Column(name="election_type")
    private Long electionType;

    public Election getElection() {
        return election;
    }

    public void setElection(Election election) {
        this.election = election;
    }

    public PartyAgent getPartyAgent() {
        return partyAgent;
    }

    public void setPartyAgent(PartyAgent partyAgent) {
        this.partyAgent = partyAgent;
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

    public PollingUnit getPollingUnit() {
        return pollingUnit;
    }

    public void setPollingUnit(PollingUnit pollingUnit) {
        this.pollingUnit = pollingUnit;
    }

    public VotingLevel getVotingLevel() {
        return votingLevel;
    }

    public void setVotingLevel(VotingLevel votingLevel) {
        this.votingLevel = votingLevel;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getElectionType() {
        return electionType;
    }

    public void setElectionType(Long electionType) {
        this.electionType = electionType;
    }

    public Long getGeoPoliticalZoneId() {
        return geoPoliticalZoneId;
    }

    public void setGeoPoliticalZoneId(Long geoPoliticalZoneId) {
        this.geoPoliticalZoneId = geoPoliticalZoneId;
    }

    public Integer getAccreditedVotersCount() {
        return accreditedVotersCount;
    }

    public void setAccreditedVotersCount(Integer accreditedVotersCount) {
        this.accreditedVotersCount = accreditedVotersCount;
    }

    public Integer getRegisteredVotersCount() {
        return registeredVotersCount;
    }

    public void setRegisteredVotersCount(Integer registeredVotersCount) {
        this.registeredVotersCount = registeredVotersCount;
    }

    public Integer getPollingUnitCount() {
        return pollingUnitCount;
    }

    public void setPollingUnitCount(Integer pollingUnitCount) {
        this.pollingUnitCount = pollingUnitCount;
    }

    public Integer getParty_1() {
        return party_1;
    }

    public void setParty_1(Integer party_1) {
        this.party_1 = party_1;
    }

    public Integer getParty_2() {
        return party_2;
    }

    public void setParty_2(Integer party_2) {
        this.party_2 = party_2;
    }

    public Integer getParty_3() {
        return party_3;
    }

    public void setParty_3(Integer party_3) {
        this.party_3 = party_3;
    }

    public Integer getParty_4() {
        return party_4;
    }

    public void setParty_4(Integer party_4) {
        this.party_4 = party_4;
    }

    public Integer getParty_5() {
        return party_5;
    }

    public void setParty_5(Integer party_5) {
        this.party_5 = party_5;
    }

    public Integer getParty_6() {
        return party_6;
    }

    public void setParty_6(Integer party_6) {
        this.party_6 = party_6;
    }

    public Long getResult() {
        return result;
    }

    public void setResult(Long result) {
        this.result = result;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Integer getVoidVotes() {
        return voidVotes;
    }

    public void setVoidVotes(Integer voidVotes) {
        this.voidVotes = voidVotes;
    }

    @Override
    public String toString() {
        return "ResultRealTime{" +
                "partyAgent=" + partyAgent +
                ", accreditedVotersCount=" + accreditedVotersCount +
                ", registeredVotersCount=" + registeredVotersCount +
                ", party_1=" + party_1 +
                ", party_2=" + party_2 +
                ", party_3=" + party_3 +
                ", party_4=" + party_4 +
                ", party_5=" + party_5 +
                ", party_6=" + party_6 +
                ", voteCount=" + voteCount +
                ", result=" + result +
                ", pollingUnitCount=" + pollingUnitCount +
                '}';
    }
}
