package com.edunge.srtool.controller;

import com.edunge.srtool.response.DashboardResponse;
import com.edunge.srtool.service.DashboardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Api(value="Dashboard", description="Endpoints to get dashboards")
@CrossOrigin(maxAge = 3600)
public class DashboardController {

    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping(value = "/dashboard/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve default dashboard.")
    public ResponseEntity<DashboardResponse> getDashboard(){
        return new ResponseEntity<>(dashboardService.getDefaultDashboard(), HttpStatus.OK);
    }

    @GetMapping(value = "/dashboard/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve default by id.")
    public ResponseEntity<DashboardResponse> getDashboardByState(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(dashboardService.getDashboardByState(id));
    }

    @GetMapping(value = "/dashboard/senatorial-district/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve default by id.")
    public ResponseEntity<DashboardResponse> getDashboardBySenatorialDistrict(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(dashboardService.getDashboardBySenatorialDistrict(id));
    }
}
