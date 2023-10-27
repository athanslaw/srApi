package com.edunge.bukinz.repository;

import com.edunge.bukinz.model.Authority;
import com.edunge.bukinz.model.AuthorityName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByName(AuthorityName authorityName);
}
