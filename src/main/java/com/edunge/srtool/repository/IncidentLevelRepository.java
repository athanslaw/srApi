package com.edunge.srtool.repository;

import com.edunge.srtool.model.IncidentLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentLevelRepository extends JpaRepository<IncidentLevel,Long> {
    IncidentLevel findByCode(String code);
    IncidentLevel findByNameLike(String name);
}
