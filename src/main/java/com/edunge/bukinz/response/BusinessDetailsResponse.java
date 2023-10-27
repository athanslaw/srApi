package com.edunge.bukinz.response;

import com.edunge.bukinz.dto.BusinessDetailsDto;
import com.edunge.bukinz.model.BusinessDetails;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusinessDetailsResponse extends BaseResponse{
    BusinessDetails businessDetails;
    List<BusinessDetails> businessDetailsList;
    List<BusinessDetailsDto> businessDetailsDtoList;
    BusinessDetailsDto businessDetailsDto;
    Integer count;

    public Integer getCount() {
        return count;
    }
    public BusinessDetailsResponse(String code, String message, BusinessDetails businessDetails) {
        super(code, message);
        this.businessDetails = businessDetails;
    }

    public BusinessDetailsResponse(String code, String message, List<BusinessDetailsDto> businessDetailsDtoList) {
        super(code, message);
        this.businessDetailsDtoList = businessDetailsDtoList;
    }

    public BusinessDetailsResponse(String code, String message, BusinessDetailsDto businessDetailsDto) {
        super(code, message);
        this.businessDetailsDto = businessDetailsDto;
    }

    public BusinessDetailsResponse(BusinessDetails businessDetails) {
        this.businessDetails = businessDetails;
    }

    public BusinessDetailsResponse(String code, String message) {
        super(code, message);
    }


    public BusinessDetails getBusinessDetails() {
        return businessDetails;
    }

    public void setBusinessDetails(BusinessDetails businessDetails) {
        this.businessDetails = businessDetails;
    }

    public List<BusinessDetails> getBusinessDetailsList() {
        return businessDetailsList;
    }

    public void setBusinessDetailsList(List<BusinessDetails> businessDetailsList) {
        this.businessDetailsList = businessDetailsList;
    }

    public List<BusinessDetailsDto> getBusinessDetailsDtoList() {
        return businessDetailsDtoList;
    }

    public void setBusinessDetailsDtoList(List<BusinessDetailsDto> businessDetailsDtoList) {
        this.businessDetailsDtoList = businessDetailsDtoList;
    }

    public BusinessDetailsDto getBusinessDetailsDto() {
        return businessDetailsDto;
    }

    public void setBusinessDetailsDto(BusinessDetailsDto businessDetailsDto) {
        this.businessDetailsDto = businessDetailsDto;
    }

}
