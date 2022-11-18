package com.edunge.srtool.controller;

import com.edunge.srtool.dto.IncidentGroupDto;
import com.edunge.srtool.model.IncidentGroup;
import com.edunge.srtool.model.IncidentType;
import com.edunge.srtool.response.IncidentGroupResponse;
import com.edunge.srtool.response.IncidentTypeResponse;
import com.edunge.srtool.service.IncidentGroupService;
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
@Api(value="Manage Incident Group", description="Endpoints to manage Incident Group")
@CrossOrigin(maxAge = 3600)
public class IncidentGroupController {

    @Autowired
    private IncidentGroupService incidentGroupService;

    @GetMapping(value = "/incident-group", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve all Incident Groups.")
    public ResponseEntity<IncidentGroupResponse> findIncidentGroups(){
        return new ResponseEntity<>(incidentGroupService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/incident-group/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find Incident Type by id.")
    public ResponseEntity<IncidentGroupResponse> findIncidentGroupById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(incidentGroupService.findIncidentGroupById(id));
    }


    @RequestMapping(value = "/incident-group", method = RequestMethod.POST)
    @ApiOperation(value = "Save Incident group to the DB")
    public ResponseEntity<IncidentGroupResponse> storeIncidentType(@RequestBody IncidentGroupDto incidentGroupDto) throws Exception {
        return ResponseEntity.ok(incidentGroupService.saveIncidentGroup(incidentGroupDto));
    }

    @RequestMapping(value = "/incident-group/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update Incident Group to the DB")
    public ResponseEntity<IncidentGroupResponse> updateIncidentGroup(@PathVariable Long id, @RequestBody IncidentGroupDto incidentGroupDto) throws Exception {
        return ResponseEntity.ok(incidentGroupService.updateIncidentGroup(id, incidentGroupDto));
    }

    @PutMapping(value = "/incident-group/activate/{id}")
    @ApiOperation(value = "Activate Incident Group")
    public ResponseEntity<IncidentGroupResponse> activateIncidentGroup(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(incidentGroupService.activateIncidentGroup(id));
    }

    @RequestMapping(value = "/incident-group/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete Incident Group by id.")
    public ResponseEntity<IncidentGroupResponse> deleteIncidentType(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(incidentGroupService.deleteIncidentGroupById(id));
    }

}
