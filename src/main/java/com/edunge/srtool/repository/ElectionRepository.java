package com.edunge.srtool.repository;

import com.edunge.srtool.model.Election;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElectionRepository extends JpaRepository<Election,Long> {
    Election findByCode(String electionCode);
    List<Election> findByNameStartingWith(String name);
    List<Election> findByStatus(Boolean status);
}
