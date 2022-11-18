package com.edunge.srtool.response;

import com.edunge.srtool.model.Event;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventResponse extends BaseResponse{
    Event event;
    List<Event> events;
    Integer count;

    public Integer getCount() {
        return count;
    }
    public EventResponse(String code, String message, Event event) {
        super(code, message);
        this.event = event;
    }

    public EventResponse(Event event) {
        this.event = event;
    }

    public EventResponse(String code, String message) {
        super(code, message);
    }

    public EventResponse(String code, String message, List<Event> events) {
        super(code, message);
        this.count  = events.size();
        this.events = events;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
