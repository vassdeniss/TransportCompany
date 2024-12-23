package org.f108349.denis;

import org.f108349.denis.ConsoleCommands.*;
import org.f108349.denis.configuration.SessionFactoryUtil;
import org.hibernate.Session;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Session ignored = SessionFactoryUtil.getSessionFactory().openSession()) {
            Scanner scanner = new Scanner(System.in);
            boolean isRunning = true;

            System.out.println("Welcome to Transport Company App");

            while (isRunning) {
                System.out.println("\nPlease select an option:");
                System.out.println("1. Customer");
                System.out.println("2. Company");
                System.out.println("3. Employee");
                System.out.println("4. Employee Classification");
                System.out.println("5. Vehicle");
                System.out.println("6. Vehicle Type");
                System.out.println("7. Order");
                System.out.println("8. Data Manager");
                System.out.println("9. Exit");
                System.out.print("Your choice: ");
                
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        CustomerCc.run(scanner);
                        break;
                    case "2":
                        CompanyCc.run(scanner);
                        break;
                    case "3":
                        EmployeeCc.run(scanner);
                        break;
                    case "4":
                        EmployeeClassificationCc.run(scanner);
                        break;
                    case "5":
                        VehicleCc.run(scanner);
                        break;
                    case "6":
                        VehicleTypeCc.run(scanner);
                        break;
                    case "7":
                        OrderCc.run(scanner);
                        break;
                    case "8":
                        DataManagerCc.run(scanner);
                        break;
                    case "9":
                        isRunning = false;
                        System.out.println("Exiting application...");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }    
    }
}
