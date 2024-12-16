package org.f108349.denis.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.f108349.denis.entity.validation.Phone;

import java.util.UUID;

@Entity
@Table(name = "employee")
public class Employee {
    public Employee() {}
    
    public Employee(String firstName, String lastName, String email, String phone, 
                    java.sql.Date hireDate) {
        this.id = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.hireDate = hireDate;
        this.isDeleted = false;
    }
    
    @Id
    @Column(columnDefinition = "CHAR(36)")
    private String id;

    @Size(max = 255, message = "First name cannot exceed 255 characters.")
    @Pattern(regexp = "^([A-Z]).*", message = "First name hast to start with a capital letter.")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Size(max = 255, message = "Last name cannot exceed 255 characters.")
    @Pattern(regexp = "^([A-Z]).*", message = "Last name hast to start with a capital letter.")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Email(message = "Please provide a valid email address.")
    @Column(nullable = false)
    private String email;

    @Phone
    @Column
    private String phone;

    @PastOrPresent(message = "Hire date cannot be in the future.")
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
    
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

    public java.sql.Date getHireDate() {
        return this.hireDate;
    }

    public void setHireDate(java.sql.Date hireDate) {
        this.hireDate = hireDate;
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public EmployeeClassification getEmployeeClassification() {
        return this.employeeClassification;
    }

    public void setEmployeeClassification(EmployeeClassification employeeClassification) {
        this.employeeClassification = employeeClassification;
    }
}
