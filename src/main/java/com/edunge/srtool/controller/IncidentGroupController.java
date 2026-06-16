package com.edunge.srtool.controller;

import com.edunge.srtool.dto.IncidentGroupDto;
import com.edunge.srtool.response.IncidentGroupResponse;
import com.edunge.srtool.service.IncidentGroupService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Manage Incident Group", description = "Endpoints to manage Incident Group")
@CrossOrigin(maxAge = 3600)
public class IncidentGroupController {

    @Autowired
    private IncidentGroupService incidentGroupService;

    @GetMapping(value = "/incident-group", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieve all Incident Groups.")
    public ResponseEntity<IncidentGroupResponse> findIncidentGroups(){
        return new ResponseEntity<>(incidentGroupService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/incident-group/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find Incident Type by id.")
    public ResponseEntity<IncidentGroupResponse> findIncidentGroupById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(incidentGroupService.findIncidentGroupById(id));
    }


    @RequestMapping(value = "/incident-group", method = RequestMethod.POST)
    @Operation(summary = "Save Incident group to the DB")
    public ResponseEntity<IncidentGroupResponse> storeIncidentType(@RequestBody IncidentGroupDto incidentGroupDto) throws Exception {
        return ResponseEntity.ok(incidentGroupService.saveIncidentGroup(incidentGroupDto));
    }

    @RequestMapping(value = "/incident-group/{id}", method = RequestMethod.PUT)
    @Operation(summary = "Update Incident Group to the DB")
    public ResponseEntity<IncidentGroupResponse> updateIncidentGroup(@PathVariable Long id, @RequestBody IncidentGroupDto incidentGroupDto) throws Exception {
        return ResponseEntity.ok(incidentGroupService.updateIncidentGroup(id, incidentGroupDto));
    }

    @PutMapping(value = "/incident-group/activate/{id}")
    @Operation(summary = "Activate Incident Group")
    public ResponseEntity<IncidentGroupResponse> activateIncidentGroup(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(incidentGroupService.activateIncidentGroup(id));
    }

    @RequestMapping(value = "/incident-group/delete/{id}", method = RequestMethod.DELETE)
    @Operation(summary = "Delete Incident Group by id.")
    public ResponseEntity<IncidentGroupResponse> deleteIncidentType(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(incidentGroupService.deleteIncidentGroupById(id));
    }

}
