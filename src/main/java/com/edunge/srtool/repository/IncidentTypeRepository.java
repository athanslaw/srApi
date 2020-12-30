package com.edunge.srtool.repository;

import com.edunge.srtool.model.IncidentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidentTypeRepository extends JpaRepository<IncidentType,Long> {
    IncidentType findByCode(String code);
    List<IncidentType> findByNameStartingWith(String name);
}
