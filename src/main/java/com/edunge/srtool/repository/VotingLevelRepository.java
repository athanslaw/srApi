package com.edunge.srtool.repository;

import com.edunge.srtool.model.VotingLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotingLevelRepository extends JpaRepository<VotingLevel,Long> {
    VotingLevel findByCode(String code);
}
