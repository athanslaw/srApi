package com.edunge.bukinz.service;

import com.edunge.bukinz.dto.UploadModel;
import com.edunge.bukinz.response.UploadResponse;

public interface UploadService {
    UploadResponse uploadFile(UploadModel model);
}
