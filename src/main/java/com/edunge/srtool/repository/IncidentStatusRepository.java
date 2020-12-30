package com.edunge.srtool.repository;

import com.edunge.srtool.model.IncidentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidentStatusRepository extends JpaRepository<IncidentStatus,Long> {
    IncidentStatus findByCode(String code);
    List<IncidentStatus> findByNameStartingWith(String name);
}
