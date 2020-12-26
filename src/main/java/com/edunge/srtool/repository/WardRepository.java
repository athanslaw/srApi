package com.edunge.srtool.repository;

import com.edunge.srtool.model.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WardRepository extends JpaRepository<Ward, Long> {
    Ward findByCode(String wardCode);
    Ward findByNameStartingWith(String name);
}
