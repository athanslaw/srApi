package com.edunge.srtool.controller;

import com.edunge.srtool.model.IncidentStatus;
import com.edunge.srtool.response.IncidentStatusResponse;
import com.edunge.srtool.service.IncidentStatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Api(value="Manage Incident Status", description="Endpoints to manage Incident Status")
@CrossOrigin(maxAge = 3600)
public class IncidentStatusController {

    private final IncidentStatusService incidentStatusService;

    @Autowired
    public IncidentStatusController(IncidentStatusService incidentStatusService) {
        this.incidentStatusService = incidentStatusService;
    }

    @GetMapping(value = "/incident-status/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve all Incident Status.")
    public ResponseEntity<IncidentStatusResponse> findIncidentStatuss(){
        return new ResponseEntity<>(incidentStatusService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/incident-status", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find Incident Status by code.")
    public ResponseEntity<IncidentStatusResponse> findIncidentStatusByCode(@RequestParam String code) throws Exception {
        return ResponseEntity.ok(incidentStatusService.findIncidentStatusByCode(code));
    }

    @GetMapping(value = "/incident-status/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find Incident Status by id.")
    public ResponseEntity<IncidentStatusResponse> findIncidentStatusById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(incidentStatusService.findIncidentStatusById(id));
    }


    @RequestMapping(value = "/incident-status", method = RequestMethod.POST)
    @ApiOperation(value = "Save Incident Status to the DB")
    public ResponseEntity<IncidentStatusResponse> storeIncidentStatus(@RequestBody IncidentStatus IncidentStatus) throws Exception {
        return ResponseEntity.ok(incidentStatusService.saveIncidentStatus(IncidentStatus));
    }

    @RequestMapping(value = "/incident-status/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update Incident Status to the DB")
    public ResponseEntity<IncidentStatusResponse> updateIncidentStatus(@PathVariable Long id, @RequestBody IncidentStatus IncidentStatus) throws Exception {
        return ResponseEntity.ok(incidentStatusService.editIncidentStatus(id, IncidentStatus));
    }

    @RequestMapping(value = "/incident-status/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete Incident Status by id.")
    public ResponseEntity<IncidentStatusResponse> deleteIncidentStatus(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(incidentStatusService.deleteIncidentStatusById(id));
    }

    @GetMapping(value = "/incident-status/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Filter Incident status by name.")
    public ResponseEntity<IncidentStatusResponse> filterIncidentStatuseByName(@RequestParam String name) throws Exception {
        return ResponseEntity.ok(incidentStatusService.filterByName(name));
    }
}
