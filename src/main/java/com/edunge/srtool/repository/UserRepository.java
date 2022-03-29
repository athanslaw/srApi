package com.edunge.srtool.repository;

import com.edunge.srtool.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findByPhone(String phone);
}
