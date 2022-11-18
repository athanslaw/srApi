package com.edunge.srtool.repository;

import com.edunge.srtool.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByCombinedKeysOrderByTimeStampDesc( String combinedKeys); // order by timestamp descending
    List<Incident> findByLgaAndIncidentGroupId(Lga lga, Long incidentGroupId);
    List<Incident> findByStateIdAndIncidentGroupId(long stateId, Long incidentGroupId);
    List<Incident> findByGeoPoliticalZoneIdAndIncidentGroupId(long zone, Long incidentGroupId);
    List<Incident> findByWardAndIncidentGroupId(Ward ward, Long incidentGroupId);
    List<Incident> findByPollingUnitAndIncidentGroupId(PollingUnit pollingUnit, Long incidentGroupId);

    List<Incident> findByLgaAndIncidentTypeAndIncidentGroupId(Lga lga, IncidentType incidentType, Long incidentGroupId);
    List<Incident> findByWardAndIncidentTypeAndIncidentGroupId(Ward ward, IncidentType incidentType, Long incidentGroupId);
    List<Incident> findByPollingUnitAndIncidentTypeAndIncidentGroupId(PollingUnit pollingUnit, IncidentType incidentType, Long incidentGroupId);

    List<Incident> findByLgaAndWeightAndIncidentGroupId(Lga lga, int weight, Long incidentGroupId);
    List<Incident> findByWardAndWeightAndIncidentGroupId(Ward ward, int weight, Long incidentGroupId);
    List<Incident> findByPollingUnitAndWeightAndIncidentGroupId(PollingUnit pollingUnit, int weight, Long incidentGroupId);

    @Query(nativeQuery = true, value="SELECT * FROM incident a WHERE a.state_id=?1 AND a.incident-group-id=?2 ORDER BY a.id DESC LIMIT 10")
    List<Incident> findTop10(State state, Long incidentGroupId);
    List<Incident> findByWeightAndIncidentGroupId(int weight, Long incidentGroupId);
    List<Incident> findByIncidentTypeAndIncidentGroupId(IncidentType incidentType, Long incidentGroupId);
}
