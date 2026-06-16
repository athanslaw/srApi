package com.edunge.srtool.controller;

import com.edunge.srtool.dto.PoliticalPartyDto;
import com.edunge.srtool.response.PoliticalPartyResponse;
import com.edunge.srtool.service.PoliticalPartyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Manage Political Party", description = "Endpoints to manage Political Party")
@CrossOrigin(maxAge = 3600)
public class PoliticalPartyController {

    private final PoliticalPartyService politicalService;

    @Autowired
    public PoliticalPartyController(PoliticalPartyService politicalPartyService) {
        this.politicalService = politicalPartyService;
    }

    @GetMapping(value = "/political-party/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieve all Political Parties.")
    public ResponseEntity<PoliticalPartyResponse> findAllParties(){
        return new ResponseEntity<>(politicalService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/political-party", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find Political Party by code.")
    public ResponseEntity<PoliticalPartyResponse> findPoliticalPartyByCode(@RequestParam String code) throws Exception {
        return ResponseEntity.ok(politicalService.findPoliticalPartyByCodeAndDefaultState(code));
    }

    @GetMapping(value = "/political-party/state/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find Political Party by code.")
    public ResponseEntity<PoliticalPartyResponse> findPoliticalPartyByCode(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(politicalService.findPoliticalPartyByState(id));
    }

    @GetMapping(value = "/political-party/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find Political Party by id.")
    public ResponseEntity<PoliticalPartyResponse> findPoliticalPartyById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(politicalService.findPoliticalPartyById(id));
    }


    @RequestMapping(value = "/political-party", method = RequestMethod.POST)
    @Operation(summary = "Save Political Party to the DB")
    public ResponseEntity<PoliticalPartyResponse> storePoliticalParty(@RequestBody PoliticalPartyDto politicalParty) throws Exception {
        return ResponseEntity.ok(politicalService.savePoliticalParty(politicalParty));
    }

    @RequestMapping(value = "/political-party/{id}", method = RequestMethod.PUT)
    @Operation(summary = "Update Political Party to the DB")
    public ResponseEntity<PoliticalPartyResponse> updatePoliticalParty(@PathVariable Long id, @RequestBody PoliticalPartyDto politicalParty) throws Exception {
        return ResponseEntity.ok(politicalService.editPoliticalParty(id, politicalParty));
    }

    @RequestMapping(value = "/political-party/delete/{id}", method = RequestMethod.DELETE)
    @Operation(summary = "Delete Political Party by id.")
    public ResponseEntity<PoliticalPartyResponse> deletePoliticalPartyById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(politicalService.deletePoliticalPartyById(id));
    }

    @GetMapping(value = "/political-party/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Filter political party by name.")
    public ResponseEntity<PoliticalPartyResponse> filterPoliticalPartyByName(@RequestParam String name) throws Exception {
        return ResponseEntity.ok(politicalService.filterByName(name));
    }
}
