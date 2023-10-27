package com.edunge.bukinz.repository;

import com.edunge.bukinz.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessDetailsRepository extends JpaRepository<BusinessDetails, Long> {
    List<BusinessDetails> findByPhone(String phone);
    List<BusinessDetails> findByCity(String City);
    List<BusinessDetails> findByPostalCode(String postalCode);
    List<BusinessDetails> findByBusinessName(String businessName);
}
