package com.edunge.bukinz.controller;

import com.edunge.bukinz.dto.LgaDto;
import com.edunge.bukinz.response.LgaResponse;
import com.edunge.bukinz.service.LgaService;
import com.edunge.bukinz.service.PollingUnitService;
import com.edunge.bukinz.service.WardService;
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
@Api(value="Manage Lga", description="Endpoints to manage LGA")
@CrossOrigin(maxAge = 3600)
public class LgaController {

    private final LgaService lgaService;
    private final WardService wardService;
    private final PollingUnitService pollingUnitService;

    @Autowired
    public LgaController(LgaService lgaService, WardService wardService, PollingUnitService pollingUnitService) {
        this.lgaService = lgaService;
        this.wardService = wardService;
        this.pollingUnitService = pollingUnitService;
    }

    @GetMapping(value = "/lgas", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve all states.")
    public ResponseEntity<LgaResponse> findAllLgas(){
        return new ResponseEntity<>(lgaService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/lga", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find state by code.")
    public ResponseEntity<LgaResponse> findLgaByCode(@RequestParam String code) throws Exception {
        return ResponseEntity.ok(lgaService.findLgaByCode(code));
    }

    @GetMapping(value = "/lga/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find LGA by id.")
    public ResponseEntity<LgaResponse> findLgaById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(lgaService.findLgaById(id));
    }


    @RequestMapping(value = "/lga", method = RequestMethod.POST)
    @ApiOperation(value = "Save LGA to the DB")
    public ResponseEntity<LgaResponse> storeLga(@RequestBody LgaDto lgaDto) throws Exception {
        return ResponseEntity.ok(lgaService.saveLga(lgaDto));
    }

    @RequestMapping(value = "/lga/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update LGA to the DB")
    public ResponseEntity<LgaResponse> updateLga(@PathVariable Long id, @RequestBody LgaDto lgaDto) throws Exception {
        LgaResponse lgaResponse = lgaService.updateLga(id,lgaDto);
        wardService.updateWardLga(id, lgaResponse.getLga());
        pollingUnitService.updatePollingUnitLga(id, lgaResponse.getLga());
        return ResponseEntity.ok(lgaResponse);
    }

    @RequestMapping(value = "/lga/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete LGA by id.")
    public ResponseEntity<LgaResponse> deleteLgaById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(lgaService.deleteLgaById(id));
    }

    @GetMapping(value = "/lga/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Filter LGA by name.")
    public ResponseEntity<LgaResponse> filterLGAByName(@RequestParam String name) throws Exception {
        return ResponseEntity.ok(lgaService.filterByName(name));
    }

    @GetMapping(value = "/lga/state", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find LGA by stateCode.")
    public ResponseEntity<LgaResponse> filterLGAByStateCode() throws Exception {
        return ResponseEntity.ok(lgaService.findLgaByStateCode());
    }

    @GetMapping(value = "/lga/state/{stateCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find LGA by stateCode.")
    public ResponseEntity<LgaResponse> filterLGAByStateCode(@PathVariable Long stateCode) throws Exception {
        return ResponseEntity.ok(lgaService.findLgaByStateCode(stateCode));
    }

    @GetMapping(value = "/lga/senatorial-district/{senatorialDistrict}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find LGA by senatorial district code.")
    public ResponseEntity<LgaResponse> filterLGABySenatorialDistrict(@PathVariable Long senatorialDistrict) throws Exception {
        return ResponseEntity.ok(lgaService.findLgaBySenatorialDistrictCode(senatorialDistrict));
    }

    @GetMapping(value = "/lga/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find LGA by stateCode.")
    public ResponseEntity<LgaResponse> filterLGABySearch(@RequestParam(required = false, defaultValue = "0") Long stateId, @RequestParam(required = false, defaultValue = "0") Long senatorialDistrictId) throws Exception {
        return ResponseEntity.ok(lgaService.findLgaFilter(stateId, senatorialDistrictId));
    }

    @PostMapping("/lga/upload")
    public ResponseEntity<LgaResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(lgaService.uploadLga(file), HttpStatus.OK);
    }
}
