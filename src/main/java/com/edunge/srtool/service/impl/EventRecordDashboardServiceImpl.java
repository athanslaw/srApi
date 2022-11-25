package com.edunge.srtool.service.impl;

import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.EventRecord;
import com.edunge.srtool.model.Lga;
import com.edunge.srtool.model.SenatorialDistrict;
import com.edunge.srtool.model.State;
import com.edunge.srtool.response.IncidentDashboardResponse;
import com.edunge.srtool.response.IncidentReport;
import com.edunge.srtool.service.EventRecordDashboardService;
import com.edunge.srtool.service.EventRecordService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class EventRecordDashboardServiceImpl implements EventRecordDashboardService {
    private final EventRecordService eventRecordService;
    private final LgaServiceImpl lgaService;
    private final PollingUnitServiceImpl pollingUnitService;
    private final StateServiceImpl stateService;


    private final SenatorialDistrictServiceImpl senatorialDistrictService;
    public EventRecordDashboardServiceImpl(EventRecordService eventRecordService, PollingUnitServiceImpl pollingUnitService,
                                           SenatorialDistrictServiceImpl senatorialDistrictService, LgaServiceImpl lgaService, StateServiceImpl stateService) {
        this.eventRecordService = eventRecordService;
        this.pollingUnitService = pollingUnitService;
        this.senatorialDistrictService = senatorialDistrictService;
        this.lgaService = lgaService;
        this.stateService = stateService;
    }

    @Override
    public IncidentDashboardResponse getDashboardByState() throws NotFoundException {
        State state = this.getState();
        return getDashboardByState(state.getId());
    }

    @Override
    public IncidentDashboardResponse getDashboardByState(Long stateId) throws NotFoundException {

        State state = new State(){{setId(stateId);}};
        Integer totalEvents = getStateEventRecordsCount(state);
        if(totalEvents == 0) {
            new IncidentDashboardResponse("00","Events Report loaded.",totalEvents, null, null);
        }
        try {
            List<IncidentReport> eventReports = getEventReport(state);
            List<IncidentReport> lgaIncidentReport = getLgaReports(state);
        return new IncidentDashboardResponse("00","Events Report loaded.",totalEvents, eventReports, lgaIncidentReport);
        }catch (Exception e){
            System.out.println("Exception: "+e.getStackTrace());
            return new IncidentDashboardResponse("00","Incident Report loaded.",totalEvents, null, null);
        }
    }

    @Override
    public IncidentDashboardResponse getDashboardBySenatorialDistrict(Long id) throws NotFoundException {

        Integer totalEvents = getDistrictEventsCount(id);
        if(totalEvents == 0) {
            new IncidentDashboardResponse("00","Event Report loaded.",totalEvents, null, null);
        }
        try {
            List<IncidentReport> eventReports = getEventReportDistrict(id);
            List<IncidentReport> lgaEventReport = getLgaReportsByDistrict(id);
        return new IncidentDashboardResponse("00","Events Report loaded.",totalEvents, eventReports, lgaEventReport);
        }catch (Exception e){
            System.out.println("Exception: "+e.getStackTrace());
            return new IncidentDashboardResponse("00","Incident Report loaded.",totalEvents, null, null);
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

    private Integer getLgaEventRecordsCount(long lga) {
        return eventRecordService.findEventRecordByLga(lga).getEventRecords()
                .stream().filter(eventRecord -> eventRecord.getEventStatus())
                .collect(Collectors.toList()).size();
    }

    private Integer getDistrictEventsCount(long senatorialDistrict) throws NotFoundException {
        return (int)eventRecordService.findEventRecordBySenatorial(senatorialDistrict).getEventRecords().stream()
                .filter(eventRecord -> eventRecord.getEventStatus())
                .count();
    }

    private List<EventRecord> getStateEvents(State state) throws NotFoundException {
        return eventRecordService.findEventRecordByState(state.getId()).getEventRecords();
    }

    public List<EventRecord> getDistrictEvents(long senatorialDistrict) throws NotFoundException {
        return eventRecordService.findEventRecordBySenatorial(senatorialDistrict).getEventRecords();
    }

    public List<EventRecord> getLgaEventsRecord(long lga){
        return eventRecordService.findEventRecordByLga(lga).getEventRecords();
    }

    private State getState() {
        try {
            State state = stateService.getDefaultState().getState();
            return state;
        }catch (NotFoundException e){
            return new State();
        }
    }

    @Override
    public IncidentDashboardResponse getDashboardByLga(Long lgaId) throws NotFoundException {
        Integer totalEvents = getLgaEventRecordsCount(lgaId);
        if(totalEvents == 0) {
            new IncidentDashboardResponse("00","Events Report loaded.",totalEvents, null, null);
        }
        try {
            List<IncidentReport> eventReports = getEventReport(lgaId);
            List<IncidentReport> lgaIncidentReport = getLgaReports(lgaId);
            return new IncidentDashboardResponse("00","Events Report loaded.",totalEvents, eventReports, lgaIncidentReport);
        }catch (Exception e){
            System.out.println("Exception: "+e.getStackTrace());
            return new IncidentDashboardResponse("00","Events Report loaded.",totalEvents, null, null);
        }
    }

    private List<IncidentReport> getEventReport(State state) throws NotFoundException {
        List<EventRecord> eventList = getStateEvents(state).stream().filter(eventRecord -> eventRecord.getEventStatus()).collect(Collectors.toList());
        HashMap<String, Integer> eventMap = new HashMap<>();
        List<IncidentReport> eventReports = new ArrayList<>();

        eventList
                .stream().filter(eventRecord -> eventRecord.getEventStatus())
                .forEach(eventRecord -> {
                    String event = eventRecord.getDescription();
                    Integer currentValue = eventMap.getOrDefault(event, 0);
                    eventMap.put(event, currentValue+1);
                });
        Integer totalPUs = (int)pollingUnitService.countByState(state);
        if(eventList.size() < 1){
            return new ArrayList<>();
        }
        eventMap.forEach((event, count)->{
            Double percent = (count * 100.0)/totalPUs;
            eventReports.add(new IncidentReport(event, count, percent));
        });
        return eventReports;
    }

    private List<IncidentReport> getEventReport(long lgaId){
        List<EventRecord> eventList = getLgaEventsRecord(lgaId);
        eventList.stream().filter(eventRecord -> eventRecord.getEventStatus()).collect(Collectors.toList());
        HashMap<String, Integer> eventMap = new HashMap<>();
        List<IncidentReport> eventReports = new ArrayList<>();
        eventList
                .forEach(eventRecord -> {
                    String eventType = eventRecord.getDescription();
                    Integer currentValue = eventMap.getOrDefault(eventType, 0);
                    eventMap.put(eventType, currentValue+1);
                });
        Lga lga = new Lga(){{ setId(lgaId);}};
        Integer totalPUs = (int)pollingUnitService.countByLga(lga);
        if(eventList.size() < 1){
            return new ArrayList<IncidentReport>();
        }
        eventMap.forEach((event, count)->{
            Double percent = (count * 100.0)/totalPUs;
            eventReports.add(new IncidentReport(event, count, percent));
        });
        return eventReports;
    }

    private List<IncidentReport> getEventReportDistrict(long senatorialDistrict) throws NotFoundException {
        List<EventRecord> eventList = getDistrictEvents(senatorialDistrict).stream().filter(
                eventRecord -> eventRecord.getEventStatus()
        ).collect(Collectors.toList());

        HashMap<String, Integer> eventMap = new HashMap<>();
        List<IncidentReport> eventReports = new ArrayList<>();
        eventList
                .forEach(eventRecord -> {
                    String event = eventRecord.getDescription();
                    Integer currentValue = eventMap.getOrDefault(event, 0);
                    eventMap.put(event, currentValue+1);
                });
        Integer totalPUs = (int)pollingUnitService.countBySenatorialDistrict(new SenatorialDistrict(){{setId(senatorialDistrict);}});
        if(eventList.size() < 1){
            return new ArrayList<IncidentReport>();
        }
        eventMap.forEach((event, count)->{
            Double percent = (count * 100.0)/totalPUs;
            eventReports.add(new IncidentReport(event, count, percent));
        });
        return eventReports;
    }

    private List<IncidentReport> getLgaReports(long lga){
        List<IncidentReport> eventReports  = new ArrayList<>();
        List<EventRecord> eventList = getLgaEventsRecord(lga);
        int totalEvents = eventList.size();
        eventList.stream().filter(eventRecord -> eventRecord.getEventStatus()).collect(Collectors.toList());
        int weight = totalEvents - eventList.size();

        try {
            Lga lgaInfo = lgaService.findLgaById(lga).getLga();

            if (eventList.size() > 0) {
                HashMap<String, Integer> eventsMap = new HashMap<>();
                AtomicInteger totalEvent = new AtomicInteger(0);
                // no of PU with incidents
                Set<Long> distinctPUs = new LinkedHashSet<>();

                eventList.forEach(event -> {
                    String eventDescription = event.getDescription();
                    Integer currentValue = eventsMap.getOrDefault(eventDescription, 0);
                    eventsMap.put(eventDescription, currentValue + 1);
                });
                eventList.stream()
                        .filter(eventRecord -> eventRecord.getEventStatus())
                        .forEach(eventRecord -> {
                            distinctPUs.add(eventRecord.getPollingUnit());
                            totalEvent.addAndGet(1);
                        });

                int totalPUs = (int)pollingUnitService.countByLga(lgaInfo);
                if (totalEvent.get() > 0) {
                    eventsMap.forEach((event, count) -> {
                        Double percent = (count * 100.0) / totalPUs;
                        eventReports.add(new IncidentReport(lgaInfo, event, count, percent, totalEvent.get(), weight));
                    });
                }

            }
        }catch (NotFoundException e){}
        return eventReports;
    }

    private List<IncidentReport> getLgaReports(State state) throws NotFoundException{
        try {
            List<Lga> lgas = lgaService.findLgaByStateCode(state.getId()).getLgas();
            List<IncidentReport> eventReports = new ArrayList<>();
            List<EventRecord> eventList = getStateEvents(state);

            int totalEvents = eventList.size();
            eventList.stream().filter(eventRecord -> eventRecord.getEventStatus()).collect(Collectors.toList());
            int weight = totalEvents - eventList.size();

            lgas.forEach(lga -> {
                HashMap<String, Integer> eventTypeMap = new HashMap<>();
                AtomicInteger totalEvent = new AtomicInteger(0);
                // no of PU with incidents
                Set<Long> distinctPUs = new LinkedHashSet<>();

                eventList.stream()
                        .filter(eventRecord -> eventRecord.getLga().equals(lga.getId()))
                        .forEach(eventRecord -> {
                            String eventRecordDescription = eventRecord.getDescription();
                            Integer currentValue = eventTypeMap.getOrDefault(eventRecordDescription, 0);
                            eventTypeMap.put(eventRecordDescription, currentValue + 1);
                        });
                eventList.stream()
                        .filter(eventRecord -> eventRecord.getLga().equals(lga.getId()))
                        .filter(eventRecord -> eventRecord.getEventStatus())
                        .forEach(incident -> {
                            distinctPUs.add(incident.getPollingUnit());
                            totalEvent.addAndGet(1);
                        });

                if (totalEvent.get() > 0) {
                    eventTypeMap.forEach((type, count) -> {
                        Double percent = (count * 100.0) / distinctPUs.size();
                        eventReports.add(new IncidentReport(lga, type, count, percent, totalEvent.get(), weight));
                    });
                }
            });
            return eventReports;
        }catch (NotFoundException e){
            throw new NotFoundException("Not found");
        }
    }

    private List<IncidentReport> getLgaReportsByDistrict(long senatorialDistrictId) throws NotFoundException{
        List<Lga> lgas = lgaService.findLgaBySenatorialDistrictCode(senatorialDistrictId).getLgas();
        List<IncidentReport> eventReports  = new ArrayList<>();
        List<EventRecord> eventList = getDistrictEvents(senatorialDistrictId);
        int totalEvents = eventList.size();
        eventList.stream().filter(eventRecord -> eventRecord.getEventStatus()).collect(Collectors.toList());
        int weight = totalEvents - eventList.size();

        lgas.forEach(lga -> {
            HashMap<String, Integer> eventeMap = new HashMap<>();
            AtomicInteger totalEvent = new AtomicInteger(0);
            // no of PU with incidents
            Set<Long> distinctPUs = new LinkedHashSet<>();

            eventList.stream()
                    .filter(incident -> incident.getLga().equals(lga.getId()))
                    .forEach(incident -> {
                        String description = incident.getDescription();
                        Integer currentValue = eventeMap.getOrDefault(description, 0);
                        eventeMap.put(description, currentValue + 1);
                    });
            eventList.stream()
                    .filter(incident -> incident.getLga().equals(lga.getId()))
                    .filter(incident -> incident.getEventStatus())
                    .forEach(incident -> {
                        distinctPUs.add(incident.getPollingUnit());
                        totalEvent.addAndGet(1);
                    });
            if(totalEvent.get() > 0) {
                eventeMap.forEach((type, count) -> {
                    Double percent = (count * 100.0) / distinctPUs.size();
                    eventReports.add(new IncidentReport(lga, type, count, percent, totalEvent.get(), weight));
                });
            }

        });
        return eventReports;
    }

}
