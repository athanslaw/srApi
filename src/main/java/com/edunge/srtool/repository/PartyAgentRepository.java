package com.edunge.srtool.repository;

import com.edunge.srtool.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartyAgentRepository extends JpaRepository<PartyAgent, Long> {
    List<PartyAgent> findByFirstnameOrLastname(String firstname, String lastname);
    PartyAgent findByPhone(String phone);
    List<PartyAgent> findByWard(Ward ward);
    List<PartyAgent> findByLga(Lga lga);
    List<PartyAgent> findByPollingUnit(PollingUnit pollingUnit);
}
