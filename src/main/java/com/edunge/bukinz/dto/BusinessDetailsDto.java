package com.edunge.bukinz.dto;

import com.edunge.bukinz.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;

public class BusinessDetailsDto extends BaseModel {
    private String phone;
    private String businessName;
    private String lastname;
    @Column(unique = true)
    private String email;
    private String role;
    private String businessAddress;
    private String city;
    private String postalCode;
    private String serviceLine; // The service the business renders e.g barbing
    private String serviceOpeningHours;
    private String serviceClosingTime;
    private Integer cancellationPeriodNumber; // 1 day 2 hours, etc.
    private String cancellationPeriodType; // days, hours, minutes
    private String staffEmails;
    private String noOfStaffAttendingToCustomers;
    private Integer stage;
    @JsonIgnore
    private String pwd;

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPwd() {
        return pwd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getServiceLine() {
        return serviceLine;
    }

    public void setServiceLine(String serviceLine) {
        this.serviceLine = serviceLine;
    }

    public String getServiceOpeningHours() {
        return serviceOpeningHours;
    }

    public void setServiceOpeningHours(String serviceOpeningHours) {
        this.serviceOpeningHours = serviceOpeningHours;
    }

    public String getServiceClosingTime() {
        return serviceClosingTime;
    }

    public void setServiceClosingTime(String serviceClosingTime) {
        this.serviceClosingTime = serviceClosingTime;
    }

    public Integer getCancellationPeriodNumber() {
        return cancellationPeriodNumber;
    }

    public void setCancellationPeriodNumber(Integer cancellationPeriodNumber) {
        this.cancellationPeriodNumber = cancellationPeriodNumber;
    }

    public String getCancellationPeriodType() {
        return cancellationPeriodType;
    }

    public void setCancellationPeriodType(String cancellationPeriodType) {
        this.cancellationPeriodType = cancellationPeriodType;
    }

    public String getStaffEmails() {
        return staffEmails;
    }

    public void setStaffEmails(String staffEmails) {
        this.staffEmails = staffEmails;
    }

    public String getNoOfStaffAttendingToCustomers() {
        return noOfStaffAttendingToCustomers;
    }

    public void setNoOfStaffAttendingToCustomers(String noOfStaffAttendingToCustomers) {
        this.noOfStaffAttendingToCustomers = noOfStaffAttendingToCustomers;
    }
}
