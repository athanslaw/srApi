package com.edunge.srtool.repository;

import com.edunge.srtool.model.Authority;
import com.edunge.srtool.model.AuthorityName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByName(AuthorityName authorityName);

}
