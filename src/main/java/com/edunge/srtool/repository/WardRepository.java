package com.edunge.srtool.repository;

import com.edunge.srtool.model.Lga;
import com.edunge.srtool.model.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WardRepository extends JpaRepository<Ward, Long> {
    Ward findByCode(String wardCode);
    List<Ward> findByNameStartingWith(String name);
    List<Ward> findByLga(Lga lga);
}
