package com.edunge.srtool.controller;

import com.edunge.srtool.dto.ElectionDto;
import com.edunge.srtool.response.ElectionResponse;
import com.edunge.srtool.service.ElectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Api(value="Manage Election", description="Endpoints to manage Election")
@CrossOrigin(maxAge = 3600, allowedHeaders = "*")
public class ElectionController {

    private final ElectionService electionService;

    @Autowired
    public ElectionController(ElectionService electionService) {
        this.electionService = electionService;
    }

    @GetMapping(value = "/election/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve all election.")
    public ResponseEntity<ElectionResponse> findAllElections(){
        return new ResponseEntity<>(electionService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/election", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find election by code.")
    public ResponseEntity<ElectionResponse> findElectionByCode(@RequestParam String code) throws Exception {
        return ResponseEntity.ok(electionService.findElectionByCode(code));
    }

    @GetMapping(value = "/election/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find election by id.")
    public ResponseEntity<ElectionResponse> findElectionById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(electionService.findElectionById(id));
    }


    @RequestMapping(value = "/election", method = RequestMethod.POST)
    @ApiOperation(value = "Save election to the DB")
    public ResponseEntity<ElectionResponse> storeElection(@RequestBody ElectionDto electionDto) throws Exception {
        return ResponseEntity.ok(electionService.saveElection(electionDto));
    }

    @RequestMapping(value = "/election/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update election to the DB")
    public ResponseEntity<ElectionResponse> updateElection(@PathVariable Long id, @RequestBody ElectionDto electionDto) throws Exception {
        return ResponseEntity.ok(electionService.updateElection(id,electionDto));
    }

    @RequestMapping(value = "/election/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete election by id.")
    public ResponseEntity<ElectionResponse> deleteElectionById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(electionService.deleteElectionById(id));
    }

    @GetMapping(value = "/election/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Filter election by name.")
    public ResponseEntity<ElectionResponse> filterElectionByCode(@RequestParam String name) throws Exception {
        return ResponseEntity.ok(electionService.filterByName(name));
    }
}
