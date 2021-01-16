package com.edunge.srtool.controller;

import com.edunge.srtool.model.IncidentType;
import com.edunge.srtool.response.IncidentTypeResponse;
import com.edunge.srtool.service.IncidentTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Api(value="Manage Incident Type", description="Endpoints to manage Incident Type")
@CrossOrigin(maxAge = 3600, allowedHeaders = "*")
public class IncidentTypeController {

    private final IncidentTypeService incidentTypeService;

    @Autowired
    public IncidentTypeController(IncidentTypeService incidentTypeService) {
        this.incidentTypeService = incidentTypeService;
    }

    @GetMapping(value = "/incident-type/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve all Incident Typ.")
    public ResponseEntity<IncidentTypeResponse> findIncidentTypes(){
        return new ResponseEntity<>(incidentTypeService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/incident-type", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find Incident Type by code.")
    public ResponseEntity<IncidentTypeResponse> findIncidentTypeByCode(@RequestParam String code) throws Exception {
        return ResponseEntity.ok(incidentTypeService.findIncidentTypeByCode(code));
    }

    @GetMapping(value = "/incident-type/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find Incident Typ by id.")
    public ResponseEntity<IncidentTypeResponse> findIncidentTypeById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(incidentTypeService.findIncidentTypeById(id));
    }


    @RequestMapping(value = "/incident-type", method = RequestMethod.POST)
    @ApiOperation(value = "Save Incident Typ to the DB")
    public ResponseEntity<IncidentTypeResponse> storeIncidentType(@RequestBody IncidentType IncidentType) throws Exception {
        return ResponseEntity.ok(incidentTypeService.saveIncidentType(IncidentType));
    }

    @RequestMapping(value = "/incident-type/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update Incident Typ to the DB")
    public ResponseEntity<IncidentTypeResponse> updateIncidentType(@PathVariable Long id, @RequestBody IncidentType IncidentType) throws Exception {
        return ResponseEntity.ok(incidentTypeService.editIncidentType(id, IncidentType));
    }

    @RequestMapping(value = "/incident-type/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete Incident Typ by id.")
    public ResponseEntity<IncidentTypeResponse> deleteIncidentType(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(incidentTypeService.deleteIncidentTypeById(id));
    }

    @GetMapping(value = "/incident-type/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Filter Incident type by name.")
    public ResponseEntity<IncidentTypeResponse> filterIncidentTypeByName(@RequestParam String name) throws Exception {
        return ResponseEntity.ok(incidentTypeService.filterByName(name));
    }
}
