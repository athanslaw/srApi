package com.edunge.srtool.repository;

import com.edunge.srtool.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRealTimeRepository extends JpaRepository<Result, Long> {
    void deleteByWard(Ward ward);
//    @Query("select r from Result r where r.election = :electionId and r.ward = :wardId and r.pollingUnit = :pollingUnitId")
//    @Query("select r from Result r where r.election = :elecction")
    Result findByElectionAndWardAndPollingUnit(Election election, Ward ward, PollingUnit pollingUnit);
    //                    @Param("wardId")Long wardId,@Param("pollingUnitId")Long pollingUnitId);
    List<Result> findByWard(Ward ward);
    List<Result> findByLga(Lga lga);
    List<Result> findByPollingUnit(PollingUnit pollingUnit);
    void deleteByLga(Lga lga);
    
}
