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
        EmployeeDao employeeDao = new EmployeeDao(SessionFactoryUtil.getSessionFactory());
        OrderDao orderDao = new OrderDao(SessionFactoryUtil.getSessionFactory());
        MenuHandler handler = new MenuHandler(scanner);
        handler.addOption("1", "Get Companies Total Orders", () -> getCompanyInfoForOrdersMade(reportDao, companyDao));
        handler.addOption("2", "Get Employees Total Orders", () -> getEmployeeInfoForOrdersMade(reportDao, employeeDao));
        handler.addOption("3", "Get Companies Income by Date", () -> getCompanyIncomeForDateRange(scanner, reportDao, orderDao));
        handler.addOption("4", "Back", () -> { });
        handler.run();
    }
    
    private static void getCompanyInfoForOrdersMade(ReportDao reportDao, CompanyDao companyDao) {
        List<Object[]> reports = companyDao.getAllCompanyOrders();
        System.out.println(reportDao.getCompanyInfoForOrdersMade(reports));
    }
    
    private static void getEmployeeInfoForOrdersMade(ReportDao reportDao, EmployeeDao employeeDao) {
        List<Object[]> reports = employeeDao.getAllEmployeeOrders();
        System.out.println(reportDao.getEmployeeInfoForOrdersMade(reports));
    }
    
    private static void getCompanyIncomeForDateRange(Scanner scanner, ReportDao reportDao, OrderDao orderDao) {
        Date startDate = ConsoleUtils.promptDate(scanner, "Enter start date: ");
        Date endDate = ConsoleUtils.promptDate(scanner, "Enter end date: ");

        List<Object[]> report = orderDao.getTotalIncomeForGivenTimePeriod(startDate, endDate);
        System.out.println(reportDao.getCompanyIncomeForDateRange(report));
    }
}
