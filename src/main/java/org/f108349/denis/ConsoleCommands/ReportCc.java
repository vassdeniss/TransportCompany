package org.f108349.denis.ConsoleCommands;

import org.f108349.denis.configuration.SessionFactoryUtil;
import org.f108349.denis.dao.*;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class ReportCc {
    public static void run(Scanner scanner) {
        ReportDao reportDao = new ReportDao();
        CompanyDao companyDao = new CompanyDao(SessionFactoryUtil.getSessionFactory());
        MenuHandler handler = new MenuHandler(scanner);
        handler.addOption("1", "Get Companies Total Orders", () -> getCompanyInfoForOrdersMade(reportDao, companyDao));
        handler.run();
    }
    
    private static void getCompanyInfoForOrdersMade(ReportDao reportDao, CompanyDao companyDao) {
        List<Object[]> reports = companyDao.getAllCompanyOrders();
        System.out.println(reportDao.getCompanyInfoForOrdersMade(reports));
    }
}
