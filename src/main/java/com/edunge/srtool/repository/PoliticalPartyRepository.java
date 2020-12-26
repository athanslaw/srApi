package com.edunge.srtool.repository;

import com.edunge.srtool.model.PoliticalParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoliticalPartyRepository extends JpaRepository<PoliticalParty, Long> {
    PoliticalParty findByCode(String code);
    PoliticalParty findByNameLike (String name);
}
