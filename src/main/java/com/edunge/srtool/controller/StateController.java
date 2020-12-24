package com.edunge.srtool.controller;

import com.edunge.srtool.dto.UserDto;
import com.edunge.srtool.exceptions.BadRequestException;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.exceptions.NotificationException;
import com.edunge.srtool.exceptions.UserException;
import com.edunge.srtool.jwt.JwtTokenUtil;
import com.edunge.srtool.jwt.JwtUserDetailsService;
import com.edunge.srtool.model.Login;
import com.edunge.srtool.model.State;
import com.edunge.srtool.response.ErrorResponse;
import com.edunge.srtool.response.LoginResponse;
import com.edunge.srtool.response.StateResponse;
import com.edunge.srtool.response.UserResponse;
import com.edunge.srtool.service.StateService;
import com.edunge.srtool.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/api/v1")
@Api(value="Manage States", description="Endpoints to manage Senatorial District")
public class StateController {

    private final StateService stateService;

    @Autowired
    public StateController( StateService stateService) {
        this.stateService = stateService;
    }

    @GetMapping(value = "/states", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve all states.")
    public ResponseEntity<StateResponse> findAllStates(){
        return new ResponseEntity<>(stateService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/state", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find state by state code.")
    public ResponseEntity<StateResponse> findStateByCode(@RequestParam String code) throws Exception {
        return ResponseEntity.ok(stateService.findStateByCode(code));
    }

    @GetMapping(value = "/state/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find state by id.")
    public ResponseEntity<StateResponse> findStateById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(stateService.findStateById(id));
    }


    @RequestMapping(value = "/state", method = RequestMethod.POST)
    @ApiOperation(value = "Save state to the DB")
    public ResponseEntity<StateResponse> storeState(@RequestBody State state) throws Exception {
        return ResponseEntity.ok(stateService.saveState(state));
    }

    @RequestMapping(value = "/state/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update state to the DB")
    public ResponseEntity<StateResponse> updateState(@PathVariable Long id, @RequestBody State state) throws Exception {
        return ResponseEntity.ok(stateService.editState(id, state));
    }

    @RequestMapping(value = "/state/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete state by id.")
    public ResponseEntity<StateResponse> deleteStateById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(stateService.deleteStateById(id));
    }
}
