package com.edunge.bukinz.service;

import com.edunge.bukinz.dto.BusinessDetailsDto;
import com.edunge.bukinz.exceptions.NotFoundException;
import com.edunge.bukinz.response.BusinessDetailsResponse;

public interface BusinessDetailsService {
    BusinessDetailsResponse saveBusinessDetails(BusinessDetailsDto businessDetailsDto) throws NotFoundException;
    BusinessDetailsResponse findBusinessDetailsById(Long id) throws NotFoundException;
    BusinessDetailsResponse updateBusinessDetails(Long id, BusinessDetailsDto businessDetailsDto) throws NotFoundException;
    BusinessDetailsResponse deleteBusinessDetailsById(Long id) throws NotFoundException;
    BusinessDetailsResponse findBusinessDetailsByBusinessName(String businessName)throws NotFoundException;
    BusinessDetailsResponse findBusinessDetailsByCity(String city) throws NotFoundException;
    BusinessDetailsResponse findBusinessDetailsByPostalCode(String postalCode) throws NotFoundException;
    BusinessDetailsResponse findBusinessDetailsByPhone(String phone)throws NotFoundException;
    BusinessDetailsResponse findAll() throws NotFoundException;
}
