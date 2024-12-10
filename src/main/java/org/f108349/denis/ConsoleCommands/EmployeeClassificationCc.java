package org.f108349.denis.ConsoleCommands;

import org.f108349.denis.dao.EmployeeClassificationDao;
import org.f108349.denis.dto.EmployeeClassificationDto;

import java.util.Scanner;

public class EmployeeClassificationCc {
    public static void run(Scanner scanner) {
        EmployeeClassificationDao dao = new EmployeeClassificationDao();
        MenuHandler handler = new MenuHandler(scanner);
        handler.addOption("1", "Save Classification", () -> saveEmployeeClassification(scanner, dao));
        handler.addOption("2", "Get Classification", () -> getEmployeeClassification(scanner, dao));
        handler.addOption("3", "Get All Classifications", () -> getAllEmployeeClassifications(dao));
        handler.addOption("4", "Delete Classification", () -> deleteEmployeeClassification(scanner, dao));
        handler.addOption("5", "Back", () -> { });
        handler.run();\
    }
    
    private static void saveEmployeeClassification(Scanner scanner, EmployeeClassificationDao dao) {
        System.out.println("\n--- Save Employee Classification ---");
        String classificationName = ConsoleUtils.promptString(scanner, "Enter classification name: ");

        EmployeeClassificationDto employeeClassification = new EmployeeClassificationDto(classificationName);
        dao.saveEmployeeClassification(employeeClassification);
        System.out.println("Employee Classification saved successfully.");
    }

    private static void getEmployeeClassification(Scanner scanner, EmployeeClassificationDao dao) {
        System.out.println("\n--- Get Employee Classification ---");
        String employeeClassificationId = ConsoleUtils.promptString(scanner, "Enter classification ID: "); 

        EmployeeClassificationDto employeeClassification = 
                dao.getEmployeeClassificationByIdWhereNotDeleted(employeeClassificationId);
        if (employeeClassification != null) {
            System.out.println(employeeClassification);
        } else {
            System.out.println("Employee Classification not found with ID: " + employeeClassificationId);
        }
    }
    
    private static void getAllEmployeeClassifications(EmployeeClassificationDao dao) {
        System.out.println("\n--- Get All Employee Classifications ---");
        dao.getAllEmployeeClassificationsWhereNotDeleted().forEach(System.out::println);
    }
    
    private static void deleteEmployeeClassification(Scanner scanner, EmployeeClassificationDao dao) {
        System.out.println("\n--- Delete Employee Classification ---");
        String employeeClassificationId = ConsoleUtils.promptString(scanner, "Enter classification ID: ");
        
        EmployeeClassificationDto employeeClassification = dao.getEmployeeClassificationByIdWhereNotDeleted(employeeClassificationId);
        if (employeeClassification == null) {
            System.out.println("Employee classification not found with ID: " + employeeClassificationId);
            return;
        }

        try {
            dao.deleteEmployeeClassification(employeeClassificationId);
            System.out.println("Employee classification deleted successfully.");
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}
