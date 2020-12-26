package com.edunge.srtool.controller;

import com.edunge.srtool.model.PoliticalParty;
import com.edunge.srtool.response.PoliticalPartyResponse;
import com.edunge.srtool.service.PoliticalPartyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Api(value="Manage Political Party", description="Endpoints to manage Political Party")
public class PoliticalPartyController {

    private final PoliticalPartyService politicalService;

    @Autowired
    public PoliticalPartyController(PoliticalPartyService politicalPartyService) {
        this.politicalService = politicalPartyService;
    }

    @GetMapping(value = "/political-party/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve all Political Parties.")
    public ResponseEntity<PoliticalPartyResponse> findAllParties(){
        return new ResponseEntity<>(politicalService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/political-party", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find Political Party by code.")
    public ResponseEntity<PoliticalPartyResponse> findPoliticalPartyByCode(@RequestParam String code) throws Exception {
        return ResponseEntity.ok(politicalService.findPoliticalPartyByCode(code));
    }

    @GetMapping(value = "/political-party/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find Political Party by id.")
    public ResponseEntity<PoliticalPartyResponse> findPoliticalPartyById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(politicalService.findPoliticalPartyById(id));
    }


    @RequestMapping(value = "/political-party", method = RequestMethod.POST)
    @ApiOperation(value = "Save Political Party to the DB")
    public ResponseEntity<PoliticalPartyResponse> storePoliticalParty(@RequestBody PoliticalParty politicalParty) throws Exception {
        return ResponseEntity.ok(politicalService.savePoliticalParty(politicalParty));
    }

    @RequestMapping(value = "/political-party/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update Political Party to the DB")
    public ResponseEntity<PoliticalPartyResponse> updatePoliticalParty(@PathVariable Long id, @RequestBody PoliticalParty politicalParty) throws Exception {
        return ResponseEntity.ok(politicalService.editPoliticalParty(id, politicalParty));
    }

    @RequestMapping(value = "/political-party/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete Political Party by id.")
    public ResponseEntity<PoliticalPartyResponse> deletePoliticalPartyById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(politicalService.deletePoliticalPartyById(id));
    }

    @GetMapping(value = "/political-party/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Filter political party by name.")
    public ResponseEntity<PoliticalPartyResponse> filterPoliticalPartyByName(@RequestParam String name) throws Exception {
        return ResponseEntity.ok(politicalService.filterByName(name));
    }
}
