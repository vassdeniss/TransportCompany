package org.f108349.denis.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "company")
public class Company {
    public Company() {}
    
    public Company(String name, String registrationNo, String email, String phone) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.registrationNo = registrationNo;
        this.email = email;
        this.phone = phone;
        this.isDeleted = false;
    }
    
    @Id
    @Column(columnDefinition = "CHAR(36)")
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(name = "registration_no", nullable = false, unique = true)
    private String registrationNo;

    @Column(nullable = false)
    private String email;

    @Column
    private String phone;

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

    public boolean isDeleted() {
        return this.isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
