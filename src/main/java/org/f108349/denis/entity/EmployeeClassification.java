package org.f108349.denis.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "employee_classification")
public class EmployeeClassification {
    @Id
    @Column(columnDefinition = "CHAR(36)")
    private String id;
    
    @Column(name = "classification_name", nullable = false, unique = true)
    private String classificationName;
    
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
}
