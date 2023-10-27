package com.edunge.bukinz.controller;

import com.edunge.bukinz.dto.BusinessDetailsDto;
import com.edunge.bukinz.exceptions.NotFoundException;
import com.edunge.bukinz.response.BusinessDetailsResponse;
import com.edunge.bukinz.service.BusinessDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Api(value="Manage Business Information", description="Endpoints to manage Business Information")
@CrossOrigin(maxAge = 3600)
public class BusinessDetailsController {

    private final BusinessDetailsService businessDetailsService;

    @Autowired
    public BusinessDetailsController(BusinessDetailsService businessDetailsService) {
        this.businessDetailsService = businessDetailsService;
    }

    @GetMapping(value = "/business/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve all businesses.")
    public ResponseEntity<BusinessDetailsResponse> findAllBusinesses() throws NotFoundException {
        return new ResponseEntity<>(businessDetailsService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/business/phone/{phone}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find business by phone.")
    public ResponseEntity<BusinessDetailsResponse> findBusinessByPhone(@PathVariable String phone) throws Exception {
        return ResponseEntity.ok(businessDetailsService.findBusinessDetailsByPhone(phone));
    }

    @GetMapping(value = "/business/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find business by id.")
    public ResponseEntity<BusinessDetailsResponse> findBusinessById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(businessDetailsService.findBusinessDetailsById(id));
    }

    @RequestMapping(value = "/business", method = RequestMethod.POST)
    @ApiOperation(value = "Save business to the DB")
    public ResponseEntity<BusinessDetailsResponse> storeBusiness(@RequestBody BusinessDetailsDto businessDetailsDto) throws Exception {
        return ResponseEntity.ok(businessDetailsService.saveBusinessDetails(businessDetailsDto));
    }

    @RequestMapping(value = "/business/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update business to the DB")
    public ResponseEntity<BusinessDetailsResponse> updateBusinessDetails(@PathVariable Long id, @RequestBody BusinessDetailsDto businessDetailsDto) throws Exception {
        return ResponseEntity.ok(businessDetailsService.updateBusinessDetails(id,businessDetailsDto));
    }

    @RequestMapping(value = "/business/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete business by id.")
    public ResponseEntity<BusinessDetailsResponse> deleteBusinessById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(businessDetailsService.deleteBusinessDetailsById(id));
    }

    @GetMapping(value = "/business/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find business by name.")
    public ResponseEntity<BusinessDetailsResponse> filterBusinessByName(@RequestParam(required = false) String businessName) throws Exception {
        return ResponseEntity.ok(businessDetailsService.findBusinessDetailsByBusinessName(businessName));
    }

    @GetMapping(value = "/business/filter/city/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Filter by city.")
    public ResponseEntity<BusinessDetailsResponse> filterBusinessByCity(@PathVariable String id) throws Exception {
        return ResponseEntity.ok(businessDetailsService.findBusinessDetailsByCity(id));
    }

    @GetMapping(value = "/business/filter/postal-code/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Filter by postal code.")
    public ResponseEntity<BusinessDetailsResponse> filterBusinessByPostalCode(@PathVariable String id) throws Exception {
        return ResponseEntity.ok(businessDetailsService.findBusinessDetailsByPostalCode(id));
    }

}
