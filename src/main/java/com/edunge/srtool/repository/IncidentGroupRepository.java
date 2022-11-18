package com.edunge.srtool.repository;

import com.edunge.srtool.model.IncidentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentGroupRepository extends JpaRepository<IncidentGroup,Long> {
    IncidentGroup findByCode(String IncidentCode);
    List<IncidentGroup> findByNameStartingWith(String name);
    List<IncidentGroup> findByStatus(Boolean status);
}
