package org.f108349.denis.dto;

import org.f108349.denis.entity.Customer;

public class CustomerDto {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    
    public CustomerDto(String firstName, String lastName, String email, String phone, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }
    
    public CustomerDto(Customer customer) {
        this.id = customer.getId();
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
        this.email = customer.getEmail();
        this.phone = customer.getPhone();
        this.address = customer.getAddress();
    }

    public String getId() {
        return this.id;
    }
    
    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Information about " + this.firstName + " " + this.lastName + ":" + 
               "\n    First Name is '" + this.firstName + '\'' + 
               "\n    Last Name is '" + this.lastName + '\'' + 
               "\n    Email is '" + this.email + '\'' + 
               "\n    Phone Number is '" + this.phone + '\'' + 
               "\n    Address is '" + this.address + '\'';
    }
}
