package com.edunge.srtool.repository;

import com.edunge.srtool.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
//    @Query(value = "SELECT * from result WHERE election_id = ?1 and ward_id=?1 and polling_unit_id=?1", nativeQuery = true)
//    @Query("select r from Result r where r.election = :electionId and r.ward = :wardId and r.pollingUnit = :pollingUnitId")
//    @Query("select r from Result r where r.election = :elecction")
    Result findByElectionAndPollingUnit(Election election, PollingUnit pollingUnit);
    Result findByElectionAndWardAndVotingLevel(Election election, Ward ward, VotingLevel votingLevel);
    Result findByElectionAndLgaAndVotingLevel(Election election, Lga lga, VotingLevel votingLevel);
    // @Param("wardId")Long wardId,@Param("pollingUnitId")Long pollingUnitId);
    List<Result> findByWard(Ward ward);
    List<Result> findByLga(Lga lga);
    List<Result> findBySenatorialDistrict(SenatorialDistrict senatorialDistrict);
    List<Result> findByPollingUnit(PollingUnit pollingUnit);
    List<Result> findByStateId(Long stateId);
    void deleteByWard(Ward ward);
    void deleteByLga(Lga lga);
    
}
