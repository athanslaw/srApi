package com.edunge.srtool.service;

import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.GeoPoliticalZone;
import com.edunge.srtool.response.GeoPoliticalZoneResponse;

import java.util.List;

public interface GeoPoliticalZoneService {
    GeoPoliticalZone findGeoPoliticalZoneById(Long id) throws NotFoundException;
    long countGeoPoliticalZone();
    List<GeoPoliticalZone> findAll() ;
    GeoPoliticalZoneResponse findAllZones() ;
}
