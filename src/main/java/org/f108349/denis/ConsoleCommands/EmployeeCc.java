package org.f108349.denis.ConsoleCommands;

import org.f108349.denis.configuration.SessionFactoryUtil;
import org.f108349.denis.dao.EmployeeDao;
import org.f108349.denis.dto.EmployeeDto;

import java.util.Scanner;

public class EmployeeCc {
    public static void run(Scanner scanner) {
        EmployeeDao dao = new EmployeeDao(SessionFactoryUtil.getSessionFactory());
        MenuHandler handler = new MenuHandler(scanner);
        handler.addOption("1", "Save Employee", () -> saveEmployee(scanner, dao));
        handler.addOption("2", "Get Employee", () -> getEmployee(scanner, dao));
        handler.addOption("3", "Get All Employees", () -> getAllEmployees(dao));
        handler.addOption("4", "Update Employee", () -> updateEmployee(scanner, dao));
        handler.addOption("5", "Delete Employee", () -> deleteEmployee(scanner, dao));
        handler.addOption("6", "Back", () -> { });
        handler.run();
    }
    
    private static void saveEmployee(Scanner scanner, EmployeeDao dao) {
        System.out.println("\n--- Save Employee ---");
        
        String firstName = ConsoleUtils.promptString(scanner, "Enter first name: ");
        String lastName = ConsoleUtils.promptString(scanner, "Enter last name: ");
        String email = ConsoleUtils.promptString(scanner, "Enter email: ");
        String phone = ConsoleUtils.promptString(scanner, "Enter phone: ");
        double salary = ConsoleUtils.promptDouble(scanner, "Enter salary: ");
        String classificationId = ConsoleUtils.promptString(scanner, "Enter classification ID: ");
        String companyId = ConsoleUtils.promptString(scanner, "Enter company ID: ");

        EmployeeDto employee = new EmployeeDto(firstName, lastName, email, phone, salary, companyId, classificationId);
        boolean saved = dao.saveEmployee(employee);
        if (saved) {
            System.out.println("Employee saved successfully.");
        }
    }

    private static void getEmployee(Scanner scanner, EmployeeDao dao) {
        System.out.println("\n--- Get Employee ---");
        String employeeId = ConsoleUtils.promptString(scanner, "Enter employee ID: ");

        EmployeeDto employee = dao.getEmployeeByIdWhereNotDeleted(employeeId);
        if (employee != null) {
            System.out.println(employee);
        } else {
            System.out.println("Employee not found with ID: " + employeeId);
        }
    }
    
    private static void getAllEmployees(EmployeeDao dao) {
        System.out.println("\n--- Get All Employees ---");
        dao.getAllEmployeesWhereNotDeleted().forEach(System.out::println);
    }
    
    private static void updateEmployee(Scanner scanner, EmployeeDao dao) {
        System.out.println("\n--- Update Employee ---");
        String employeeId = ConsoleUtils.promptString(scanner, "Enter employee ID: ");

        EmployeeDto employee = dao.getEmployeeByIdWhereNotDeleted(employeeId);
        if (employee == null) {
            System.out.println("Employee not found with ID: " + employeeId);
            return;
        }

        System.out.println("Employee found: " + employee.getFirstName() + " " + employee.getLastName());
        System.out.println("Which field do you want to update?");
        
        MenuHandler menuHandler = new MenuHandler(scanner);
        menuHandler.addOption("1", "First name", () -> {
            System.out.println("Current first name: " + employee.getFirstName());
            employee.setFirstName(ConsoleUtils.promptString(scanner, "Enter new first name: "));
        });
        menuHandler.addOption("2", "Last name", () -> {
            System.out.println("Current last name: " + employee.getLastName());
            employee.setLastName(ConsoleUtils.promptString(scanner, "Enter new last name: "));
        });
        menuHandler.addOption("3", "Email", () -> {
            System.out.println("Current email: " + employee.getEmail());
            employee.setEmail(ConsoleUtils.promptString(scanner, "Enter new email: "));
        });
        menuHandler.addOption("4", "Phone", () -> {
            System.out.println("Current phone: " + employee.getPhone());
            employee.setPhone(ConsoleUtils.promptString(scanner, "Enter new phone: "));
        });
        menuHandler.addOption("5", "Hire date", () -> {
            System.out.println("Current hire date: " + employee.getHireDate());
            employee.setHireDate(ConsoleUtils.promptDate(scanner, "Enter new hire date (yyyy-MM-dd): "));
        });
        menuHandler.addOption("6", "Salary", () -> {
            System.out.println("Current salary: " + employee.getSalary());
            employee.setSalary(ConsoleUtils.promptDouble(scanner, "Enter new salary: "));
        });
        menuHandler.addOption("7", "Update company ID", () -> {
            System.out.println("Current company ID: " + employee.getCompanyId());
            employee.setCompanyId(ConsoleUtils.promptString(scanner, "Enter new company ID: "));
        });
        menuHandler.addOption("8", "Update classification ID", () -> {
            System.out.println("Current classification ID: " + employee.getEmployeeClassificationId());
            employee.setEmployeeClassificationId(ConsoleUtils.promptString(scanner, "Enter new classification ID: "));
        });
        
        menuHandler.run();

        dao.updateEmployee(employee);
        System.out.println("Employee updated successfully.");
    }
    
    private static void deleteEmployee(Scanner scanner, EmployeeDao dao) {
        System.out.println("\n--- Delete Employee ---");
        String employeeId = ConsoleUtils.promptString(scanner, "Enter employee ID: ");

        EmployeeDto employee = dao.getEmployeeByIdWhereNotDeleted(employeeId);
        if (employee == null) {
            System.out.println("Employee not found with ID: " + employeeId);
            return;
        }
        
        dao.deleteEmployee(employeeId);
        System.out.println("Employee deleted successfully.");
    }
}
