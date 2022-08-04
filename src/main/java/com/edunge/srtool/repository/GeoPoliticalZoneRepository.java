package com.edunge.srtool.repository;

import com.edunge.srtool.model.GeoPoliticalZone;
import com.edunge.srtool.model.SenatorialDistrict;
import com.edunge.srtool.model.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GeoPoliticalZoneRepository extends JpaRepository<GeoPoliticalZone, Long> {
}
