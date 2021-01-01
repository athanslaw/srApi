package com.edunge.srtool.controller;

import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.PoliticalPartyCandidateResponse;
import com.edunge.srtool.service.PoliticalPartyCandidateService;
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
@Api(value="Manage Political Party Candidate", description="Endpoints to manage Political Party Candidates")
@CrossOrigin(maxAge = 3600)
public class PoliticalPartyCandidateController {
    private final PoliticalPartyCandidateService politicalPartyCandidateService;

    private static final Logger logger = LoggerFactory.getLogger(PoliticalPartyCandidateController.class);
    @Autowired
    public PoliticalPartyCandidateController(PoliticalPartyCandidateService politicalPartyCandidateService) {
        this.politicalPartyCandidateService = politicalPartyCandidateService;
    }

    @GetMapping(value = "/political-party-candidate/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve all Political Parties.")
    public ResponseEntity<PoliticalPartyCandidateResponse> findAllParties(){
        return new ResponseEntity<>(politicalPartyCandidateService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/political-party-candidate/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve all Political Party by Id")
    public ResponseEntity<PoliticalPartyCandidateResponse> findCandidateById(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(politicalPartyCandidateService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping(value = "/political-party-candidate/delete/{id}")
    @ApiOperation(value = "Retrieve all Political Party by Id")
    public ResponseEntity<PoliticalPartyCandidateResponse> deleteCandidate(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(politicalPartyCandidateService.deleteById(id), HttpStatus.OK);
    }

    @PostMapping(value = "/political-party-candidate")
    public ResponseEntity<PoliticalPartyCandidateResponse> savePoliticalCandidateResponse(@RequestParam Long electionId,
                                                                                          @RequestParam Long politicalPartyId,
                                                                                          @RequestParam String firstName,
                                                                                          @RequestParam String lastName,
                                                                                          @RequestParam("file") MultipartFile file) throws NotFoundException {

        PoliticalPartyCandidateResponse response = politicalPartyCandidateService.savePoliticalPartyCandidate(
                electionId,politicalPartyId,firstName, lastName,file);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PutMapping(value = "/political-party-candidate/{id}")
    public ResponseEntity<PoliticalPartyCandidateResponse> updatePoliticalCandidateResponse(@PathVariable Long id,
                                                                                            @RequestParam Long electionId,
                                                                                            @RequestParam Long politicalPartyId,
                                                                                            @RequestParam String firstName,
                                                                                            @RequestParam String lastName,
                                                                                            @RequestParam("file") MultipartFile file) throws NotFoundException {

        PoliticalPartyCandidateResponse response = politicalPartyCandidateService.updatePoliticalPartyCandidate(id,
                electionId,politicalPartyId,firstName, lastName,file);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


    @GetMapping("/uploads/img/{fileName:.+}")
    public ResponseEntity<Resource> loadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = politicalPartyCandidateService.loadPoliticalPartyImage(fileName);

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


    @GetMapping(value = "/political-party-candidate/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find candidate agent by name.")
    public ResponseEntity<PoliticalPartyCandidateResponse> filterPartyAgentByName(@RequestParam(required = false) String firstname, @RequestParam(required = false) String lastname) throws Exception {
        return ResponseEntity.ok(politicalPartyCandidateService.findCandidateByName(firstname, lastname));
    }
}
