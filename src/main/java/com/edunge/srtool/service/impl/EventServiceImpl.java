package com.edunge.srtool.service.impl;

import com.edunge.srtool.dto.EventDto;
import com.edunge.srtool.exceptions.DuplicateException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.Event;
import com.edunge.srtool.repository.EventRepository;
import com.edunge.srtool.repository.PollingUnitRepository;
import com.edunge.srtool.response.EventResponse;
import com.edunge.srtool.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private PollingUnitRepository pollingUnitRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceImpl.class);

    private static final String SERVICE_NAME = "Event";

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
    public EventResponse saveEvent(EventDto eventDto) {

        try {
            Event event = new Event();

            event.setStatus(true);
            event.setCode(eventDto.getCode());
            event.setName("");
            event.setDescription(eventDto.getDescription());

            eventRepository.save(event);
            return new EventResponse("00", String.format(successTemplate, SERVICE_NAME), event);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        throw new DuplicateException(String.format(duplicateTemplate, eventDto.getCode()));
    }

    @Override
    public EventResponse findEventById(Long id) throws NotFoundException {
        Event event = getEvent(id);
        return new EventResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), event);
    }

    @Override
    public EventResponse updateEvent(Long id, EventDto eventDto) throws NotFoundException {
        Event event = getEvent(id);
        try {
            event.setId(id);
            event.setDescription(eventDto.getDescription());
            event.setCode(eventDto.getCode());
            event.setStatus(eventDto.getStatus());
            return new EventResponse("00", String.format(updateTemplate, SERVICE_NAME), event);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public EventResponse activateEvent(Long id) throws NotFoundException {
        Event event = getEvent(id);
        try {
            event.setStatus(true);
            return new EventResponse("00", String.format(updateTemplate, SERVICE_NAME), event);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public EventResponse deactivateEvent(Long id) throws NotFoundException {
        Event event = getEvent(id);
        try {
            event.setStatus(false);
            return new EventResponse("00", String.format(updateTemplate, SERVICE_NAME), event);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public EventResponse deleteEventById(Long id) throws NotFoundException {
        Event event = getEvent(id);
        eventRepository.delete(event);
        return new EventResponse("00",String.format(deleteTemplate,id));
    }

    @Override
    public EventResponse findAll() {
        List<Event> events = eventRepository.findAll();
        return new EventResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), events);
    }

    @Override
    public EventResponse findEventByStatus(Boolean status) {
        return new EventResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME),
                eventRepository.findByStatus(status));
    }

    @Override
    public void updateEventStatus(Long id, Boolean status){
        Event event = eventRepository.findById(id).get();
        event.setStatus(!event.getStatus());
        eventRepository.save(event);
    }

    private Event getEvent(Long id) throws NotFoundException {
        Optional<Event> currentEventRecord = eventRepository.findById(id);
        if(!currentEventRecord.isPresent()){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return currentEventRecord.get();
    }
}
