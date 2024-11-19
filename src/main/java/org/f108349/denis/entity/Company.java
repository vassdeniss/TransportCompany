package org.f108349.denis.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "company")
public class Company {
    @Id
    @Column(columnDefinition = "CHAR(36)")
    private UUID id;

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
}
