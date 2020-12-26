package com.edunge.srtool.repository;

import com.edunge.srtool.model.SenatorialDistrict;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SenatorialDistrictRepository extends JpaRepository<SenatorialDistrict, Long> {
    SenatorialDistrict findByCode(String code);
    SenatorialDistrict findByNameLike(String name);
}
