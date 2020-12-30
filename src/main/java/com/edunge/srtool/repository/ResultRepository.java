package com.edunge.srtool.repository;

import com.edunge.srtool.model.Election;
import com.edunge.srtool.model.PollingUnit;
import com.edunge.srtool.model.Result;
import com.edunge.srtool.model.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
//    @Query(value = "SELECT * from result WHERE election_id = ?1 and ward_id=?1 and polling_unit_id=?1", nativeQuery = true)
//    @Query("select r from Result r where r.election = :electionId and r.ward = :wardId and r.pollingUnit = :pollingUnitId")
//    @Query("select r from Result r where r.election = :elecction")
    Result findByElectionAndWardAndPollingUnit(Election election, Ward ward, PollingUnit pollingUnit);
    //                    @Param("wardId")Long wardId,@Param("pollingUnitId")Long pollingUnitId);
}
