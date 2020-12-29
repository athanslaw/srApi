package com.edunge.srtool.controller;

import com.edunge.srtool.model.IncidentLevel;
import com.edunge.srtool.response.IncidentLevelResponse;
import com.edunge.srtool.service.IncidentLevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Api(value="Manage Incident Level", description="Endpoints to manage Incident Level")
public class IncidentLevelController {

    private final IncidentLevelService incidentLevelService;

    @Autowired
    public IncidentLevelController(IncidentLevelService incidentLevelService) {
        this.incidentLevelService = incidentLevelService;
    }

    @GetMapping(value = "/incident-level/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve all Incident Level.")
    public ResponseEntity<IncidentLevelResponse> findIncidentLevels(){
        return new ResponseEntity<>(incidentLevelService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/incident-level", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find Incident Level by code.")
    public ResponseEntity<IncidentLevelResponse> findIncidentLevelByCode(@RequestParam String code) throws Exception {
        return ResponseEntity.ok(incidentLevelService.findIncidentLevelByCode(code));
    }

    @GetMapping(value = "/incident-level/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find Incident Level by id.")
    public ResponseEntity<IncidentLevelResponse> findIncidentLevelById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(incidentLevelService.findIncidentLevelById(id));
    }


    @RequestMapping(value = "/incident-level", method = RequestMethod.POST)
    @ApiOperation(value = "Save Incident Level to the DB")
    public ResponseEntity<IncidentLevelResponse> storeIncidentLevel(@RequestBody IncidentLevel IncidentLevel) throws Exception {
        return ResponseEntity.ok(incidentLevelService.saveIncidentLevel(IncidentLevel));
    }

    @RequestMapping(value = "/incident-level/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update Incident Level to the DB")
    public ResponseEntity<IncidentLevelResponse> updateIncidentLevel(@PathVariable Long id, @RequestBody IncidentLevel IncidentLevel) throws Exception {
        return ResponseEntity.ok(incidentLevelService.editIncidentLevel(id, IncidentLevel));
    }

    @RequestMapping(value = "/incident-level/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete Incident Level by id.")
    public ResponseEntity<IncidentLevelResponse> deleteIncidentLevel(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(incidentLevelService.deleteIncidentLevelById(id));
    }


    @GetMapping(value = "/incident-level/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Filter Incident leve by name.")
    public ResponseEntity<IncidentLevelResponse> filterIncidentLeveleByName(@RequestParam String name) throws Exception {
        return ResponseEntity.ok(incidentLevelService.filterByName(name));
    }
}
