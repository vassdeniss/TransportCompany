package org.f108349.denis.dto;

import org.f108349.denis.entity.Company;

public class CompanyDto {
    private String id;
    private String name;
    private String registrationNo;
    private String email;
    private String phone;

    public CompanyDto(String name, String registrationNo, String email, String phone) {
        this.name = name;
        this.registrationNo = registrationNo;
        this.email = email;
        this.phone = phone;
    }
    
    public CompanyDto(Company company) {
        this.id = company.getId();
        this.name = company.getName();
        this.registrationNo = company.getRegistrationNo();
        this.email = company.getEmail();
        this.phone = company.getPhone();
    }

    public String getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistrationNo() {
        return this.registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Information about " + this.name + ":" + 
               "\n    Name is '" + this.name + '\'' + 
               "\n    Registration No is '" + this.registrationNo + '\'' + 
               "\n    Email is '" + this.email + '\'' + 
               "\n    Phone Number is '" + this.phone + '\'';
    }
}