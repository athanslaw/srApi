package com.edunge.srtool.repository;

import com.edunge.srtool.model.PoliticalParty;
import com.edunge.srtool.model.Result;
import com.edunge.srtool.model.ResultPerParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultPerPartyRepository extends JpaRepository<ResultPerParty, Long> {
    ResultPerParty findByResultAndPoliticalParty(Result result, PoliticalParty politicalParty);
    List<ResultPerParty> findByResult(Result result);
    void deleteByResult(Result result);
}
