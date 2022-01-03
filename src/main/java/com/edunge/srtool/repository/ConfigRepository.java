package com.edunge.srtool.repository;

import com.edunge.srtool.model.Config;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepository extends JpaRepository<Config, Long> {
}
