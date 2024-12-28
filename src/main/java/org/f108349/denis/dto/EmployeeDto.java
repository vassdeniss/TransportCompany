package org.f108349.denis.dto;

import org.f108349.denis.entity.Employee;

import java.sql.Date;
import java.time.LocalDate;

public class EmployeeDto {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Date hireDate;
    private double salary;
    private boolean isDeleted;
    private String companyId;
    private CompanyDto company;
    private String employeeClassificationId;
    private EmployeeClassificationDto employeeClassification;

    public EmployeeDto() {}
    
    public EmployeeDto(String firstName, String lastName, String email, String phone, double salary, 
                       String companyId, String employeeClassificationId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.hireDate = Date.valueOf(LocalDate.now());
        this.salary = salary;
        this.companyId = companyId;
        this.employeeClassificationId = employeeClassificationId;
    }

    public EmployeeDto(Employee employee) {
        this.id = employee.getId();
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.email = employee.getEmail();
        this.phone = employee.getPhone();
        this.hireDate = employee.getHireDate();
        this.salary = employee.getSalary();
        this.isDeleted = employee.isDeleted();
        this.companyId = employee.getCompany() != null ? employee.getCompany().getId() : null;
        this.employeeClassificationId = employee.getEmployeeClassification() != null ? employee.getEmployeeClassification().getId() : null;
        this.company = new CompanyDto(employee.getCompany());
        this.employeeClassification = new EmployeeClassificationDto(employee.getEmployeeClassification());
    }

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

    public Date getHireDate() {
        return this.hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }
    
    public double getSalary() {
        return this.salary;
    }
    
    public void setSalary(double salary) {
        this.salary = salary;
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public CompanyDto getCompany() {
        return this.company;
    }

    public void setCompany(CompanyDto company) {
        this.company = company;
    }

    public String getEmployeeClassificationId() {
        return this.employeeClassificationId;
    }

    public void setEmployeeClassificationId(String employeeClassificationId) {
        this.employeeClassificationId = employeeClassificationId;
    }

    public EmployeeClassificationDto getEmployeeClassification() {
        return this.employeeClassification;
    }

    public void setEmployeeClassification(EmployeeClassificationDto employeeClassification) {
        this.employeeClassification = employeeClassification;
    }

    @Override
    public String toString() {
        return "Information about employee " + this.firstName + " " + this.lastName + ":" +
               "\n    Email: '" + this.email + '\'' +
               "\n    Phone: '" + this.phone + '\'' +
               "\n    Hire Date: '" + this.hireDate + '\'' +
               "\n    Company: '" + (this.company != null ? this.company.getName() : "N/A") + '\'' +
               "\n    Classification: '" + (this.employeeClassification != null ? this.employeeClassification.getClassificationName() : "N/A") + '\'';
    }
}
