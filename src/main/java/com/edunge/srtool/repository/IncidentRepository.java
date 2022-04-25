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
    @Query(nativeQuery = true, value="SELECT * FROM incident a ORDER BY a.id DESC LIMIT 10")
    List<Incident> findTop10(State state);
}
