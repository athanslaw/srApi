package com.edunge.srtool.service;

import com.edunge.srtool.service.impl.DownloadServiceImpl;

import java.util.List;

public interface DownloadService {
    List<DownloadServiceImpl.ResultDownload> getResults();
}
