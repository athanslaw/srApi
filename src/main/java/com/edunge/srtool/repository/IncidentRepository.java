package com.edunge.srtool.repository;

import com.edunge.srtool.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    Incident findByWardAndPollingUnit( Ward ward, PollingUnit pollingUnit);
    List<Incident> findByLga(Lga lga);
    List<Incident> findByWard(Ward ward);
    List<Incident> findByPollingUnit(PollingUnit pollingUnit);
}
