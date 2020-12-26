package com.edunge.srtool.repository;

import com.edunge.srtool.model.IncidentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentStatusRepository extends JpaRepository<IncidentStatus,Long> {
    IncidentStatus findByCode(String code);
    IncidentStatus findByNameLike(String name);
}
