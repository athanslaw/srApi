package com.edunge.srtool.repository;

import com.edunge.srtool.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRealTimeRepository extends JpaRepository<ResultRealTime, Long> {
    void deleteByWard(Ward ward);
    void deleteByResult(Long result);
    List<ResultRealTime> findByElectionAndLgaAndVotingLevel(Election election, Lga lga, VotingLevel votingLevelId);
    List<ResultRealTime> findByElectionAndWardAndVotingLevelLessThan(Election election, Ward ward, VotingLevel votingLevelId);
    //                    @Param("wardId")Long wardId,@Param("pollingUnitId")Long pollingUnitId);
    List<ResultRealTime> findByElectionAndLga(Election election, Lga lga);
    List<ResultRealTime> findByLga(Lga lga);
//    List<ResultRealTime> findByElection(Election election);
    List<ResultRealTime> findBySenatorialDistrict(SenatorialDistrict senatorialDistrict);
    List<ResultRealTime> findByStateId(long stateId);
    void deleteByLga(Lga lga);
    
}
