package com.edunge.srtool.controller;

import com.edunge.srtool.dto.PartyAgentDto;
import com.edunge.srtool.response.PartyAgentResponse;
import com.edunge.srtool.service.PartyAgentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@Api(value="Manage Party Agent", description="Endpoints to manage Party Agent")
@CrossOrigin(maxAge = 3600)
public class PartyAgentController {

    private final PartyAgentService partyAgentService;

    @Autowired
    public PartyAgentController(PartyAgentService partyAgentService) {
        this.partyAgentService = partyAgentService;
    }

    @GetMapping(value = "/party-agent/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve all Party Agents.")
    public ResponseEntity<PartyAgentResponse> findAllPartyAgents(){
        return new ResponseEntity<>(partyAgentService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/party-agent/phone/{phone}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find party agent by code.")
    public ResponseEntity<PartyAgentResponse> findPartyAgentByPhone(@PathVariable String phone) throws Exception {
        return ResponseEntity.ok(partyAgentService.findPartyAgentByPhone(phone));
    }

    @GetMapping(value = "/party-agent/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find party agent by id.")
    public ResponseEntity<PartyAgentResponse> findPartyAgentById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(partyAgentService.findPartyAgentById(id));
    }

    @RequestMapping(value = "/party-agent", method = RequestMethod.POST)
    @ApiOperation(value = "Save party agent to the DB")
    public ResponseEntity<PartyAgentResponse> storePartyAgent(@RequestBody PartyAgentDto partyAgentDto) throws Exception {
        return ResponseEntity.ok(partyAgentService.savePartyAgent(partyAgentDto));
    }

    @RequestMapping(value = "/party-agent/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update party agent to the DB")
    public ResponseEntity<PartyAgentResponse> updatePartyAgent(@PathVariable Long id, @RequestBody PartyAgentDto partyAgentDto) throws Exception {
        return ResponseEntity.ok(partyAgentService.updatePartyAgent(id,partyAgentDto));
    }

    @RequestMapping(value = "/party-agent/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete party agent by id.")
    public ResponseEntity<PartyAgentResponse> deletePartyAgentById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(partyAgentService.deletePartyAgentById(id));
    }

    @GetMapping(value = "/party-agent/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find party agent by name.")
    public ResponseEntity<PartyAgentResponse> filterPartyAgentByName(@RequestParam(required = false) String firstname, @RequestParam(required = false) String lastname) throws Exception {
        return ResponseEntity.ok(partyAgentService.findPartyAgentByName(firstname, lastname));
    }

    @GetMapping(value = "/party-agent/filter/state/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Filter by name.")
    public ResponseEntity<PartyAgentResponse> filterPartyAgentByState(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(partyAgentService.findPartyAgentByState(id));
    }

    @GetMapping(value = "/party-agent/filter/senatorial/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Filter by name.")
    public ResponseEntity<PartyAgentResponse> filterPartyAgentBySenatorialDistrict(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(partyAgentService.findPartyAgentBySenatorialDistrict(id));
    }

    @GetMapping(value = "/party-agent/filter/lga/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Filter by name.")
    public ResponseEntity<PartyAgentResponse> filterPartyAgentByLga(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(partyAgentService.findPartyAgentByLga(id));
    }

    @GetMapping(value = "/party-agent/filter/ward/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Filter by name.")
    public ResponseEntity<PartyAgentResponse> filterPartyAgentByWard(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(partyAgentService.findPartyAgentByWard(id));
    }

    @GetMapping(value = "/party-agent/filter/polling-unit/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Filter by name.")
    public ResponseEntity<PartyAgentResponse> filterPartyAgentByPollingUnit(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(partyAgentService.findPartyAgentByPollingUnit(id));
    }

    @PostMapping("/party-agent/upload")
    public ResponseEntity<PartyAgentResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(partyAgentService.uploadPartyAgent(file), HttpStatus.OK);
    }

}
