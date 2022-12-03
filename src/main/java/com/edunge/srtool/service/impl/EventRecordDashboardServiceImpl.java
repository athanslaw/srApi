package com.edunge.srtool.service.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.EventRecord;
import com.edunge.srtool.model.Lga;
import com.edunge.srtool.model.SenatorialDistrict;
import com.edunge.srtool.model.State;
import com.edunge.srtool.model.Ward;
import com.edunge.srtool.response.EventRecordDashboardResponse;
import com.edunge.srtool.service.EventRecordDashboardService;
import com.edunge.srtool.service.EventRecordService;

@Service
public class EventRecordDashboardServiceImpl implements EventRecordDashboardService {
    private final EventRecordService eventRecordService;
    private final PollingUnitServiceImpl pollingUnitService;
    private final StateServiceImpl stateService;


    public EventRecordDashboardServiceImpl(EventRecordService eventRecordService, PollingUnitServiceImpl pollingUnitService,
                                           StateServiceImpl stateService) {
        this.eventRecordService = eventRecordService;
        this.pollingUnitService = pollingUnitService;
        this.stateService = stateService;
    }

    @Override
    public EventRecordDashboardResponse getDashboardByState(String eventId) throws NotFoundException {
        State state = this.getState();
        return getDashboardByState(state.getId(), eventId);
    }

    @Override
    public EventRecordDashboardResponse getDashboardByState(Long stateId, String eventId) throws NotFoundException {

        State state = new State(){{setId(stateId);}};
        List<EventRecord> eventList = getStateEvents(state, eventId);
        Integer totalPUs = (int)pollingUnitService.countByState(state);
        if(eventList == null || eventList.size() == 0) {
            return new EventRecordDashboardResponse(0l, 0l, totalPUs);
        }

        return getEventRecordDashboardResponse(eventList, totalPUs);
    }

    @Override
    public EventRecordDashboardResponse getDashboardBySenatorialDistrict(Long id, String eventId) throws NotFoundException {

        SenatorialDistrict senatorialDistrict = new SenatorialDistrict(){{setId(id);}};
        List<EventRecord> eventList = getDistrictEvents(id, eventId);
        Integer totalPUs = (int)pollingUnitService.countBySenatorialDistrict(senatorialDistrict);
        if(eventList == null || eventList.size() == 0) {
            return new EventRecordDashboardResponse(0l, 0l, totalPUs);
        }

        return getEventRecordDashboardResponse(eventList, totalPUs);
    }

    @Override
    public EventRecordDashboardResponse getDashboardByLga(Long lgaId, String eventId) throws NotFoundException {
        Lga lga = new Lga(){{setId(lgaId);}};
        List<EventRecord> eventList = getLgaEventsRecord(lgaId, eventId);
        Integer totalPUs = (int)pollingUnitService.countByLga(lga);
        if(eventList == null || eventList.size() == 0) {
            return new EventRecordDashboardResponse(0l, 0l, totalPUs);
        }

        return getEventRecordDashboardResponse(eventList, totalPUs);
    }

    @Override
    public EventRecordDashboardResponse getDashboardByWard(Long wardId, String eventId) throws NotFoundException {
        Ward ward = new Ward(){{setId(wardId);}};
        List<EventRecord> eventList = getWardEventsRecord(wardId, eventId);
        Integer totalPUs = (int)pollingUnitService.countByWard(ward);
        if(eventList == null || eventList.size() == 0) {
            return new EventRecordDashboardResponse(0l, 0l, totalPUs);
        }

        return getEventRecordDashboardResponse(eventList, totalPUs);
    }

    private EventRecordDashboardResponse getEventRecordDashboardResponse(List<EventRecord> eventList, Integer totalPUs) {
        try {
            AtomicLong positiveEvent = new AtomicLong(0l);
            AtomicLong negativeEvent = new AtomicLong(0l);

            eventList.forEach(eventRecord -> {
                if(eventRecord.getEventStatus()) {
                    positiveEvent.addAndGet(1);
                }else{
                    negativeEvent.addAndGet(1);
                }
            });

            long noEvent = totalPUs - eventList.size();

            return new EventRecordDashboardResponse(positiveEvent.get(), negativeEvent.get(), noEvent);

        }catch (Exception e){
            System.out.println("Exception: "+e.getStackTrace());
            return new EventRecordDashboardResponse(0l, 0l, 0l);
        }
    }

    public Integer getStateEventRecordsCount(State state) {
        try {
            return eventRecordService.findEventRecordByState(state.getId()).getEventRecords()
                    .stream().filter(eventRecord -> eventRecord.getEventStatus())
                    .collect(Collectors.toList()).size();
        }catch (NotFoundException e){
            return 0;
        }
    }


    private List<EventRecord> getStateEvents(State state ,String eventId) throws NotFoundException {
        if(eventId == null) {
            return eventRecordService.findEventRecordByState(state.getId()).getEventRecords();
        }
        else {
            return eventRecordService.findEventRecordByStateAndEventId(state.getId(), Long.valueOf(eventId)).getEventRecords();
        }
    }

    public List<EventRecord> getDistrictEvents(long senatorialDistrict, String eventId) throws NotFoundException {
        return eventId == null?
                eventRecordService.findEventRecordBySenatorial(senatorialDistrict).getEventRecords()
        : eventRecordService.findEventRecordBySenatorialAndEventId(senatorialDistrict, Long.valueOf(eventId)).getEventRecords();
    }

    private List<EventRecord> getWardEventsRecord(long ward, String eventId){
        return eventId == null ? eventRecordService.findEventRecordByWard(ward).getEventRecords()
                : eventRecordService.findEventRecordByWardAndEventId(ward, Long.valueOf(eventId)).getEventRecords();
    }

    public List<EventRecord> getLgaEventsRecord(long lga, String eventId){
        return eventId == null?eventRecordService.findEventRecordByLga(lga).getEventRecords()
                : eventRecordService.findEventRecordByLgaAndEventId(lga, Long.valueOf(eventId)).getEventRecords();
    }

    private State getState() {
        try {
            State state = stateService.getDefaultState().getState();
            return state;
        }catch (NotFoundException e){
            return new State();
        }
    }

}
