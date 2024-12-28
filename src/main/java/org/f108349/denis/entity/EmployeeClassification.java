package org.f108349.denis.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.grammars.hql.HqlParser;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "employee_classification")
public class EmployeeClassification {
    public EmployeeClassification() {}
    
    public EmployeeClassification(String classificationName) {
        this.id = UUID.randomUUID().toString();
        this.classificationName = classificationName;
        this.isDeleted = false;
    }
    
    @Id
    @Column(columnDefinition = "CHAR(36)")
    private String id;
    
    @NotBlank(message = "Classification cannot be blank.")
    @Size(max = 255, message = "Classification cannot exceed 255 characters.")
    @Column(name = "classification_name", nullable = false, unique = true)
    private String classificationName;
    
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @OneToMany(mappedBy = "employeeClassification", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
    private Set<Employee> employees = new HashSet<>();
    
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassificationName() {
        return this.classificationName;
    }

    public void setClassificationName(String classificationName) {
        this.classificationName = classificationName;
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }
    
    public Set<Employee> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }
    
    public static EmployeeClassification createTestClassification(int uniqueness) {
        return new EmployeeClassification("name" + uniqueness);
    }
}
