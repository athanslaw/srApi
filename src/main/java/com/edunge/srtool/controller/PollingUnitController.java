package com.edunge.srtool.controller;

import com.edunge.srtool.dto.PollingUnitDto;
import com.edunge.srtool.response.PollingUnitResponse;
import com.edunge.srtool.service.PollingUnitService;
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
@Api(value="Manage Polling Unit", description="Endpoints to manage Polling Unit")
@CrossOrigin(maxAge = 3600)
public class PollingUnitController {

    private final PollingUnitService pollingUnitService;

    @Autowired
    public PollingUnitController(PollingUnitService pollingUnitService) {
        this.pollingUnitService = pollingUnitService;
    }

    @GetMapping(value = "/polling-unit/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve all polling units.")
    public ResponseEntity<PollingUnitResponse> findAllPollingUnits(){
        return new ResponseEntity<>(pollingUnitService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/polling-unit", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find Polling unit by code.")
    public ResponseEntity<PollingUnitResponse> findPollingUnitByCode(@RequestParam String code) throws Exception {
        return ResponseEntity.ok(pollingUnitService.findPollingUnitByCode(code));
    }

    @GetMapping(value = "/polling-unit/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find Polling Unit by id.")
    public ResponseEntity<PollingUnitResponse> findPollingUnitById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(pollingUnitService.findPollingUnitById(id));
    }


    @RequestMapping(value = "/polling-unit", method = RequestMethod.POST)
    @ApiOperation(value = "Save Polling Unit to the DB")
    public ResponseEntity<PollingUnitResponse> storePollingUnit(@RequestBody PollingUnitDto pollingUnitDto) throws Exception {
        return ResponseEntity.ok(pollingUnitService.savePollingUnit(pollingUnitDto));
    }

    @RequestMapping(value = "/polling-unit/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update Polling Unit to the DB")
    public ResponseEntity<PollingUnitResponse> updatePollingUnit(@PathVariable Long id, @RequestBody PollingUnitDto pollingUnitDto) throws Exception {
        return ResponseEntity.ok(pollingUnitService.updatePollingUnit(id,pollingUnitDto));
    }

    @RequestMapping(value = "/polling-unit/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete Polling Unit by id.")
    public ResponseEntity<PollingUnitResponse> deletePollingUnitById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(pollingUnitService.deletePollingUnitById(id));
    }

    @GetMapping(value = "/polling-unit/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Filter polling unit by name.")
    public ResponseEntity<PollingUnitResponse> filterPollingUnitByCode(@RequestParam String name) throws Exception {
        return ResponseEntity.ok(pollingUnitService.filterByName(name));
    }

    @GetMapping(value = "/polling-unit/ward/{wardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find polling unit by ward id.")
    public ResponseEntity<PollingUnitResponse> filterPollingUnitByCode(@PathVariable Long wardId) throws Exception {
        return ResponseEntity.ok(pollingUnitService.findByWardCode(wardId));
    }

    @GetMapping(value = "/polling-unit/lga/{lgaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find polling unit by lga id.")
    public ResponseEntity<PollingUnitResponse> filterPollingUnitByLga(@PathVariable Long lgaId) throws Exception {
        return ResponseEntity.ok(pollingUnitService.findByLga(lgaId));
    }

    @GetMapping(value = "/polling-unit/state/{stateCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find polling unit by state id.")
    public ResponseEntity<PollingUnitResponse> filterPollingUnitByState(@PathVariable Long stateCode) throws Exception {
        return ResponseEntity.ok(pollingUnitService.findByState(stateCode));
    }

    @GetMapping(value = "/polling-unit/senatorial-district/{senatorialDistrictCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find polling unit by senatorial id.")
    public ResponseEntity<PollingUnitResponse> filterPollingUnitBySenatorialDistrict(@PathVariable Long senatorialDistrictCode) throws Exception {
        return ResponseEntity.ok(pollingUnitService.findBySenatorialDistrict(senatorialDistrictCode));
    }

    @PostMapping("/polling-unit/upload")
    public ResponseEntity<PollingUnitResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(pollingUnitService.uploadPollingUnit(file), HttpStatus.OK);
    }
}
