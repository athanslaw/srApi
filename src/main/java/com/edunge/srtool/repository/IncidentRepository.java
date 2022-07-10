package com.edunge.srtool.repository;

import com.edunge.srtool.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByCombinedKeysOrderByTimeStampDesc( String combinedKeys); // order by timestamp descending
    List<Incident> findByLga(Lga lga);
    List<Incident> findByStateId(long stateId);
    List<Incident> findByWard(Ward ward);
    List<Incident> findByPollingUnit(PollingUnit pollingUnit);

    List<Incident> findByLgaAndIncidentType(Lga lga, IncidentType incidentType);
    List<Incident> findByWardAndIncidentType(Ward ward, IncidentType incidentType);
    List<Incident> findByPollingUnitAndIncidentType(PollingUnit pollingUnit, IncidentType incidentType);

    List<Incident> findByLgaAndWeight(Lga lga, int weight);
    List<Incident> findByWardAndWeight(Ward ward, int weight);
    List<Incident> findByPollingUnitAndWeight(PollingUnit pollingUnit, int weight);

    @Query(nativeQuery = true, value="SELECT * FROM incident a WHERE a.state_id=?1 ORDER BY a.id DESC LIMIT 10")
    List<Incident> findTop10(State state);
    List<Incident> findByWeight(int weight);
    List<Incident> findByIncidentType(IncidentType incidentType);
}
