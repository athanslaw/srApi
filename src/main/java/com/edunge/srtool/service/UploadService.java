package com.edunge.srtool.service;

import com.edunge.srtool.dto.UploadModel;
import com.edunge.srtool.response.UploadResponse;

public interface UploadService {
    UploadResponse uploadFile(UploadModel model);
}
