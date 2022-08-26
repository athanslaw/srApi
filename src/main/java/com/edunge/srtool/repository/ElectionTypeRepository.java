package com.edunge.srtool.repository;

import com.edunge.srtool.model.ElectionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElectionTypeRepository extends JpaRepository<ElectionType, Long> {
    List<ElectionType> findByStatus(Boolean status);
}
