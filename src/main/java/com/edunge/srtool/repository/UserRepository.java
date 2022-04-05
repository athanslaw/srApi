package com.edunge.srtool.repository;

import com.edunge.srtool.model.Lga;
import com.edunge.srtool.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findByPhone(String phone);
    List<User> findByLgaId(String lgaId);
    List<User> findByFirstnameOrLastnameOrPhone(String fname, String lname, String phone);
}
