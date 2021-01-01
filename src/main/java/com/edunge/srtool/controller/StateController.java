package com.edunge.srtool.controller;

import com.edunge.srtool.response.StateResponse;
import com.edunge.srtool.service.StateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@Api(value="Manage States", description="Endpoints to manage States")
@CrossOrigin(maxAge = 3600)
public class StateController {
    private static final Logger logger = LoggerFactory.getLogger(PoliticalPartyCandidateController.class);
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


    @PostMapping(value =  "/state")
    @ApiOperation(value = "Save state to the DB")
    public ResponseEntity<StateResponse> storeState( @RequestParam String code, @RequestParam String name, @RequestParam MultipartFile file) throws Exception {
        return ResponseEntity.ok(stateService.saveState(code, name, file));
    }

    @PutMapping(value = "/state/{id}")
    @ApiOperation(value = "Update state to the DB")
    public ResponseEntity<StateResponse> updateState(@PathVariable Long id, @RequestParam String code, @RequestParam String name, @RequestParam MultipartFile file) throws Exception {
        return ResponseEntity.ok(stateService.editState(id, code, name, file));
    }

    @RequestMapping(value = "/state/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete state by id.")
    public ResponseEntity<StateResponse> deleteStateById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(stateService.deleteStateById(id));
    }

    @GetMapping(value = "/state/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Filter state by name.")
    public ResponseEntity<StateResponse> filterStateByCode(@RequestParam String name) throws Exception {
        return ResponseEntity.ok(stateService.filterByName(name));
    }

    @GetMapping("/uploads/svg/{fileName:.+}")
    public ResponseEntity<Resource> loadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = stateService.loadSvg(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
