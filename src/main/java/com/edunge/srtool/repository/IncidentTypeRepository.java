package com.edunge.srtool.repository;

import com.edunge.srtool.model.IncidentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentTypeRepository extends JpaRepository<IncidentType,Long> {
    IncidentType findByCode(String code);
    IncidentType findByNameStartingWith(String name);
}
