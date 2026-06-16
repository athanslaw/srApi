package com.edunge.srtool.controller;

import com.edunge.srtool.dto.IncidentDto;
import com.edunge.srtool.response.IncidentResponse;
import com.edunge.srtool.service.IncidentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Manage Incident", description = "Endpoints to manage Incident")
@CrossOrigin(maxAge = 3600)
public class IncidentController {

    private final IncidentService incidentService;

    @Autowired
    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping(value = "/incident/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieve all incidents.")
    public ResponseEntity<IncidentResponse> findAllIncidents(@RequestParam(required = false) String incidentType, @RequestParam(required = false) String incidentWeight){
        return new ResponseEntity<>(incidentService.findAll(incidentType, incidentWeight), HttpStatus.OK);
    }

    @RequestMapping(value = "/incident", method = RequestMethod.POST)
    @Operation(summary = "Save incident to the DB")
    public ResponseEntity<IncidentResponse> storeIncident(@RequestBody IncidentDto incidentDto) throws Exception {
        return ResponseEntity.ok(incidentService.saveIncident(incidentDto));
    }

    @RequestMapping(value = "/incident/{id}", method = RequestMethod.PUT)
    @Operation(summary = "Update incident to the DB")
    public ResponseEntity<IncidentResponse> updateIncident(@PathVariable Long id, @RequestBody IncidentDto incidentDto) throws Exception {
        return ResponseEntity.ok(incidentService.updateIncident(id,incidentDto));
    }

    @RequestMapping(value = "/incident/delete/{id}", method = RequestMethod.DELETE)
    @Operation(summary = "Delete incident by id.")
    public ResponseEntity<IncidentResponse> deleteIncidentById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(incidentService.deleteIncidentById(id));
    }

    @GetMapping(value = "/incident/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Filter incident by name.")
    public ResponseEntity<IncidentResponse> filterIncidentByCode(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(incidentService.findIncidentById(id));
    }

    @GetMapping(value = "/incident/zone/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Filter incident by zone.")
    public ResponseEntity<IncidentResponse> filterIncidentByZone(@PathVariable Long id, @RequestParam(required = false) String incidentType, @RequestParam(required = false) String incidentWeight) throws Exception {
        return ResponseEntity.ok(incidentService.findIncidentByZone(id, incidentType, incidentWeight));
    }
    @GetMapping(value = "/incident/state/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Filter incident by state.")
    public ResponseEntity<IncidentResponse> filterIncidentByState(@PathVariable Long id, @RequestParam(required = false) String incidentType, @RequestParam(required = false) String incidentWeight) throws Exception {
        return ResponseEntity.ok(incidentService.findIncidentByStateId(id, incidentType, incidentWeight));
    }
    @GetMapping(value = "/incident/senatorial/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Filter incident by Senatorial.")
    public ResponseEntity<IncidentResponse> filterIncidentBySenatorial(@PathVariable Long id, @RequestParam(required = false) String incidentType, @RequestParam(required = false) String incidentWeight) throws Exception {
        return ResponseEntity.ok(incidentService.findIncidentBySenatorial(id, incidentType, incidentWeight));
    }

    @GetMapping(value = "/incident/lga/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Filter incident by Lga.")
    public ResponseEntity<IncidentResponse> filterIncidentByLga(@PathVariable Long id, @RequestParam(required = false) String incidentType, @RequestParam(required = false) String incidentWeight) {
        return ResponseEntity.ok(incidentService.findIncidentByLga(id, incidentType, incidentWeight));
    }

    @GetMapping(value = "/incident/ward/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Filter incident by Ward.")
    public ResponseEntity<IncidentResponse> filterIncidentByWard(@PathVariable Long id, @RequestParam(required = false) String incidentType, @RequestParam(required = false) String incidentWeight) throws Exception {
        return ResponseEntity.ok(incidentService.findIncidentByWard(id, incidentType, incidentWeight));
    }

    @GetMapping(value = "/incident/polling-unit/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Filter incident by Polling Unit.")
    public ResponseEntity<IncidentResponse> filterIncidentByPollingUnit(@PathVariable Long id, @RequestParam(required = false) String incidentType, @RequestParam(required = false) String incidentWeight) throws Exception {
        return ResponseEntity.ok(incidentService.findIncidentByPollingUnit(id, incidentType, incidentWeight));
    }

    @PostMapping("/incident/upload")
    public ResponseEntity<IncidentResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(incidentService.uploadIncident(file), HttpStatus.OK);
    }
}
