package com.edunge.bukinz.service.impl;

import com.edunge.bukinz.dto.BusinessDetailsDto;
import com.edunge.bukinz.dto.UserDto;
import com.edunge.bukinz.exceptions.DuplicateException;
import com.edunge.bukinz.exceptions.NotFoundException;
import com.edunge.bukinz.model.*;
import com.edunge.bukinz.repository.*;
import com.edunge.bukinz.response.BusinessDetailsResponse;
import com.edunge.bukinz.service.BusinessDetailsService;
import com.edunge.bukinz.service.FileProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BusinessDetailsServiceImpl implements BusinessDetailsService {

    private final BusinessDetailsRepository businessDetailsRepository;
    private final UserServiceImpl userService;

    private static final String SERVICE_NAME = "Business Details";
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessDetailsService.class);

    @Autowired
    FileProcessingService fileProcessingService;

    @Value("${notfound.message.template}")
    private String notFoundTemplate;

    @Value("${success.message.template}")
    private String successTemplate;

    @Value("${duplicate.message.template}")
    private String duplicateTemplate;

    @Value("${update.message.template}")
    private String updateTemplate;

    @Value("${delete.message.template}")
    private String deleteTemplate;

    @Value("${fetch.message.template}")
    private String fetchRecordTemplate;

    @Autowired
    public BusinessDetailsServiceImpl(BusinessDetailsRepository businessDetailsRepository, UserServiceImpl userService) {
        this.businessDetailsRepository = businessDetailsRepository;
        this.userService = userService;
    }

    private BusinessDetails buildBusinessDetailsObj(BusinessDetails businessDetails, BusinessDetailsDto businessDetailsDto){
        if(businessDetailsDto.getBusinessName() != null) businessDetails.setBusinessName(businessDetailsDto.getBusinessName());
        if(businessDetailsDto.getBusinessAddress() != null) businessDetails.setBusinessAddress(businessDetailsDto.getBusinessAddress());
        if(businessDetailsDto.getCity() != null) businessDetails.setCity(businessDetails.getCity());
        if(businessDetailsDto.getCancellationPeriodNumber() != null) businessDetails.setCancellationPeriodNumber(businessDetailsDto.getCancellationPeriodNumber());
        if(businessDetailsDto.getCancellationPeriodType() != null) businessDetails.setCancellationPeriodType(businessDetailsDto.getCancellationPeriodType());
        if(businessDetailsDto.getPhone() != null) businessDetails.setPhone(businessDetailsDto.getPhone());
        if(businessDetailsDto.getEmail() != null) businessDetails.setEmail(businessDetailsDto.getEmail());
        if(businessDetailsDto.getRole() != null) businessDetails.setRole(businessDetailsDto.getRole());
        if(businessDetailsDto.getPostalCode() != null) businessDetails.setPostalCode(businessDetailsDto.getPostalCode());
        if(businessDetailsDto.getServiceClosingTime() != null) businessDetails.setServiceClosingTime(businessDetailsDto.getServiceClosingTime());
        if(businessDetailsDto.getServiceLine() != null) businessDetails.setServiceLine(businessDetailsDto.getServiceLine());
        if(businessDetailsDto.getServiceOpeningHours() != null) businessDetails.setServiceOpeningHours(businessDetailsDto.getServiceOpeningHours());
        if(businessDetailsDto.getNoOfStaffAttendingToCustomers() != null) businessDetails.setNoOfStaffAttendingToCustomers(businessDetailsDto.getNoOfStaffAttendingToCustomers());
        businessDetails.setStage(businessDetailsDto.getStage());
        return businessDetails;
    }
    @Override
    public BusinessDetailsResponse saveBusinessDetails(BusinessDetailsDto businessDetailsDto) throws NotFoundException {

        List<BusinessDetails> businessDetails = businessDetailsRepository.findByPhone(businessDetailsDto.getPhone());
        if(businessDetails.isEmpty()){
            BusinessDetails businessDetail = this.buildBusinessDetailsObj(new BusinessDetails(), businessDetailsDto);

            // save in users table too
            userService.saveBusiness(businessDetailsToUserDto(businessDetail, businessDetailsDto.getPwd()));

            return new BusinessDetailsResponse("00", String.format(successTemplate, SERVICE_NAME), businessDetail);
        }
        throw new DuplicateException(String.format(duplicateTemplate, "Matching record"));
    }

    private UserDto businessDetailsToUserDto(BusinessDetails businessDetails, String pwd){
        UserDto userDto = new UserDto();
        userDto.setEmail(businessDetails.getEmail());
        userDto.setFirstname(businessDetails.getBusinessName());
        userDto.setLastname("");
        userDto.setPhone(businessDetails.getPhone());
        userDto.setPassword(pwd);
        userDto.setRole("business");
        return userDto;
    }

    @Override
    public BusinessDetailsResponse findBusinessDetailsById(Long id) throws NotFoundException {
        BusinessDetails businessDetails = getBusinessDetails(id);
        return new BusinessDetailsResponse("00", String.format(fetchRecordTemplate,SERVICE_NAME), businessDetails);
    }
    private BusinessDetailsDto mapToBusinessDetailsDta(BusinessDetails businessDetail, BusinessDetailsDto businessDetailsDto){
        businessDetailsDto.setBusinessAddress(businessDetail.getBusinessAddress());
        businessDetailsDto.setBusinessName(businessDetail.getBusinessName());
        businessDetailsDto.setCity(businessDetail.getCity());
        businessDetailsDto.setCancellationPeriodNumber(businessDetail.getCancellationPeriodNumber());
        businessDetailsDto.setPostalCode(businessDetail.getPostalCode());
        businessDetailsDto.setServiceLine(businessDetail.getServiceLine());
        businessDetailsDto.setCancellationPeriodType(businessDetail.getCancellationPeriodType());
        businessDetailsDto.setNoOfStaffAttendingToCustomers(businessDetail.getNoOfStaffAttendingToCustomers());
        businessDetailsDto.setServiceClosingTime(businessDetail.getServiceClosingTime());
        businessDetailsDto.setServiceOpeningHours(businessDetail.getServiceOpeningHours());
        businessDetailsDto.setStaffEmails(businessDetail.getStaffEmails());
        businessDetailsDto.setEmail(businessDetail.getEmail());
        businessDetailsDto.setEmail(businessDetail.getEmail());
        businessDetailsDto.setPhone(businessDetail.getPhone());
        businessDetailsDto.setId(businessDetail.getId());
        businessDetailsDto.setRole(businessDetail.getRole());
        businessDetailsDto.setStage(businessDetail.getStage());
        return businessDetailsDto;
    }

    @Override
    public BusinessDetailsResponse findBusinessDetailsByBusinessName(String businessName) throws NotFoundException {
        List<BusinessDetails> businessDetails = businessDetailsRepository.findByBusinessName(businessName);
        List<BusinessDetailsDto> businessDetailsDtoList = new ArrayList<>();
        businessDetails.forEach(businessDetail->{
            BusinessDetailsDto businessDetailsDto = this.mapToBusinessDetailsDta(businessDetail, new BusinessDetailsDto());
            businessDetailsDtoList.add(businessDetailsDto);
        });
        if(businessDetails.isEmpty()){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return new BusinessDetailsResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), businessDetailsDtoList);
    }
    @Override
    public BusinessDetailsResponse findBusinessDetailsByCity(String city) throws NotFoundException {
        List<BusinessDetails> businessDetails = businessDetailsRepository.findByCity(city);
        List<BusinessDetailsDto> businessDetailsDtoList = new ArrayList<>();
        businessDetails.forEach(businessDetail->{
            BusinessDetailsDto businessDetailsDto = this.mapToBusinessDetailsDta(businessDetail, new BusinessDetailsDto());
            businessDetailsDtoList.add(businessDetailsDto);
        });
        if(businessDetails.isEmpty()){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return new BusinessDetailsResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), businessDetailsDtoList);
    }

    @Override
    public BusinessDetailsResponse findBusinessDetailsByPostalCode(String postalCode) throws NotFoundException {
        List<BusinessDetails> businessDetails = businessDetailsRepository.findByPostalCode(postalCode);
        List<BusinessDetailsDto> businessDetailsDtoList = new ArrayList<>();
        businessDetails.forEach(businessDetail->{
            BusinessDetailsDto businessDetailsDto = this.mapToBusinessDetailsDta(businessDetail, new BusinessDetailsDto());
            businessDetailsDtoList.add(businessDetailsDto);
        });
        if(businessDetails.isEmpty()){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return new BusinessDetailsResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), businessDetailsDtoList);
    }

    @Override
    public BusinessDetailsResponse findBusinessDetailsByPhone(String phone) throws NotFoundException {
        List<BusinessDetails> businessDetail = businessDetailsRepository.findByPhone(phone);
        if(businessDetail.isEmpty()){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        BusinessDetailsDto businessDetailsDto = this.mapToBusinessDetailsDta(businessDetail.get(0), new BusinessDetailsDto());
        return new BusinessDetailsResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), businessDetailsDto);
    }

    @Override
    public BusinessDetailsResponse findAll() throws NotFoundException {
        List<BusinessDetails> businessDetails = businessDetailsRepository.findAll();
        List<BusinessDetailsDto> businessDetailsDtoList = new ArrayList<>();
        businessDetails.stream().forEach(businessDetail->{
            BusinessDetailsDto businessDetailsDto = this.mapToBusinessDetailsDta(businessDetail, new BusinessDetailsDto());
            businessDetailsDtoList.add(businessDetailsDto);
        });
        if(businessDetails==null){
            throw new NotFoundException(String.format(notFoundTemplate,SERVICE_NAME));
        }
        return new BusinessDetailsResponse("00", String.format(fetchRecordTemplate, SERVICE_NAME), businessDetailsDtoList);
    }

    @Override
    public BusinessDetailsResponse updateBusinessDetails(Long id, BusinessDetailsDto businessDetailsDto) throws NotFoundException {
        BusinessDetails businessDetails = getBusinessDetails(id);
        businessDetails = this.buildBusinessDetailsObj(businessDetails, businessDetailsDto);
        businessDetailsRepository.save(businessDetails);
        userService.saveBusiness(businessDetailsToUserDto(businessDetails, businessDetailsDto.getPwd()));
        return new BusinessDetailsResponse("00", String.format(successTemplate, SERVICE_NAME), businessDetails);
    }

    @Override
    public BusinessDetailsResponse deleteBusinessDetailsById(Long id) throws NotFoundException {
        BusinessDetails businessDetails = getBusinessDetails(id);
        businessDetailsRepository.deleteById(id);
        return new BusinessDetailsResponse("00",String.format(deleteTemplate,businessDetails.getEmail()));
    }

    private BusinessDetails getBusinessDetails(Long id) throws NotFoundException {
        Optional<BusinessDetails> businessDetails = businessDetailsRepository.findById(id);
        if(!businessDetails.isPresent()){
            throw new NotFoundException("Party agent not found.");
        }
        return businessDetails.get();
    }


    private void saveBusinessDetails(String businessName,
                                String email,
                                String phoneNumber,
                                String address,
                                String password,
                                String role)  {

        List<BusinessDetails> businessDetails = businessDetailsRepository.findByPhone(phoneNumber);

        try{
            if(businessDetails.isEmpty()){
                BusinessDetails businessDetail = new BusinessDetails();
                businessDetail.setEmail(email);
                businessDetail.setBusinessAddress(address);
                businessDetail.setPhone(phoneNumber);
                businessDetail.setBusinessName(businessName);

                businessDetail.setRole(role);
                businessDetailsRepository.save(businessDetail);

                userService.saveBusiness(businessDetailsToUserDto(businessDetail, password));
            }
        }
        catch (Exception ex){
            LOGGER.info("Business information could not be saved");
        }
    }

    private BusinessDetailsResponse processUpload(List<String> lines){
        for (String line:lines) {
            String[] state = line.split(",");
            saveBusinessDetails(state[0].trim(), state[1].trim(), state[2].trim(), state[3].trim(),state[4].trim(),state[5].trim());
        }
        return new BusinessDetailsResponse("00", "File Uploaded.");
    }
}
