package com.edunge.srtool.repository;

import com.edunge.srtool.model.EventRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRecordRepository extends JpaRepository<EventRecord,Long> {
    List<EventRecord> findByEventStatusAndIncidentGroupId(Boolean eventStatus, Long incidentGroupId);
    List<EventRecord> findByPollingUnitAndIncidentGroupId(Long pollingUnit, Long incidentGroupId);
    List<EventRecord> findByLgaAndIncidentGroupId(Long lga, Long incidentGroupId);
    List<EventRecord> findByGeoPoliticalZoneIdAndIncidentGroupId(Long geoPoliticalZoneId, Long incidentGroupId);
    List<EventRecord> findByEventIdAndIncidentGroupId(Long eventId, Long incidentGroupId);
    List<EventRecord> findByCombinedKeys(String combinedKey);
    List<EventRecord> findByStateIdAndIncidentGroupId(Long stateId, Long incidentGroupId);
    List<EventRecord> findBySenatorialDistrictIdAndIncidentGroupId(Long senatorial, Long incidentGroupId);
    List<EventRecord> findByWardAndIncidentGroupId(Long ward, Long incidentGroupId);

    List<EventRecord> findByPollingUnitAndEventIdAndIncidentGroupId(Long pollingUnit, Long eventId, Long incidentGroupId);
    List<EventRecord> findByLgaAndEventIdAndIncidentGroupId(Long lga, Long eventId, Long incidentGroupId);
    List<EventRecord> findByGeoPoliticalZoneIdAndEventIdAndIncidentGroupId(Long geoPoliticalZoneId, Long eventId, Long incidentGroupId);
    List<EventRecord> findByStateIdAndEventIdAndIncidentGroupId(Long stateId, Long eventId, Long incidentGroupId);
    List<EventRecord> findBySenatorialDistrictIdAndEventIdAndIncidentGroupId(Long senatorial, Long eventId, Long incidentGroupId);
    List<EventRecord> findByWardAndEventIdAndIncidentGroupId(Long ward, Long eventId, Long incidentGroupId);

}
