package org.f108349.denis;

import org.f108349.denis.ConsoleCommands.CompanyCc;
import org.f108349.denis.ConsoleCommands.CustomerCc;
import org.f108349.denis.ConsoleCommands.VehicleCc;
import org.f108349.denis.ConsoleCommands.VehicleTypeCc;
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
                System.out.println("3. Vehicle Type");
                System.out.println("4. Vehicle");
                System.out.println("6. Exit");
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
                        VehicleTypeCc.run(scanner);
                        break;
                    case "4":
                        VehicleCc.run(scanner);
                        break;
                    case "6":
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
