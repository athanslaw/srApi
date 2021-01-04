package com.edunge.srtool.repository;

import com.edunge.srtool.model.PoliticalParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PoliticalPartyRepository extends JpaRepository<PoliticalParty, Long> {
    PoliticalParty findByCode(String code);
    List<PoliticalParty> findByNameStartingWith(String name);
//    List<PoliticalParty> findByState(State state);
}
