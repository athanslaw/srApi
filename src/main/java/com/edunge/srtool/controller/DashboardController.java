package com.edunge.srtool.controller;

import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.DashboardResponse;
import com.edunge.srtool.response.IncidentDashboardResponse;
import com.edunge.srtool.response.NationalDashboardResponse;
import com.edunge.srtool.service.DashboardService;
import com.edunge.srtool.service.EventRecordDashboardService;
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
    private final EventRecordDashboardService eventRecordDashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService, IncidentDashboardService incidentDashboardService,
                               EventRecordDashboardService eventRecordDashboardService) {
        this.dashboardService = dashboardService;
        this.incidentDashboardService = incidentDashboardService;
        this.eventRecordDashboardService = eventRecordDashboardService;
    }

    @GetMapping(value = "/dashboard/{electionType}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve default dashboard.")
    public ResponseEntity<DashboardResponse> getDashboard(@PathVariable Long electionType) throws NotFoundException {
        return new ResponseEntity<>(dashboardService.getDefaultDashboard(electionType), HttpStatus.OK);
    }

    @GetMapping(value = "/dashboard/default-state/{stateId}/{electionType}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve results for default state.")
    public ResponseEntity<DashboardResponse> getDashboardByState(@PathVariable Long stateId, @PathVariable Long electionType) throws NotFoundException {
        return ResponseEntity.ok(dashboardService.getDashboardByState(stateId, electionType));
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

    @GetMapping(value = "/dashboard/national/zonal/{id}/{electionType}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Geo Political Zone Dashboard")
    public ResponseEntity<NationalDashboardResponse> getDashboardByZone(@PathVariable Long id, @PathVariable Long electionType) throws Exception {
        return ResponseEntity.ok(dashboardService.getDashboardByZone(id, electionType));
    }

    @GetMapping(value = "/dashboard/national/state/{id}/{electionType}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Geo Political Zone Dashboard")
    public ResponseEntity<NationalDashboardResponse> getDashboardByStateNational(@PathVariable Long id, @PathVariable Long electionType) throws Exception {
        return ResponseEntity.ok(dashboardService.getDashboardByStateGlobal(id, electionType));
    }

    @GetMapping(value = "/dashboard/state/{id}/{electionType}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Dashboard result by LGA")
    public ResponseEntity<DashboardResponse> getDashboardByStateLga(@PathVariable Long id, @PathVariable Long electionType) throws Exception {
        return ResponseEntity.ok(dashboardService.getDashboardByLga(id, electionType));
    }

    @GetMapping(value = "/dashboard/incidents", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Incident Dashboard By State")
    public ResponseEntity<IncidentDashboardResponse> getIncidentDashboardByState() throws Exception {
        return ResponseEntity.ok(incidentDashboardService.getDashboardByState());
    }

    @GetMapping(value = "/dashboard/incidents/state/{stateId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Incident Dashboard By State")
    public ResponseEntity<IncidentDashboardResponse> getIncidentDashboardByState(@PathVariable Long stateId) throws Exception {
        return ResponseEntity.ok(incidentDashboardService.getDashboardByState(stateId));
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







    @GetMapping(value = "/dashboard/events", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Events Dashboard By State")
    public ResponseEntity<IncidentDashboardResponse> getEventDashboardByState() throws Exception {
        return ResponseEntity.ok(eventRecordDashboardService.getDashboardByState());
    }

    @GetMapping(value = "/dashboard/events/state/{stateId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Event Dashboard By State")
    public ResponseEntity<IncidentDashboardResponse> getEventDashboardByState(@PathVariable Long stateId) throws Exception {
        return ResponseEntity.ok(eventRecordDashboardService.getDashboardByState(stateId));
    }

    @GetMapping(value = "/dashboard/events/senatorial-district/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Event Dashboard By District")
    public ResponseEntity<IncidentDashboardResponse> getEventDashboardByDistrict(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(eventRecordDashboardService.getDashboardBySenatorialDistrict(id));
    }

    @GetMapping(value = "/dashboard/events/lga/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Event Dashboard By Lga")
    public ResponseEntity<IncidentDashboardResponse> getEventDashboardByLga(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(eventRecordDashboardService.getDashboardByLga(id));
    }
}
