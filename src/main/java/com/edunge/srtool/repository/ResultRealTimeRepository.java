package com.edunge.srtool.repository;

import com.edunge.srtool.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRealTimeRepository extends JpaRepository<ResultRealTime, Long> {
    void deleteByWard(Ward ward);
    void deleteByResult(Long result);
    List<ResultRealTime> findByElectionAndLgaAndVotingLevelAndElectionType(Election election, Lga lga, VotingLevel votingLevelId, Long electionType);
    List<ResultRealTime> findByElectionAndWardAndVotingLevelLessThanAndElectionType(Election election, Ward ward, VotingLevel votingLevelId, Long electionType);
    //                    @Param("wardId")Long wardId,@Param("pollingUnitId")Long pollingUnitId);
    List<ResultRealTime> findByElectionAndLgaAndElectionType(Election election, Lga lga, Long electionType);
    List<ResultRealTime> findByLgaAndElectionTypeAndElection(Lga lga, Long electionType, Election election);
    List<ResultRealTime> findByElection(Election election);
    List<ResultRealTime> findByElectionAndElectionType(Election election, Long electionType);
    List<ResultRealTime> findByElectionAndElectionTypeAndGeoPoliticalZoneId(Election election, Long electionType, Long zone);
    List<ResultRealTime> findByElectionAndElectionTypeAndStateId(Election election, Long electionType, Long stateId);
    List<ResultRealTime> findBySenatorialDistrictAndElectionTypeAndElection(SenatorialDistrict senatorialDistrict, Long electionType, Election election);
    List<ResultRealTime> findByStateIdAndElectionTypeAndElection(long stateId, Long electionType, Election election);
    void deleteByLga(Lga lga);
    @Query(nativeQuery = true, value="SELECT SUM(registered_voters_count) FROM result_real_time WHERE election_id=?1 AND election_type=?2")
    Long findSumRegisteredVotes(Election election, Long electionType);
    @Query(nativeQuery = true, value="SELECT SUM(registered_voters_count) FROM resultRealTime WHERE election_id=?1 AND geo_political_zone=?2 AND election_type=?3")
    Long findSumRegisteredVotersZone(Election election, GeoPoliticalZone zone, Long electionType);
    @Query(nativeQuery = true, value="SELECT SUM(accredited_voters_count) FROM result_real_time WHERE election_id=?1 AND election_type=?2")
    Long findSumAccreditedVoters(Election election, Long electionType);
    @Query(nativeQuery = true, value="SELECT SUM(accredited_voters_count) FROM result_real_time WHERE election_id=?1 AND geo_political_zone=?2 AND election_type=?3")
    Long findSumAccreditedVotersZone(Election election, GeoPoliticalZone zone, Long electionType);
    @Query(nativeQuery = true, value="SELECT SUM(vote_count) FROM result_real_time WHERE election_id=?1")
    Long findSumVoteCount(Election election, Long electionType);
    @Query(nativeQuery = true, value="SELECT SUM(vote_count) FROM result_real_time WHERE election_id=?1 AND geo_political_zone=?2 AND election_type=?3")
    Long findSumVoteCountZone(Election election, GeoPoliticalZone zone, Long electionType);
    
}
