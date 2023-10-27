package com.edunge.bukinz.service;

import com.edunge.bukinz.exceptions.NotFoundException;
import com.edunge.bukinz.model.GeoPoliticalZone;
import com.edunge.bukinz.response.GeoPoliticalZoneResponse;

import java.util.List;

public interface GeoPoliticalZoneService {
    GeoPoliticalZone findGeoPoliticalZoneById(Long id) throws NotFoundException;
    long countGeoPoliticalZone();
    List<GeoPoliticalZone> findAll() ;
    GeoPoliticalZoneResponse findAllZones() ;
}
