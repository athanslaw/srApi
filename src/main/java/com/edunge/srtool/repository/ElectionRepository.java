package com.edunge.srtool.repository;

import com.edunge.srtool.model.Election;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectionRepository extends JpaRepository<Election,Long> {
    Election findByCode(String electionCode);
    Election findByNameStartingWith(String name);
}
