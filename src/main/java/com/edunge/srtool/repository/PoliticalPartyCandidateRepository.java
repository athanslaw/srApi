package com.edunge.srtool.repository;

import com.edunge.srtool.model.PoliticalPartyCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PoliticalPartyCandidateRepository extends JpaRepository<PoliticalPartyCandidate, Long> {
    List<PoliticalPartyCandidate> findByFirstnameOrLastname(String firstname, String lastname);
}
