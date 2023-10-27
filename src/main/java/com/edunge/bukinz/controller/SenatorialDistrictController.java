package com.edunge.bukinz.controller;

import com.edunge.bukinz.dto.SenatorialDistrictDto;
import com.edunge.bukinz.response.SenatorialDistrictResponse;
import com.edunge.bukinz.service.LgaService;
import com.edunge.bukinz.service.PollingUnitService;
import com.edunge.bukinz.service.SenatorialDistrictService;
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
@Api(value="Manage Senatorial District", description="Endpoints to manage Senatorial District")
@CrossOrigin(maxAge = 3600)
public class SenatorialDistrictController {

    private final SenatorialDistrictService senatorialDistrictService;
    private final LgaService lgaService;
    private final WardService wardService;
    private final PollingUnitService pollingUnitService;

    @Autowired
    public SenatorialDistrictController(SenatorialDistrictService senatorialDistrictService,
                                        LgaService lgaService, WardService wardService, PollingUnitService pollingUnitService) {
        this.senatorialDistrictService = senatorialDistrictService;
        this.lgaService = lgaService;
        this.wardService = wardService;
        this.pollingUnitService = pollingUnitService;
    }

    @GetMapping(value = "/senatorial-districts", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve all states.")
    public ResponseEntity<SenatorialDistrictResponse> findAllSenatorialDistricts(){
        return new ResponseEntity<>(senatorialDistrictService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/senatorial-district", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find state by state code.")
    public ResponseEntity<SenatorialDistrictResponse> findSenatorialDistrictByCode(@RequestParam String code) throws Exception {
        return ResponseEntity.ok(senatorialDistrictService.findSenatorialDistrictByCode(code));
    }

    @GetMapping(value = "/senatorial-district/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find senatorial district by id.")
    public ResponseEntity<SenatorialDistrictResponse> findSenatorialDistrictById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(senatorialDistrictService.findSenatorialDistrictById(id));
    }


    @RequestMapping(value = "/senatorial-district", method = RequestMethod.POST)
    @ApiOperation(value = "Save senatorial district to the DB")
    public ResponseEntity<SenatorialDistrictResponse> storeSenatorialDistrict(@RequestBody SenatorialDistrictDto senatorialDistrictDto) throws Exception {
        return ResponseEntity.ok(senatorialDistrictService.saveSenatorialDistrict(senatorialDistrictDto));
    }

    @RequestMapping(value = "/senatorial-district/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update senatorial district to the DB")
    public ResponseEntity<SenatorialDistrictResponse> updateSenatorialDistrict(@PathVariable Long id, @RequestBody SenatorialDistrictDto senatorialDistrictDto) throws Exception {
        SenatorialDistrictResponse senatorialDistrictResponse = senatorialDistrictService.updateSenatorialDistrict(id,senatorialDistrictDto);
        lgaService.updateLgaDistrict(id, senatorialDistrictResponse.getSenatorialDistrict());
        wardService.updateWardDistrict(id, senatorialDistrictResponse.getSenatorialDistrict());
        pollingUnitService.updatePollingUnitDistrict(id, senatorialDistrictResponse.getSenatorialDistrict());
        return ResponseEntity.ok(senatorialDistrictResponse);
    }

    @RequestMapping(value = "/senatorial-district/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete senatorial district by id.")
    public ResponseEntity<SenatorialDistrictResponse> deleteSenatorialDistrictById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(senatorialDistrictService.deleteSenatorialDistrictById(id));
    }

    @GetMapping(value = "/senatorial-district/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Filter senatorial district by code.")
    public ResponseEntity<SenatorialDistrictResponse> filterSenatorialDistrictByCode(@RequestParam String name) throws Exception {
        return ResponseEntity.ok(senatorialDistrictService.filterByName(name));
    }

    @GetMapping(value = "/senatorial-district/state/{stateCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find senatorial district by stateCode.")
    public ResponseEntity<SenatorialDistrictResponse> filterLGAByStateCode(@PathVariable Long stateCode) throws Exception {
        return ResponseEntity.ok(senatorialDistrictService.findSenatorialDistrictByStateCode(stateCode));
    }

    @GetMapping(value = "/senatorial-district/state/default", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find senatorial district for default state.")
    public ResponseEntity<SenatorialDistrictResponse> filterSenatorialDistrictForDefaultState() throws Exception {
        return ResponseEntity.ok(senatorialDistrictService.findSenatorialDistrictForDefaultState());
    }

    @PostMapping("/senatorial-district/upload")
    public ResponseEntity<SenatorialDistrictResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(senatorialDistrictService.uploadSenatorialDistrict(file), HttpStatus.OK);
    }
}
