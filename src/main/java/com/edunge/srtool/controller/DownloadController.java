package com.edunge.srtool.controller;

import com.edunge.srtool.service.DownloadService;
import com.edunge.srtool.service.impl.DownloadServiceImpl;
import com.edunge.srtool.service.impl.DownloadServiceImpl.ResultDownload;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        List<ResultDownload> resultDownloads = downloadService.getResults();
        DownloadServiceImpl.writeResults(response.getWriter(),resultDownloads);
        return new ResponseEntity(HttpStatus.OK);
    }
}
