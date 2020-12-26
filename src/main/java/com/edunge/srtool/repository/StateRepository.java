package com.edunge.srtool.repository;

import com.edunge.srtool.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {
    State findByCode(String code);
    State findByNameLike(String name);
}
