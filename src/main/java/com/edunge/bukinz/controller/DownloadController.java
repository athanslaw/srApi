package com.edunge.bukinz.controller;

import com.edunge.bukinz.service.DownloadService;
import com.edunge.bukinz.service.impl.DownloadServiceImpl;
import com.edunge.bukinz.service.impl.DownloadServiceImpl.ResultDownload;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Api(value="Download Results", description="Download election results")
@CrossOrigin(maxAge = 3600)
public class DownloadController {
    private final DownloadService downloadService;

    public DownloadController(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/results/download")
    public ResponseEntity downloadResult(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Disposition", "attachment; filename=election_results");
        response.setHeader("Content-Type", "application/octet-stream");
        DownloadServiceImpl.writeResults(response.getWriter(),new ArrayList<>());
        return new ResponseEntity(HttpStatus.OK);
    }
}
