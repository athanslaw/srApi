package com.edunge.srtool.service.impl;

import com.edunge.srtool.dto.EventRecordDto;
import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.EventRecord;
import com.edunge.srtool.model.PollingUnit;
import com.edunge.srtool.repository.EventRecordRepository;
import com.edunge.srtool.repository.PollingUnitRepository;
import com.edunge.srtool.response.EventRecordResponse;
import com.edunge.srtool.service.EventRecordService;
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
            String combinedKeys = eventRecordDto.getPollingUnitId() + "" + eventRecordDto.getEventId();
            EventRecord eventRecord = new EventRecord();
            PollingUnit pu = pollingUnitRepository.findById(eventRecordDto.getPollingUnitId()).get();

            eventRecord.setEventStatus(eventRecordDto.getEventStatus());
            eventRecord.setAgentId(eventRecordDto.getAgentId());
            eventRecord.setCombinedKeys(combinedKeys);
            eventRecord.setEventId(eventRecordDto.getEventId());
            eventRecord.setDescription(eventRecordDto.getDescription());
            eventRecord.setLga(eventRecordDto.getLgaId());
            eventRecord.setWard(pu.getWard().getId());
            eventRecord.setPollingUnit(eventRecord.getPollingUnit());
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
        List<EventRecord> EventRecord = eventRecordRepository.findByEventId(id);
        return new EventRecordResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), EventRecord);
    }

    @Override
    public EventRecordResponse findEventRecordByPollingUnit(Long id) throws NotFoundException {
        List<EventRecord> EventRecord = eventRecordRepository.findByPollingUnit(id);
        return new EventRecordResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), EventRecord);
    }

    @Override
    public EventRecordResponse findEventRecordByWard(Long id) throws NotFoundException {
        List<EventRecord> EventRecord = eventRecordRepository.findByWard(id);
        return new EventRecordResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), EventRecord);
    }

    @Override
    public EventRecordResponse findEventRecordByLga(Long id) throws NotFoundException {
        List<EventRecord> EventRecord = eventRecordRepository.findByLga(id);
        return new EventRecordResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), EventRecord);
    }

    @Override
    public EventRecordResponse findEventRecordByState(Long id) throws NotFoundException {
        List<EventRecord> EventRecord = eventRecordRepository.findByStateId(id);
        return new EventRecordResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), EventRecord);
    }

    @Override
    public EventRecordResponse findEventRecordByZone(Long id) throws NotFoundException {
        List<EventRecord> EventRecord = eventRecordRepository.findByGeoPoliticalZoneId(id);
        return new EventRecordResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), EventRecord);
    }


    @Override
    public EventRecordResponse findEventRecordByPollingUnitAndEventId(Long id, Long eventId) throws NotFoundException {
        List<EventRecord> EventRecord = eventRecordRepository.findByPollingUnitAndEventId(id, eventId);
        return new EventRecordResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), EventRecord);
    }

    @Override
    public EventRecordResponse findEventRecordByWardAndEventId(Long id, Long eventId) throws NotFoundException {
        List<EventRecord> EventRecord = eventRecordRepository.findByWardAndEventId(id, eventId);
        return new EventRecordResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), EventRecord);
    }

    @Override
    public EventRecordResponse findEventRecordByLgaAndEventId(Long id, Long eventId) throws NotFoundException {
        List<EventRecord> EventRecord = eventRecordRepository.findByLgaAndEventId(id, eventId);
        return new EventRecordResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), EventRecord);
    }

    @Override
    public EventRecordResponse findEventRecordByStateAndEventId(Long id, Long eventId) throws NotFoundException {
        List<EventRecord> EventRecord = eventRecordRepository.findByStateIdAndEventId(id, eventId);
        return new EventRecordResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), EventRecord);
    }

    @Override
    public EventRecordResponse findEventRecordByZoneAndEventId(Long id, Long eventId) throws NotFoundException {
        List<EventRecord> EventRecord = eventRecordRepository.findByGeoPoliticalZoneIdAndEventId(id, eventId);
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
                eventRecordRepository.findByEventStatus(status));
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
