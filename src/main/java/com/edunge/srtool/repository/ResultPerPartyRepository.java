package com.edunge.srtool.repository;

import com.edunge.srtool.model.ResultPerParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultPerPartyRepository extends JpaRepository<ResultPerParty, Long> {
}
