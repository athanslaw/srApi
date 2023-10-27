package com.edunge.bukinz.repository;

import com.edunge.bukinz.model.SenatorialDistrict;
import com.edunge.bukinz.model.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SenatorialDistrictRepository extends JpaRepository<SenatorialDistrict, Long> {
    SenatorialDistrict findByCode(String code);
    List<SenatorialDistrict> findByNameStartingWith(String name);
    List<SenatorialDistrict> findByState(State state);
    long countByState(State state);
}
