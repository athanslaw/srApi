package com.edunge.srtool.controller;

import com.edunge.srtool.dto.ResultDto;
import com.edunge.srtool.response.ResultResponse;
import com.edunge.srtool.service.ResultService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/result/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve all results.")
    public ResponseEntity<ResultResponse> findAllResults(){
        return new ResponseEntity<>(resultService.findAll(), HttpStatus.OK);
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
}