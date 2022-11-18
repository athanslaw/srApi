package com.edunge.srtool.repository;

import com.edunge.srtool.model.EventRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRecordRepository extends JpaRepository<EventRecord,Long> {
    List<EventRecord> findByEventStatus(Boolean eventStatus);
    List<EventRecord> findByPollingUnit(Long pollingUnit);
    List<EventRecord> findByLga(Long lga);
    List<EventRecord> findByGeoPoliticalZoneId(Long geoPoliticalZoneId);
    List<EventRecord> findByEventId(Long eventId);
    List<EventRecord> findByStateId(Long stateId);
    List<EventRecord> findByWard(Long ward);

    List<EventRecord> findByPollingUnitAndEventId(Long pollingUnit, Long eventId);
    List<EventRecord> findByLgaAndEventId(Long lga, Long eventId);
    List<EventRecord> findByGeoPoliticalZoneIdAndEventId(Long geoPoliticalZoneId, Long eventId);
    List<EventRecord> findByStateIdAndEventId(Long stateId, Long eventId);
    List<EventRecord> findByWardAndEventId(Long ward, Long eventId);

}
