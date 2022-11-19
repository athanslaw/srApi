package com.edunge.srtool.controller;

import com.edunge.srtool.dto.EventDto;
import com.edunge.srtool.dto.EventRecordDto;
import com.edunge.srtool.response.EventRecordResponse;
import com.edunge.srtool.response.EventResponse;
import com.edunge.srtool.service.EventRecordService;
import com.edunge.srtool.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Api(value="Manage Events")
@CrossOrigin(maxAge = 3600)
public class EventsController {

    @Autowired
    private EventRecordService eventRecordService;
    @Autowired
    private EventService eventService;

    @GetMapping(value = "/event-record", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve all events.")
    public ResponseEntity<EventRecordResponse> findIncidentGroups(){
        return new ResponseEntity<>(eventRecordService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/event-record/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find Event Record by id.")
    public ResponseEntity<EventRecordResponse> findEventRecordById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(eventRecordService.findEventRecordById(id));
    }

    @GetMapping(value = "/event-record/event-id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find Event Record by id.")
    public ResponseEntity<EventRecordResponse> findEventRecordByEventId(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(eventRecordService.findEventRecordByEventId(id));
    }

    @GetMapping(value = "/event-record/state-events", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find Event Record by state.")
    public ResponseEntity<EventRecordResponse> findEventRecordByStateAndEventId(@RequestParam("state") String state, @RequestParam(value = "eventId", required = false) String eventId) throws Exception {
        try{
            Long stateId = Long.valueOf(state);
            if(eventId == null) return ResponseEntity.ok(eventRecordService.findEventRecordByState(stateId));
            Long id = Long.valueOf(eventId);
            return ResponseEntity.ok(eventRecordService.findEventRecordByStateAndEventId(stateId, id));
        }catch (Exception e){
            return ResponseEntity.ok(new EventRecordResponse("04","No records found"));
        }
    }

    @GetMapping(value = "/event-record/senatorial-events", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find Event Record by district.")
    public ResponseEntity<EventRecordResponse> findEventRecordBySenetorialAndEventId(@RequestParam("senatorial") String senatorial, @RequestParam(value = "eventId", required = false) String eventId) throws Exception {
        try{
            Long senatorialId = Long.valueOf(senatorial);
            if(eventId == null) return ResponseEntity.ok(eventRecordService.findEventRecordBySenatorial(senatorialId));
            Long id = Long.valueOf(eventId);
            return ResponseEntity.ok(eventRecordService.findEventRecordBySenatorialAndEventId(senatorialId, id));
        }catch (Exception e){
            return ResponseEntity.ok(new EventRecordResponse("04","No records found"));
        }
    }

    @GetMapping(value = "/event-record/zone-events", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find Event Record by zone.")
    public ResponseEntity<EventRecordResponse> findEventRecordByZoneAndEventId(@RequestParam("zone") String zone, @RequestParam(value = "eventId", required = false) String eventId) throws Exception {
        try{
            Long zoneId = Long.valueOf(zone);
            if(eventId == null) return ResponseEntity.ok(eventRecordService.findEventRecordByZone(zoneId));
            Long id = Long.valueOf(eventId);
            return ResponseEntity.ok(eventRecordService.findEventRecordByZoneAndEventId(zoneId, id));
        }catch (Exception e){
            return ResponseEntity.ok(new EventRecordResponse("04","No records found"));
        }
    }

    @GetMapping(value = "/event-record/lga-events", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find Event Record by lga.")
    public ResponseEntity<EventRecordResponse> findEventRecordByLgaAndEventId(@RequestParam("lga") String lga, @RequestParam(value = "eventId", required = false) String eventId) throws Exception {
        try{
            Long lgaId = Long.valueOf(lga);
            if(eventId == null) return ResponseEntity.ok(eventRecordService.findEventRecordByLga(lgaId));
            Long id = Long.valueOf(eventId);
            return ResponseEntity.ok(eventRecordService.findEventRecordByLgaAndEventId(lgaId, id));
        }catch (Exception e){
            return ResponseEntity.ok(new EventRecordResponse("04","No records found"));
        }
    }


    @GetMapping(value = "/event-record/ward-events", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find Event Record by ward.")
    public ResponseEntity<EventRecordResponse> findEventRecordByWardAndEventId(@RequestParam("ward") String ward, @RequestParam(value = "eventId", required = false) String eventId) throws Exception {
        try{
            Long wardId = Long.valueOf(ward);
            if(eventId == null) return ResponseEntity.ok(eventRecordService.findEventRecordByWard(wardId));
            Long id = Long.valueOf(eventId);
            return ResponseEntity.ok(eventRecordService.findEventRecordByWardAndEventId(wardId, id));
        }catch (Exception e){
            return ResponseEntity.ok(new EventRecordResponse("04","No records found"));
        }
    }

    @GetMapping(value = "/event-record/pu-events", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find Event Record by Polling Unit.")
    public ResponseEntity<EventRecordResponse> findEventRecordByPUAndEventId(@RequestParam("pollingUnit") String pollingUnit, @RequestParam(value = "eventId", required = false) String eventId) {
        try{
            Long pollingUnitId = Long.valueOf(pollingUnit);
            if(eventId == null) return ResponseEntity.ok(eventRecordService.findEventRecordByPollingUnit(pollingUnitId));
            Long id = Long.valueOf(eventId);
            return ResponseEntity.ok(eventRecordService.findEventRecordByPollingUnitAndEventId(pollingUnitId, id));
        }catch (Exception e){
            return ResponseEntity.ok(new EventRecordResponse("04","No records found"));
        }
    }


    @PostMapping(value = "/event-record")
    @ApiOperation(value = "Save event record to the DB")
    public ResponseEntity<EventRecordResponse> storeEventRecord(@RequestBody EventRecordDto eventRecordDto) {
        return ResponseEntity.ok(eventRecordService.saveEventRecord(eventRecordDto));
    }

    @PutMapping("/event-record/{id}")
    @ApiOperation(value = "Update Event Record to the DB")
    public ResponseEntity<EventRecordResponse> updateEventRecord(@PathVariable Long id, @RequestBody EventRecordDto eventRecordDto) throws Exception {
        return ResponseEntity.ok(eventRecordService.updateEventRecord(id, eventRecordDto));
    }

    @DeleteMapping(value = "/event-record/delete/{id}")
    @ApiOperation(value = "Delete Event Record by id.")
    public ResponseEntity<EventRecordResponse> deleteEventRecord(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(eventRecordService.deleteEventRecordById(id));
    }




    @GetMapping(value = "/event", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve all events.")
    public ResponseEntity<EventResponse> findEvent(){
        return new ResponseEntity<>(eventService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/event/active", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve active events.")
    public ResponseEntity<EventResponse> findActiveEvents(){
        return new ResponseEntity<>(eventService.findEventByStatus(true), HttpStatus.OK);
    }

    @GetMapping(value = "/event/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find Event by id.")
    public ResponseEntity<EventResponse> findEventById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(eventService.findEventById(id));
    }


    @PostMapping("/event")
    @ApiOperation(value = "Save event to the DB")
    public ResponseEntity<EventResponse> storeEvent(@RequestBody EventDto eventDto) throws Exception {
        return ResponseEntity.ok(eventService.saveEvent(eventDto));
    }

    @PutMapping("/event/{id}")
    @ApiOperation(value = "Update Event")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable Long id, @RequestBody EventDto eventDto) throws Exception {
        return ResponseEntity.ok(eventService.updateEvent(id, eventDto));
    }

    @PutMapping("/event/activate/{id}")
    @ApiOperation(value = "Activate Event")
    public ResponseEntity<EventResponse> activateEvent(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(eventService.activateEvent(id));
    }

    @PutMapping("/event/deactivate/{id}")
    @ApiOperation(value = "Deactivate Event")
    public ResponseEntity<EventResponse> deactivateEvent(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(eventService.deactivateEvent(id));
    }

    @DeleteMapping(value = "/event/delete/{id}")
    @ApiOperation(value = "Delete Event by id.")
    public ResponseEntity<EventResponse> deleteEvent(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(eventService.deleteEventById(id));
    }

}
