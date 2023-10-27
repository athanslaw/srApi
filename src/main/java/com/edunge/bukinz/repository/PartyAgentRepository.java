package com.edunge.bukinz.repository;

import com.edunge.bukinz.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartyAgentRepository extends JpaRepository<PartyAgent, Long> {
    List<PartyAgent> findByFirstnameOrLastnameOrPhone(String firstname, String lastname, String phone);
    PartyAgent findByPhone(String phone);
    List<PartyAgent> findByWard(Ward ward);
    List<PartyAgent> findByLga(Lga lga);
    List<PartyAgent> findByPollingUnit(PollingUnit pollingUnit);
}
