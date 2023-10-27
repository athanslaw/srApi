package com.edunge.bukinz.repository;

import com.edunge.bukinz.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailAndRoleNot(String email, String role);
    List<User> findByPhoneAndRoleNot(String phone, String role);
    User findByPhoneAndRole(String phone, String role);
    User findByPhone(String phone);
    User findByEmail(String email);
    List<User> findByLgaIdAndRoleNot(String lgaId, String role);
    List<User> findByStateIdAndRoleNot(Long stateId, String role);
    List<User> findByRole(String role);
    List<User> findByRoleNot(String role);
    List<User> findByFirstnameOrLastnameOrPhone(String fname, String lname, String phone);
}
