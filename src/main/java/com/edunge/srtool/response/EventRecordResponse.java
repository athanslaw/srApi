package com.edunge.srtool.response;

import com.edunge.srtool.model.EventRecord;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventRecordResponse extends BaseResponse{
    EventRecord eventRecord;
    List<EventRecord> eventRecords;
    Integer count;

    public Integer getCount() {
        return count;
    }
    public EventRecordResponse(String code, String message, EventRecord eventRecord) {
        super(code, message);
        this.eventRecord = eventRecord;
    }

    public EventRecordResponse(EventRecord eventRecord) {
        this.eventRecord = eventRecord;
    }

    public EventRecordResponse(String code, String message) {
        super(code, message);
    }

    public EventRecordResponse(String code, String message, List<EventRecord> eventRecords) {
        super(code, message);
        this.count  = eventRecords.size();
        this.eventRecords = eventRecords;
    }

    public EventRecord getEventRecord() {
        return eventRecord;
    }

    public void setEventRecord(EventRecord eventRecord) {
        this.eventRecord = eventRecord;
    }

    public List<EventRecord> getEventRecords() {
        return eventRecords;
    }

    public void setEventRecords(List<EventRecord> eventRecords) {
        this.eventRecords = eventRecords;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
