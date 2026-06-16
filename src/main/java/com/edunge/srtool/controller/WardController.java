package com.edunge.srtool.controller;

import com.edunge.srtool.dto.WardDto;
import com.edunge.srtool.response.WardResponse;
import com.edunge.srtool.service.PollingUnitService;
import com.edunge.srtool.service.WardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Manage Ward", description = "Endpoints to manage Ward")
@CrossOrigin(maxAge = 3600)
public class WardController {

    private final WardService wardService;
    private final PollingUnitService pollingUnitService;

    @Autowired
    public WardController(WardService wardService, PollingUnitService pollingUnitService) {
        this.wardService = wardService;
        this.pollingUnitService = pollingUnitService;
    }

    @GetMapping(value = "/wards", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieve all states.")
    public ResponseEntity<WardResponse> findAllWards(){
        return new ResponseEntity<>(wardService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/ward", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find ward by code.")
    public ResponseEntity<WardResponse> findWardByCode(@RequestParam String code) throws Exception {
        return ResponseEntity.ok(wardService.findWardByCode(code));
    }

    @GetMapping(value = "/ward/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find ward by id.")
    public ResponseEntity<WardResponse> findWardById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(wardService.findWardById(id));
    }


    @RequestMapping(value = "/ward", method = RequestMethod.POST)
    @Operation(summary = "Save ward to the DB")
    public ResponseEntity<WardResponse> storeWard(@RequestBody WardDto wardDto) throws Exception {
        return ResponseEntity.ok(wardService.saveWard(wardDto));
    }

    @RequestMapping(value = "/ward/{id}", method = RequestMethod.PUT)
    @Operation(summary = "Update ward to the DB")
    public ResponseEntity<WardResponse> updateWard(@PathVariable Long id, @RequestBody WardDto wardDto) throws Exception {
        WardResponse wardResponse = wardService.updateWard(id,wardDto);
        pollingUnitService.updatePollingUnitWard(id, wardResponse.getWard());
        return ResponseEntity.ok(wardResponse);
    }

    @RequestMapping(value = "/ward/delete/{id}", method = RequestMethod.DELETE)
    @Operation(summary = "Delete ward by id.")
    public ResponseEntity<WardResponse> deletewardById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(wardService.deleteWardById(id));
    }

    @GetMapping(value = "/ward/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Filter ward by name.")
    public ResponseEntity<WardResponse> filterWardByCode(@RequestParam String name) throws Exception {
        return ResponseEntity.ok(wardService.filterByName(name));
    }

    @GetMapping(value = "/ward/lga/{lgaCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find ward by lga code.")
    public ResponseEntity<WardResponse> filterWardByLgaCode(@PathVariable Long lgaCode) throws Exception {
        return ResponseEntity.ok(wardService.findByLga(lgaCode));
    }

    @GetMapping(value = "/ward/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find ward by senatorial district code.")
    public ResponseEntity<WardResponse> filterWardByLgaIdStateIdSenatorialDistrictID(@RequestParam(required = false, defaultValue = "0") Long stateId, @RequestParam(required = false, defaultValue = "0") Long senatorialDistrictId, @RequestParam(required = false, defaultValue = "0") Long lgaWardId)  throws Exception {
        return ResponseEntity.ok(wardService.searchWardByFilter(stateId,senatorialDistrictId, lgaWardId));
    }

    @PostMapping("/ward/upload")
    public ResponseEntity<WardResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(wardService.uploadWard(file), HttpStatus.OK);
    }
}
