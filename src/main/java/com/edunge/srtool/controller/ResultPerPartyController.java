package com.edunge.srtool.controller;

import com.edunge.srtool.dto.ResultPerPartyDto;
import com.edunge.srtool.response.ResultPerPartyResponse;
import com.edunge.srtool.service.ResultPerPartyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Api(value="Manage Result Per Party", description="Endpoints to manage Result Per Party")
@CrossOrigin(maxAge = 3600, allowedHeaders = "*")
public class ResultPerPartyController {

    private final ResultPerPartyService resultPerPartyService;

    @Autowired
    public ResultPerPartyController(ResultPerPartyService resultPerPartyService) {
        this.resultPerPartyService = resultPerPartyService;
    }

    @GetMapping(value = "/result-per-party/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve all result per party.")
    public ResponseEntity<ResultPerPartyResponse> findAllResultPerParty(){
        return new ResponseEntity<>(resultPerPartyService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/result-per-party", method = RequestMethod.POST)
    @ApiOperation(value = "Save result per party to the DB")
    public ResponseEntity<ResultPerPartyResponse> storeResultPerParty(@RequestBody ResultPerPartyDto resultDto) throws Exception {
        return ResponseEntity.ok(resultPerPartyService.saveResultPerParty(resultDto));
    }

    @RequestMapping(value = "/result-per-party/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update result per party to the DB")
    public ResponseEntity<ResultPerPartyResponse> updateResult(@PathVariable Long id, @RequestBody ResultPerPartyDto resultDto) throws Exception {
        return ResponseEntity.ok(resultPerPartyService.updateResultPerParty(id,resultDto));
    }

    @RequestMapping(value = "/result-per-party/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete result per party by id.")
    public ResponseEntity<ResultPerPartyResponse> deleteResultById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(resultPerPartyService.deleteResultPerPartyById(id));
    }

    @GetMapping(value = "/result-per-party/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Filter result per party by name.")
    public ResponseEntity<ResultPerPartyResponse> filterResultById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(resultPerPartyService.findResultPerPartyById(id));
    }
}
