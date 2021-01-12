package com.edunge.srtool.service;

import com.edunge.srtool.dto.ResultDto;
import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.response.ResultResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ResultService {
    ResultResponse saveResult(ResultDto ward) throws NotFoundException;
    ResultResponse findResultById(Long id) throws NotFoundException;
    ResultResponse updateResult(Long id, ResultDto ward) throws NotFoundException;
    ResultResponse deleteResultById(Long id) throws NotFoundException;
    ResultResponse findAll() ;

    ResultResponse uploadResult(MultipartFile file);
}
