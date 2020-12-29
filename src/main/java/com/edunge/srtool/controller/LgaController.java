package com.edunge.srtool.controller;

import com.edunge.srtool.dto.LgaDto;
import com.edunge.srtool.response.LgaResponse;
import com.edunge.srtool.service.LgaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Api(value="Manage Lga", description="Endpoints to manage LGA")
public class LgaController {

    private final LgaService lgaService;

    @Autowired
    public LgaController(LgaService lgaService) {
        this.lgaService = lgaService;
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
        return ResponseEntity.ok(lgaService.updateLga(id,lgaDto));
    }

    @RequestMapping(value = "/lga/delete/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole=ADMIN")
    @ApiOperation(value = "Delete LGA by id.")
    public ResponseEntity<LgaResponse> deleteLgaById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(lgaService.deleteLgaById(id));
    }

    @GetMapping(value = "/lga/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Filter LGA by name.")
    public ResponseEntity<LgaResponse> filterLGAByName(@RequestParam String name) throws Exception {
        return ResponseEntity.ok(lgaService.filterByName(name));
    }
}
