package org.f108349.denis.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import org.f108349.denis.entity.validation.Phone;
import org.f108349.denis.entity.validation.RegistrationNumber;

import java.util.UUID;

@Entity
@Table(name = "company")
public class Company {
    public Company() {}
    
    public Company(String name, String registrationNo, String email, String phone, double income) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.registrationNo = registrationNo;
        this.email = email;
        this.phone = phone;
        this.income = income;
        this.isDeleted = false;
    }
    
    @Id
    @Column(columnDefinition = "CHAR(36)")
    private String id;

    @Size(min = 3, max = 255, message = "Company name has to be between 3 and 255 characters.")
    @Pattern(regexp = "^([A-Z]).*", message = "Company cannot be blank and has to start with a capital letter.")
    @Column(nullable = false)
    private String name;
    
    @RegistrationNumber
    @Column(name = "registration_no", nullable = false, unique = true)
    private String registrationNo;

    @Email(message = "Please provide a valid email address.")
    @Column(nullable = false)
    private String email;

    @Phone
    @Column
    private String phone;

    @NotNull(message = "Income cannot be null.")
    @Min(value = 0, message = "Income must be a positive value.")
    @Column(nullable = false)
    private double income;
    
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

    public double getIncome() {
        return this.income;
    }
    
    public void setIncome(double income) {
        this.income = income;
    }
    
    public boolean isDeleted() {
        return this.isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
