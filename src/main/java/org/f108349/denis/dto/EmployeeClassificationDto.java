package org.f108349.denis.dto;

import org.f108349.denis.entity.EmployeeClassification;

public class EmployeeClassificationDto {
    private String id;
    private String classificationName;
    
    public EmployeeClassificationDto(String classificationName) {
        this.classificationName = classificationName;
    }
    
    public EmployeeClassificationDto(EmployeeClassification employeeClassification) {
        this.id = employeeClassification.getId();
        this.classificationName = employeeClassification.getClassificationName();
    }

    public String getId() {
        return this.id;
    }
    
    public String getClassificationName() {
        return this.classificationName;
    }

    public void setClassificationName(String classificationName) {
        this.classificationName = classificationName;
    }

    @Override
    public String toString() {
        return "Employee classification " + this.classificationName;
    }
}
