package com.edunge.srtool.repository;

import com.edunge.srtool.model.PollingUnit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollingUnitRepository extends JpaRepository<PollingUnit, Long> {
    PollingUnit findByCode(String wardCode);
    PollingUnit findByNameStartingWith(String name);
}
