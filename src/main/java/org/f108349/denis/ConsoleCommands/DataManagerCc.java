package org.f108349.denis.ConsoleCommands;

import org.f108349.denis.configuration.SessionFactoryUtil;
import org.f108349.denis.dao.DataManagerDao;

import java.util.Scanner;

public class DataManagerCc {
    public static void run(Scanner scanner) {
        DataManagerDao dao = new DataManagerDao(SessionFactoryUtil.getSessionFactory());
        MenuHandler handler = new MenuHandler(scanner);
        handler.addOption("1", "Companies by Name", () -> getCompaniesByName(scanner, dao));
        handler.addOption("2", "Companies by Income", () -> getCompaniesByIncome(scanner, dao));
        handler.addOption("3", "Employees by Classification", () -> getEmployeesByClassification(scanner, dao));
        handler.addOption("4", "Employees by Salary", () -> getEmployeesBySalary(scanner, dao));
        handler.addOption("5", "Back", () -> { });
        handler.run();
    }
    
    private static void getCompaniesByName(Scanner scanner, DataManagerDao dao) {
        String nameFilter = ConsoleUtils.promptString(scanner, "Enter a name filter (or press Enter to skip): ");
        String order = ConsoleUtils.promptString(scanner, "Enter sorting order (asc or desc): ");
        
        dao.getCompaniesByName(nameFilter, order).forEach(System.out::println);
    }
    
    private static void getCompaniesByIncome(Scanner scanner, DataManagerDao dao) {
        Double minIncome = ConsoleUtils.promptDouble(scanner, "Enter minimum income (or leave blank for no minimum): ");
        Double maxIncome = ConsoleUtils.promptDouble(scanner, "Enter maximum income (or leave blank for no maximum): ");
        String order = ConsoleUtils.promptString(scanner, "Enter order (asc or desc): ");
        
        dao.getCompaniesByIncome(minIncome, maxIncome, order).forEach(System.out::println);
    }
    
    private static void getEmployeesByClassification(Scanner scanner, DataManagerDao dao) {
        String classificationFilter = ConsoleUtils.promptString(scanner, "Enter a classification filter (or press Enter to skip): ");
        String order = ConsoleUtils.promptString(scanner, "Enter order (asc or desc): ");
        
        dao.getEmployeesByClassification(classificationFilter, order).forEach(System.out::println);
    }
    
    private static void getEmployeesBySalary(Scanner scanner, DataManagerDao dao) {
        Double minSalary = ConsoleUtils.promptDouble(scanner, "Enter minimum salary (or leave blank for no minimum): ");
        Double maxSalary = ConsoleUtils.promptDouble(scanner, "Enter maximum salary (or leave blank for no maximum): ");
        String order = ConsoleUtils.promptString(scanner, "Enter order (asc or desc): ");
        
        dao.getEmployeesBySalary(minSalary, maxSalary, order).forEach(System.out::println);
    }
}
