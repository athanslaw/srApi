package com.edunge.srtool.repository;

import com.edunge.srtool.model.Lga;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LgaRepository extends JpaRepository<Lga, Long> {
    Lga findByCode(String lgaCode);
}
