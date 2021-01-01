package com.edunge.srtool.controller;

import com.edunge.srtool.dto.IncidentDto;
import com.edunge.srtool.response.IncidentResponse;
import com.edunge.srtool.service.IncidentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Api(value="Manage Incident", description="Endpoints to manage Incident")
@CrossOrigin(maxAge = 3600)
public class IncidentController {

    private final IncidentService incidentService;

    @Autowired
    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping(value = "/incident/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve all incidents.")
    public ResponseEntity<IncidentResponse> findAllIncidents(){
        return new ResponseEntity<>(incidentService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/incident", method = RequestMethod.POST)
    @ApiOperation(value = "Save incident to the DB")
    public ResponseEntity<IncidentResponse> storeIncident(@RequestBody IncidentDto incidentDto) throws Exception {
        return ResponseEntity.ok(incidentService.saveIncident(incidentDto));
    }

    @RequestMapping(value = "/incident/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update incident to the DB")
    public ResponseEntity<IncidentResponse> updateIncident(@PathVariable Long id, @RequestBody IncidentDto incidentDto) throws Exception {
        return ResponseEntity.ok(incidentService.updateIncident(id,incidentDto));
    }

    @RequestMapping(value = "/incident/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete incident by id.")
    public ResponseEntity<IncidentResponse> deleteIncidentById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(incidentService.deleteIncidentById(id));
    }

    @GetMapping(value = "/incident/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Filter incident by name.")
    public ResponseEntity<IncidentResponse> filterIncidentByCode(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(incidentService.findIncidentById(id));
    }
}
