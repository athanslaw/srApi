package com.edunge.srtool.repository;

import com.edunge.srtool.model.GeoPoliticalZone;
import com.edunge.srtool.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {
    State findByCode(String code);
    List<State> findByGeoPoliticalZone(Long code);
    State findByNameStartingWith(String name);
    State findByDefaultState(Boolean defaultState);

    Long countByGeoPoliticalZone(GeoPoliticalZone geoPoliticalZone);
}
