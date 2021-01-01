package com.edunge.srtool.repository;

import com.edunge.srtool.model.PollingUnit;
import com.edunge.srtool.model.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PollingUnitRepository extends JpaRepository<PollingUnit, Long> {
    PollingUnit findByCode(String wardCode);
    List<PollingUnit> findByNameStartingWith(String name);
    List<PollingUnit> findByWard(Ward ward);
}
