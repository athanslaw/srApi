package com.edunge.srtool.service.impl;

import com.edunge.srtool.exceptions.NotFoundException;
import com.edunge.srtool.model.GeoPoliticalZone;
import com.edunge.srtool.repository.GeoPoliticalZoneRepository;
import com.edunge.srtool.response.GeoPoliticalZoneResponse;
import com.edunge.srtool.response.StateResponse;
import com.edunge.srtool.service.GeoPoliticalZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GeoPoliticalZoneServiceImpl implements GeoPoliticalZoneService {

    @Autowired
    GeoPoliticalZoneRepository geoPoliticalZoneRepository;

    @Override
    public GeoPoliticalZone findGeoPoliticalZoneById(Long id) throws NotFoundException {
        Optional<GeoPoliticalZone> geoPoliticalZone = geoPoliticalZoneRepository.findById(id);
        if(!geoPoliticalZone.isPresent()){
            throw new NotFoundException("GeoPolitical zone not found.");
        }
        return geoPoliticalZone.get();
    }

    @Override
    public long countGeoPoliticalZone() {
        return geoPoliticalZoneRepository.count();
    }

    @Override
    public List<GeoPoliticalZone> findAll() {
        return geoPoliticalZoneRepository.findAll();
    }

    @Override
    public GeoPoliticalZoneResponse findAllZones() {
        return new GeoPoliticalZoneResponse("00", "Retrieved Geopolitical zones",findAll());
    }
}
