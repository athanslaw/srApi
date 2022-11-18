package com.edunge.srtool.service;

import com.edunge.srtool.dto.EventDto;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.EventResponse;

public interface EventService {
    EventResponse saveEvent(EventDto eventDto) throws NotFoundException;
    EventResponse findEventById(Long id) throws NotFoundException;
    EventResponse updateEvent(Long id, EventDto eventDto) throws NotFoundException;
    EventResponse activateEvent(Long id) throws NotFoundException;
    EventResponse deactivateEvent(Long id) throws NotFoundException;
    EventResponse deleteEventById(Long id) throws NotFoundException;
    EventResponse findAll();
    EventResponse findEventByStatus(Boolean status);
    void updateEventStatus(Long id, Boolean status);
}
