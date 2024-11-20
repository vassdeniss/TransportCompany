package org.f108349.denis;

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
                System.out.println("4. Exit");
                System.out.print("Your choice: ");
                
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        saveCustomer(scanner);
                        break;
                    case "2":
                        getCustomer(scanner);
                        break;
                    case "3":
                        getAllCustomers();
                        break;
                    case "4":
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
    
    private static void saveCustomer(Scanner scanner) {
        System.out.println("\n--- Save Customer ---");
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();

        System.out.print("Enter address: ");
        String address = scanner.nextLine();

        Customer customer = new Customer(firstName, lastName, email, phone, address);
        CustomerDao.saveCustomer(customer);
        System.out.println("Customer saved successfully.");
    }

    private static void getCustomer(Scanner scanner) {
        System.out.println("\n--- Get Customer ---");
        System.out.print("Enter customer ID: ");
        String customerId = scanner.nextLine();

        Customer customer = CustomerDao.getCustomerById(customerId);
        if (customer != null) {
            System.out.println(customer);
        } else {
            System.out.println("Customer not found with ID: " + customerId);
        }
    }
    
    private static void getAllCustomers() {
        System.out.println("\n--- Get All Customers ---");

        List<Customer> customers = CustomerDao.getAllCustomers();
        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }
}
