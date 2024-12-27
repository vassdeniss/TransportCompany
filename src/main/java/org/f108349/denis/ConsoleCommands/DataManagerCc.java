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
        handler.addOption("5", "Orders by Destination", () -> getOrderByDestination(scanner, dao));
        handler.addOption("6", "Orders by Cost", () -> getOrdersByCost(scanner, dao));
        handler.addOption("7", "Back", () -> { });
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
    
    public static void getOrderByDestination(Scanner scanner, DataManagerDao dao) {
        String orderFiler = ConsoleUtils.promptString(scanner, "Enter an order filter (or press Enter to skip): ");
        String order = ConsoleUtils.promptString(scanner, "Enter order (asc or desc): ");
        
        dao.getOrderByDestination(orderFiler, order).forEach(System.out::println);
    }
    
    public static void getOrdersByCost(Scanner scanner, DataManagerDao dao) {
        Double minCost = ConsoleUtils.promptDouble(scanner, "Enter minimum cost (or leave blank for no minimum): ");
        Double maxCost = ConsoleUtils.promptDouble(scanner, "Enter maximum cost (or leave blank for no maximum): ");
        String order = ConsoleUtils.promptString(scanner, "Enter order (asc or desc): ");
        
        dao.getOrdersByCost(minCost, maxCost, order).forEach(System.out::println);
    }
}
