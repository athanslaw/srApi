package com.edunge.srtool.controller;

import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.DashboardResponse;
import com.edunge.srtool.response.IncidentDashboardResponse;
import com.edunge.srtool.response.NationalDashboardResponse;
import com.edunge.srtool.service.DashboardService;
import com.edunge.srtool.service.IncidentDashboardService;
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
    private final IncidentDashboardService incidentDashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService, IncidentDashboardService incidentDashboardService) {
        this.dashboardService = dashboardService;
        this.incidentDashboardService = incidentDashboardService;
    }

    @GetMapping(value = "/dashboard/{electionType}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve default dashboard.")
    public ResponseEntity<DashboardResponse> getDashboard(@PathVariable Long electionType) throws NotFoundException {
        return new ResponseEntity<>(dashboardService.getDefaultDashboard(electionType), HttpStatus.OK);
    }

    @GetMapping(value = "/dashboard/default-state/{electionType}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve results for default state.")
    public ResponseEntity<DashboardResponse> getDashboardByState(@PathVariable Long electionType) throws NotFoundException {
        return ResponseEntity.ok(dashboardService.getDashboardByState(electionType));
    }

    @GetMapping(value = "/dashboard/senatorial-district/{id}/{electionType}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Senatorial District Dashboard")
    public ResponseEntity<DashboardResponse> getDashboardBySenatorialDistrict(@PathVariable Long id, @PathVariable Long electionType) throws Exception {
        return ResponseEntity.ok(dashboardService.getDashboardBySenatorialDistrict(id, electionType));
    }

    @GetMapping(value = "/dashboard/lga/{id}/{electionType}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Dashboard result by LGA")
    public ResponseEntity<DashboardResponse> getDashboardByLga(@PathVariable Long id, @PathVariable Long electionType) throws Exception {
        return ResponseEntity.ok(dashboardService.getDashboardByLga(id, electionType));
    }


    /**
     * implement results for national report
     */

    @GetMapping(value = "/dashboard/national/{electionType}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve results for default state.")
    public ResponseEntity<NationalDashboardResponse> getDashboardByCountry(@PathVariable Long electionType) throws NotFoundException {
        return ResponseEntity.ok(dashboardService.getDashboardByCountry(electionType));
    }

    @GetMapping(value = "/dashboard/zonal/{id}/{electionType}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Geo Political Zone Dashboard")
    public ResponseEntity<NationalDashboardResponse> getDashboardByZone(@PathVariable Long id, @PathVariable Long electionType) throws Exception {
        return ResponseEntity.ok(dashboardService.getDashboardByZone(id, electionType));
    }

    @GetMapping(value = "/dashboard/state/{id}/{electionType}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Dashboard result by LGA")
    public ResponseEntity<DashboardResponse> getDashboardByState(@PathVariable Long id, @PathVariable Long electionType) throws Exception {
        return ResponseEntity.ok(dashboardService.getDashboardByLga(id, electionType));
    }

    @GetMapping(value = "/dashboard/incidents", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Incident Dashboard By State")
    public ResponseEntity<IncidentDashboardResponse> getIncidentDashboardByState() throws Exception {
        return ResponseEntity.ok(incidentDashboardService.getDashboardByState());
    }

    @GetMapping(value = "/dashboard/incidents/senatorial-district/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Incident Dashboard By District")
    public ResponseEntity<IncidentDashboardResponse> getIncidentDashboardByDistrict(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(incidentDashboardService.getDashboardBySenatorialDistrict(id));
    }

    @GetMapping(value = "/dashboard/incidents/lga/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Incident Dashboard By Lga")
    public ResponseEntity<IncidentDashboardResponse> getIncidentDashboardByLga(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(incidentDashboardService.getDashboardByLga(id));
    }
}
