package com.edunge.bukinz.repository;

import com.edunge.bukinz.model.GeoPoliticalZone;
import com.edunge.bukinz.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {
    State findByCode(String code);
    List<State> findByGeoPoliticalZone(GeoPoliticalZone zoneId);
    State findByNameStartingWith(String name);
    State findByDefaultState(Boolean defaultState);

    Long countByGeoPoliticalZone(GeoPoliticalZone geoPoliticalZone);
}
