package com.edunge.srtool.controller;

import com.edunge.srtool.dto.WardDto;
import com.edunge.srtool.response.WardResponse;
import com.edunge.srtool.service.WardService;
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
@Api(value="Manage Ward", description="Endpoints to manage Ward")
@CrossOrigin(maxAge = 3600)
public class WardController {

    private final WardService wardService;

    @Autowired
    public WardController(WardService wardService) {
        this.wardService = wardService;
    }

    @GetMapping(value = "/wards", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve all states.")
    public ResponseEntity<WardResponse> findAllWards(){
        return new ResponseEntity<>(wardService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/ward", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find ward by code.")
    public ResponseEntity<WardResponse> findWardByCode(@RequestParam String code) throws Exception {
        return ResponseEntity.ok(wardService.findWardByCode(code));
    }

    @GetMapping(value = "/ward/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find ward by id.")
    public ResponseEntity<WardResponse> findWardById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(wardService.findWardById(id));
    }


    @RequestMapping(value = "/ward", method = RequestMethod.POST)
    @ApiOperation(value = "Save ward to the DB")
    public ResponseEntity<WardResponse> storeWard(@RequestBody WardDto wardDto) throws Exception {
        return ResponseEntity.ok(wardService.saveWard(wardDto));
    }

    @RequestMapping(value = "/ward/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update ward to the DB")
    public ResponseEntity<WardResponse> updateWard(@PathVariable Long id, @RequestBody WardDto wardDto) throws Exception {
        return ResponseEntity.ok(wardService.updateWard(id,wardDto));
    }

    @RequestMapping(value = "/ward/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete ward by id.")
    public ResponseEntity<WardResponse> deletewardById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(wardService.deleteWardById(id));
    }

    @GetMapping(value = "/ward/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Filter ward by name.")
    public ResponseEntity<WardResponse> filterWardByCode(@RequestParam String name) throws Exception {
        return ResponseEntity.ok(wardService.filterByName(name));
    }

    @GetMapping(value = "/ward/lga/{lgaCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find ward by lga code.")
    public ResponseEntity<WardResponse> filterWardByLgaCode(@PathVariable Long lgaCode) throws Exception {
        return ResponseEntity.ok(wardService.findByLga(lgaCode));
    }

    @GetMapping(value = "/ward/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find ward by senatorial district code.")
    public ResponseEntity<WardResponse> filterWardByLgaIdStateIdSenatorialDistrictID(@RequestParam(required = false, defaultValue = "0") Long stateId, @RequestParam(required = false, defaultValue = "0") Long senatorialDistrictId, @RequestParam(required = false, defaultValue = "0") Long lgaWardId)  throws Exception {
        return ResponseEntity.ok(wardService.searchWardByFilter(stateId,senatorialDistrictId, lgaWardId));
    }

    @PostMapping("/ward/upload")
    public ResponseEntity<WardResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(wardService.uploadWard(file), HttpStatus.OK);
    }
}
