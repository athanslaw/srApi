package com.edunge.srtool.repository;

import com.edunge.srtool.model.SenatorialDistrict;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SenatorialDistrictRepository extends JpaRepository<SenatorialDistrict, Long> {
    SenatorialDistrict findByCode(String code);
    List<SenatorialDistrict> findByNameStartingWith(String name);
}
