package com.edunge.srtool.repository;

import com.edunge.srtool.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PollingUnitRepository extends JpaRepository<PollingUnit, Long> {
    PollingUnit findByCode(String wardCode);
    List<PollingUnit> findByNameStartingWith(String name);
    List<PollingUnit> findByWard(Ward ward);
    List<PollingUnit> findByLga(Lga lga);
    long countByLga(Lga lga);
    long countByWard(Ward ward);
    long countBySenatorialDistrict(SenatorialDistrict senatorialDistrict);
    long countByState(State state);
    List<PollingUnit> findByState(State state);
    List<PollingUnit> findBySenatorialDistrict(SenatorialDistrict senatorialDistrict);
}
