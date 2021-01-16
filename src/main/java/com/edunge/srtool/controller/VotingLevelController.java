package com.edunge.srtool.controller;

import com.edunge.srtool.model.VotingLevel;
import com.edunge.srtool.response.VotingLevelResponse;
import com.edunge.srtool.service.VotingLevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Api(value="Manage Voting Level", description="Endpoints to manage Voting Level")
@CrossOrigin( maxAge = 3600)
public class VotingLevelController {

    private VotingLevelService votingLevelService;

    @Autowired
    public VotingLevelController(VotingLevelService votingLevelService) {
        this.votingLevelService = votingLevelService;
    }

    @GetMapping(value = "/voting-level/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve all Voting Level.")
    public ResponseEntity<VotingLevelResponse> findVotingLevels(){
        return new ResponseEntity<>(votingLevelService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/voting-level", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find Voting Level by code.")
    public ResponseEntity<VotingLevelResponse> findVotingLevelByCode(@RequestParam String code) throws Exception {
        return ResponseEntity.ok(votingLevelService.findVotingLevelByCode(code));
    }

    @GetMapping(value = "/voting-level/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find Voting Level by id.")
    public ResponseEntity<VotingLevelResponse> findVotingLevelById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(votingLevelService.findVotingLevelById(id));
    }


    @RequestMapping(value = "/voting-level", method = RequestMethod.POST)
    @ApiOperation(value = "Save Voting Level to the DB")
    public ResponseEntity<VotingLevelResponse> storeVotingLevel(@RequestBody VotingLevel votingLevel) throws Exception {
        return ResponseEntity.ok(votingLevelService.saveVotingLevel(votingLevel));
    }

    @RequestMapping(value = "/voting-level/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update Voting Level to the DB")
    public ResponseEntity<VotingLevelResponse> updateVotingLevel(@PathVariable Long id, @RequestBody VotingLevel votingLevel) throws Exception {
        return ResponseEntity.ok(votingLevelService.editVotingLevel(id, votingLevel));
    }

    @RequestMapping(value = "/voting-level/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete Voting Level by id.")
    public ResponseEntity<VotingLevelResponse> deleteVotingLevel(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(votingLevelService.deleteVotingLevelById(id));
    }
}
