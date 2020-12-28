package com.edunge.srtool.repository;

import com.edunge.srtool.model.PartyAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartyAgentRepository extends JpaRepository<PartyAgent, Long> {
    PartyAgent findByEmail(String email);
    List<PartyAgent> findByFirstnameOrLastname(String firstname, String lastname);
    PartyAgent findByPhone(String phone);
}
