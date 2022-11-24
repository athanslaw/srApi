package com.edunge.srtool.service.impl;

import com.edunge.srtool.dto.EventRecordDto;
import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.EventRecord;
import com.edunge.srtool.model.IncidentGroup;
import com.edunge.srtool.model.PollingUnit;
import com.edunge.srtool.repository.EventRecordRepository;
import com.edunge.srtool.repository.PollingUnitRepository;
import com.edunge.srtool.response.EventRecordResponse;
import com.edunge.srtool.service.EventRecordService;
import com.edunge.srtool.service.IncidentGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EventRecordServiceImpl implements EventRecordService {

    @Autowired
    private EventRecordRepository eventRecordRepository;
    @Autowired
    private IncidentGroupService incidentGroupService;

    @Autowired
    private PollingUnitRepository pollingUnitRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(EventRecordServiceImpl.class);

    private static final String SERVICE_NAME = "EventRecord";

    @Value("${notfound.message.template}")
    private String notFoundTemplate;

    @Value("${success.message.template}")
    private String successTemplate;

    @Value("${duplicate.message.template}")
    private String duplicateTemplate;

    @Value("${update.message.template}")
    private String updateTemplate;

    @Value("${delete.message.template}")
    private String deleteTemplate;

    @Value("${fetch.message.template}")
    private String fetchRecordTemplate;

    @Override
    public EventRecordResponse saveEventRecord(EventRecordDto eventRecordDto) {

        try {
            Long incidentGroupId = incidentGroupService.getActiveIncidentGroupId();
            String combinedKeys = eventRecordDto.getPollingUnit() + "_" + eventRecordDto.getEventId() + "_" + incidentGroupId;
            List<EventRecord> eventRecords = eventRecordRepository.findByCombinedKeys(combinedKeys);
            EventRecord eventRecord;
            if(eventRecords.size() > 0){
                eventRecord = eventRecords.get(0);
                eventRecord.setEventStatus(eventRecordDto.getEventStatus());
                eventRecordRepository.save(eventRecord);
                return new EventRecordResponse("00", String.format(successTemplate, SERVICE_NAME), eventRecord);
            }
            eventRecord = new EventRecord();
            PollingUnit pu = pollingUnitRepository.findById(eventRecordDto.getPollingUnit()).get();

            eventRecord.setEventStatus(eventRecordDto.getEventStatus());
            eventRecord.setIncidentGroupId(incidentGroupId);
            eventRecord.setPollingUnitName(pu.getCode()+" - "+pu.getName());
            eventRecord.setLgaName(pu.getLga().getName());
            eventRecord.setSenatorialDistrictId(pu.getSenatorialDistrict().getId());
            eventRecord.setSenatorialDistrictName(pu.getSenatorialDistrict().getName());
            eventRecord.setWardName(pu.getWard().getCode()+" - "+pu.getWard().getName());
            eventRecord.setStateName(pu.getState().getName());
            eventRecord.setGeoPoliticalZoneName(pu.getState().getGeoPoliticalZone().getName());
            eventRecord.setAgentId(eventRecordDto.getAgentId());
            eventRecord.setCombinedKeys(combinedKeys);
            eventRecord.setEventId(eventRecordDto.getEventId());
            eventRecord.setDescription(eventRecordDto.getDescription());
            eventRecord.setLga(eventRecordDto.getLga());
            eventRecord.setWard(pu.getWard().getId());
            eventRecord.setPollingUnit(eventRecordDto.getPollingUnit());
            eventRecord.setGeoPoliticalZoneId(pu.getState().getGeoPoliticalZone().getId());
            eventRecord.setStateId(pu.getState().getId());

            eventRecordRepository.save(eventRecord);
            return new EventRecordResponse("00", String.format(successTemplate, SERVICE_NAME), eventRecord);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        throw new DuplicateException(String.format(duplicateTemplate, eventRecordDto.getCode()));
    }

    @Override
    public EventRecordResponse findEventRecordById(Long id) throws NotFoundException {
        EventRecord EventRecord = getEventRecord(id);
        return new EventRecordResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), EventRecord);
    }

    @Override
    public EventRecordResponse findEventRecordByEventId(Long id) throws NotFoundException {
        List<EventRecord> EventRecord = eventRecordRepository.findByEventIdAndIncidentGroupId(id, getActiveIncidentGroupId());
        return new EventRecordResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), EventRecord);
    }

    @Override
    public EventRecordResponse findEventRecordByPollingUnit(Long id) throws NotFoundException {
        List<EventRecord> EventRecord = eventRecordRepository.findByPollingUnitAndIncidentGroupId(id, getActiveIncidentGroupId());
        return new EventRecordResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), EventRecord);
    }

    private String getPollingUnit(Long id){
        PollingUnit pu = pollingUnitRepository.findById(id).get();
        return pu.getCode()+" - "+pu.getName();
    }

    @Override
    public EventRecordResponse findEventRecordByWard(Long id) throws NotFoundException {
        List<EventRecord> eventRecord = eventRecordRepository.findByWardAndIncidentGroupId(id, getActiveIncidentGroupId());
        return new EventRecordResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), eventRecord);
    }

    @Override
    public EventRecordResponse findEventRecordByLga(Long id) {
        List<EventRecord> EventRecord = eventRecordRepository.findByLgaAndIncidentGroupId(id, getActiveIncidentGroupId());
        return new EventRecordResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), EventRecord);
    }

    @Override
    public EventRecordResponse findEventRecordByState(Long id) throws NotFoundException {
        List<EventRecord> EventRecord = eventRecordRepository.findByStateIdAndIncidentGroupId(id, getActiveIncidentGroupId());
        return new EventRecordResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), EventRecord);
    }

    @Override
    public EventRecordResponse findEventRecordBySenatorial(Long id) throws NotFoundException {
        List<EventRecord> EventRecord = eventRecordRepository.findBySenatorialDistrictIdAndIncidentGroupId(id, getActiveIncidentGroupId());
        return new EventRecordResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), EventRecord);
    }

    @Override
    public EventRecordResponse findEventRecordByZone(Long id) throws NotFoundException {
        List<EventRecord> EventRecord = eventRecordRepository.findByGeoPoliticalZoneIdAndIncidentGroupId(id, getActiveIncidentGroupId());
        return new EventRecordResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), EventRecord);
    }


    @Override
    public EventRecordResponse findEventRecordByPollingUnitAndEventId(Long id, Long eventId) throws NotFoundException {
        List<EventRecord> EventRecord = eventRecordRepository.findByPollingUnitAndEventIdAndIncidentGroupId(id, eventId, getActiveIncidentGroupId());
        return new EventRecordResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), EventRecord);
    }

    @Override
    public EventRecordResponse findEventRecordByWardAndEventId(Long id, Long eventId) throws NotFoundException {
        List<EventRecord> EventRecord = eventRecordRepository.findByWardAndEventIdAndIncidentGroupId(id, eventId, getActiveIncidentGroupId());
        return new EventRecordResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), EventRecord);
    }

    @Override
    public EventRecordResponse findEventRecordByLgaAndEventId(Long id, Long eventId) throws NotFoundException {
        List<EventRecord> EventRecord = eventRecordRepository.findByLgaAndEventIdAndIncidentGroupId(id, eventId, getActiveIncidentGroupId());
        return new EventRecordResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), EventRecord);
    }

    @Override
    public EventRecordResponse findEventRecordByStateAndEventId(Long id, Long eventId) throws NotFoundException {
        List<EventRecord> EventRecord = eventRecordRepository.findByStateIdAndEventIdAndIncidentGroupId(id, eventId, getActiveIncidentGroupId());
        return new EventRecordResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), EventRecord);
    }

    @Override
    public EventRecordResponse findEventRecordBySenatorialAndEventId(Long id, Long eventId) throws NotFoundException {
        List<EventRecord> EventRecord = eventRecordRepository.findBySenatorialDistrictIdAndEventIdAndIncidentGroupId(id, eventId, getActiveIncidentGroupId());
        return new EventRecordResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), EventRecord);
    }

    @Override
    public EventRecordResponse findEventRecordByZoneAndEventId(Long id, Long eventId) throws NotFoundException {
        List<EventRecord> EventRecord = eventRecordRepository.findByGeoPoliticalZoneIdAndEventIdAndIncidentGroupId(id, eventId, getActiveIncidentGroupId());
        return new EventRecordResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), EventRecord);
    }

    @Override
    public EventRecordResponse updateEventRecord(Long id, EventRecordDto eventRecordDto) throws NotFoundException {
        EventRecord eventRecord = getEventRecord(id);
        try {
            eventRecord.setId(id);
            eventRecord.setAgentId(eventRecordDto.getAgentId());
            eventRecord.setEventId(eventRecordDto.getEventId());
            eventRecord.setDescription(eventRecordDto.getDescription());
            eventRecord.setEventStatus(eventRecordDto.getEventStatus());
            eventRecordRepository.save(eventRecord);
            return new EventRecordResponse("00", String.format(updateTemplate, SERVICE_NAME), eventRecord);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public EventRecordResponse deleteEventRecordById(Long id) throws NotFoundException {
        EventRecord eventRecord = getEventRecord(id);
        eventRecordRepository.delete(eventRecord);
        return new EventRecordResponse("00",String.format(deleteTemplate,id));
    }

    @Override
    public EventRecordResponse findAll() {
        List<EventRecord> EventRecords = eventRecordRepository.findAll();
        return new EventRecordResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), EventRecords);
    }

    @Override
    public EventRecordResponse findEventRecordByStatus(Boolean status) {
        return new EventRecordResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME),
                eventRecordRepository.findByEventStatusAndIncidentGroupId(status, getActiveIncidentGroupId()));
    }

    private Long getActiveIncidentGroupId(){
        return incidentGroupService.getActiveIncidentGroupId();
    }

    @Override
    public void updateEventRecordStatus(Long id, Boolean status){
        EventRecord eventRecord = eventRecordRepository.findById(id).get();
        eventRecord.setEventStatus(status);
        eventRecordRepository.save(eventRecord);
    }

    private EventRecord getEventRecord(Long id) throws NotFoundException {
        Optional<EventRecord> currentEventRecord = eventRecordRepository.findById(id);
        if(!currentEventRecord.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return currentEventRecord.get();
    }
}
