package org.f108349.denis.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @Column(columnDefinition = "CHAR(36)")
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column
    private String phone;

    @Column(name = "hire_date")
    private java.sql.Date hireDate;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
    
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = true, foreignKey = @ForeignKey(name = "FK_company_employee"))
    private Company company;

    @ManyToOne
    @JoinColumn(name = "employee_classification_id", nullable = true, foreignKey = @ForeignKey(name = "FK_employee_classification"))
    private EmployeeClassification employeeClassification;
}
