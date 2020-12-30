package com.edunge.srtool.repository;

import com.edunge.srtool.model.IncidentLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidentLevelRepository extends JpaRepository<IncidentLevel,Long> {
    IncidentLevel findByCode(String code);
    List<IncidentLevel> findByNameStartingWith(String name);
}
