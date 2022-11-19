package com.edunge.srtool.service;

import com.edunge.srtool.dto.EventRecordDto;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.EventRecordResponse;

public interface EventRecordService {
    EventRecordResponse saveEventRecord(EventRecordDto eventRecordDto);
    EventRecordResponse findEventRecordById(Long id) throws NotFoundException;
    EventRecordResponse findEventRecordByEventId(Long id) throws NotFoundException;
    EventRecordResponse findEventRecordByPollingUnit(Long id) throws NotFoundException;
    EventRecordResponse findEventRecordByWard(Long id) throws NotFoundException;
    EventRecordResponse findEventRecordByLga(Long id) throws NotFoundException;
    EventRecordResponse findEventRecordByState(Long id) throws NotFoundException;
    EventRecordResponse findEventRecordBySenatorial(Long id) throws NotFoundException;
    EventRecordResponse findEventRecordByZone(Long id) throws NotFoundException;
    EventRecordResponse findEventRecordByPollingUnitAndEventId(Long id, Long eventId) throws NotFoundException;
    EventRecordResponse findEventRecordByWardAndEventId(Long id, Long eventId) throws NotFoundException;
    EventRecordResponse findEventRecordByLgaAndEventId(Long id, Long eventId) throws NotFoundException;
    EventRecordResponse findEventRecordByStateAndEventId(Long id, Long eventId) throws NotFoundException;
    EventRecordResponse findEventRecordBySenatorialAndEventId(Long id, Long eventId) throws NotFoundException;
    EventRecordResponse findEventRecordByZoneAndEventId(Long id, Long eventId) throws NotFoundException;
    EventRecordResponse updateEventRecord(Long id, EventRecordDto eventRecordDto) throws NotFoundException;
    EventRecordResponse deleteEventRecordById(Long id) throws NotFoundException;
    EventRecordResponse findAll();
    EventRecordResponse findEventRecordByStatus(Boolean status);
    void updateEventRecordStatus(Long id, Boolean status);
}
