package org.f108349.denis.dao;

import java.util.List;

public class ReportDao {
    public String getCompanyInfoForOrdersMade(List<Object[]> companyInfo) {
        StringBuilder info = new StringBuilder();
        for (Object[] objects : companyInfo) {
            info.append("Company ")
                    .append(objects[0])
                    .append(" made ")
                    .append(objects[1])
                    .append(" orders earning ")
                    .append(objects[2])
                    .append(".\n");
        }
        
        return info.toString();
    }
    
    public String getEmployeeInfoForOrdersMade(List<Object[]> employeeInfo) {
        StringBuilder info = new StringBuilder();
        for (Object[] objects : employeeInfo) {
            info.append("Employee ")
                    .append(objects[0])
                    .append(" (employed by ")
                    .append(objects[1])
                    .append(")")
                    .append(" is assigned to ")
                    .append(objects[2])
                    .append(" orders and made ")
                    .append(objects[3])
                    .append(".\n");
        }
        
        return info.toString();
    }
}
