package com.edunge.bukinz.repository;

import com.edunge.bukinz.model.Lga;
import com.edunge.bukinz.model.SenatorialDistrict;
import com.edunge.bukinz.model.State;
import com.edunge.bukinz.model.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WardRepository extends JpaRepository<Ward, Long> {
    Ward findByCode(String wardCode);
    List<Ward> findByNameStartingWithOrderByCodeAsc(String name);
    List<Ward> findByLgaOrderByCodeAsc(Lga lga);
    List<Ward> findByLga(Lga lga);
    List<Ward> findByState(State state);
    long countByState(State state);
    long countByLga(Lga lga);
    long countBySenatorialDistrict(SenatorialDistrict senatorialDistrict);
    List<Ward> findBySenatorialDistrict(SenatorialDistrict senatorialDistrict);
}
