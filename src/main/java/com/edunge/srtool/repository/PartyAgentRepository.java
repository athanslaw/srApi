package com.edunge.srtool.repository;

import com.edunge.srtool.model.PartyAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyAgentRepository extends JpaRepository<PartyAgent, Long> {
}
