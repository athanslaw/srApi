package com.edunge.srtool.repository;

import com.edunge.srtool.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    Result findByElectionAndWardAndPollingUnit(Long electionId, Long wardId, Long pollingUnitId);
}
