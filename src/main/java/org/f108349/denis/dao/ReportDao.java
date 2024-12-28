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
}
