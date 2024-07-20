package com.edunge.srtool.controller;

import com.edunge.srtool.dto.ResultDto;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.ResultRealTimeResponse;
import com.edunge.srtool.response.ResultResponse;
import com.edunge.srtool.service.ResultService;
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
@Api(value="Manage Result", description="Endpoints to manage Result")
@CrossOrigin(maxAge = 3600)
public class ResultController {

    private final ResultService resultService;

    @Autowired
    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping(value = "/result/filter/zone/{zoneId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve all results.")
    public ResponseEntity<ResultResponse> findResultsByZone(@PathVariable Long zoneId, @RequestParam(value = "electionType", required = false) Long electionType) throws NotFoundException{
        return new ResponseEntity<>(resultService.findByZoneId(zoneId, electionType), HttpStatus.OK);
    }


    @GetMapping(value = "/result/filter/state/{stateId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve all results.")
    public ResponseEntity<ResultResponse> findAllResults(@PathVariable Long stateId, @RequestParam(value = "electionType", required = false) Long electionType) throws NotFoundException {
        return new ResponseEntity<>(resultService.findByStateId(stateId, electionType), HttpStatus.OK);
    }

    @RequestMapping(value = "/result", method = RequestMethod.POST)
    @ApiOperation(value = "Save result to the DB")
    public ResponseEntity<ResultResponse> storeResult(@RequestBody ResultDto resultDto) throws Exception {
        return ResponseEntity.ok(resultService.saveResult(resultDto));
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update result to the DB")
    public ResponseEntity<ResultResponse> updateResult(@PathVariable Long id, @RequestBody ResultDto resultDto) throws Exception {
        return ResponseEntity.ok(resultService.updateResult(id,resultDto));
    }

    @RequestMapping(value = "/result/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete result by id.")
    public ResponseEntity<ResultResponse> deleteResultById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(resultService.deleteResultById(id));
    }

    @GetMapping(value = "/result/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Filter result by name.")
    public ResponseEntity<ResultResponse> filterResultByCode(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(resultService.findResultById(id));
    }

    @GetMapping(value = "/result/filter/senatorialDistrict/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Filter by senatorial district.")
    public ResponseEntity<ResultResponse> filterResultBySenatorialDistrict(@PathVariable Long id, @RequestParam(value = "electionType", required = false) Long electionType) throws Exception {
        return ResponseEntity.ok(resultService.filterBySenatorialDistrict(id, electionType));
    }

    @GetMapping(value = "/result/filter/lga/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Filter by name.")
    public ResponseEntity<ResultResponse> filterResultByLga(@PathVariable Long id, @RequestParam(value = "electionType", required = false) Long electionType) throws Exception {
        return ResponseEntity.ok(resultService.filterByLga(id, electionType));
    }

    @GetMapping(value = "/result/filter/ward/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Filter by name.")
    public ResponseEntity<ResultResponse> filterResultByWard(@PathVariable Long id, @RequestParam(value = "electionType", required = false) Long electionType) throws Exception {
        return ResponseEntity.ok(resultService.filterByWard(id, electionType));
    }

    @GetMapping(value = "/result/filter/polling-unit/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Filter by name.")
    public ResponseEntity<ResultResponse> filterResultByPollingUnit(@PathVariable Long id, @RequestParam(value = "electionType", required = false) Long electionType) throws Exception {
        return ResponseEntity.ok(resultService.filterByPollingUnit(id, electionType));
    }

    @PostMapping("/result/upload")
    public ResponseEntity<ResultResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(resultService.uploadResult(file), HttpStatus.OK);
    }
}
