package com.edunge.srtool.controller;

import com.edunge.srtool.response.GeoPoliticalZoneResponse;
import com.edunge.srtool.service.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Manage Zones", description = "Endpoints to manage Geo Political Zone")
@CrossOrigin(maxAge = 3600)
public class GeoPoliticalZoneController {

    private final GeoPoliticalZoneService geoPoliticalZoneService;

    @Autowired
    public GeoPoliticalZoneController(GeoPoliticalZoneService geoPoliticalZoneService) {
        this.geoPoliticalZoneService = geoPoliticalZoneService;
    }

    @GetMapping(value = "/zones", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieve all zones.")
    public ResponseEntity<GeoPoliticalZoneResponse> findAllGeoPoliticalZones(){
        return new ResponseEntity<>(geoPoliticalZoneService.findAllZones(), HttpStatus.OK);
    }

}
