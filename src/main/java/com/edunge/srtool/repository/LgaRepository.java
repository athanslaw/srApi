package com.edunge.srtool.repository;

import com.edunge.srtool.model.Lga;
import com.edunge.srtool.model.SenatorialDistrict;
import com.edunge.srtool.model.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LgaRepository extends JpaRepository<Lga, Long> {
    Lga findByCode(String lgaCode);
    List<Lga> findByNameStartingWith(String name);
    List<Lga> findByState(State state);
    List<Lga> findBySenatorialDistrict(SenatorialDistrict senatorialDistrict);
}
