package org.f108349.denis;

import org.f108349.denis.ConsoleCommands.CustomerCc;
import org.f108349.denis.configuration.SessionFactoryUtil;
import org.f108349.denis.dao.CustomerDao;
import org.f108349.denis.entity.Customer;
import org.hibernate.Session;

import java.io.LineNumberInputStream;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Session ignored = SessionFactoryUtil.getSessionFactory().openSession()) {
            Scanner scanner = new Scanner(System.in);
            boolean isRunning = true;

            System.out.println("Welcome to Transport Company App");

            while (isRunning) {
                System.out.println("\nPlease select an option:");
                System.out.println("1. Save Customer");
                System.out.println("2. Get Customer");
                System.out.println("3. Get All Customers");
                System.out.println("4. Update Customer");
                System.out.println("5. Delete Customer");
                System.out.println("6. Exit");
                System.out.print("Your choice: ");
                
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        CustomerCc.saveCustomer(scanner);
                        break;
                    case "2":
                        CustomerCc.getCustomer(scanner);
                        break;
                    case "3":
                        CustomerCc.getAllCustomers();
                        break;
                    case "4":
                        CustomerCc.updateCustomer(scanner);
                        break;
                    case "5":
                        CustomerCc.deleteCustomer(scanner);
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
