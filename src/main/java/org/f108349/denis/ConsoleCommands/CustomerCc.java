package org.f108349.denis.ConsoleCommands;

import org.f108349.denis.dao.CustomerDao;
import org.f108349.denis.dto.CustomerDto;

import java.util.Scanner;

public class CustomerCc {
    public static void run(Scanner scanner) {
        CustomerDao dao = new CustomerDao();
        
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
                CustomerCc.saveCustomer(scanner, dao);
                break;
            case "2":
                CustomerCc.getCustomer(scanner, dao);
                break;
            case "3":
                CustomerCc.getAllCustomers(dao);
                break;
            case "4":
                CustomerCc.updateCustomer(scanner, dao);
                break;
            case "5":
                CustomerCc.deleteCustomer(scanner, dao);
                break;
            case "6":
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }   
    }
    
    private static void saveCustomer(Scanner scanner, CustomerDao dao) {
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

        CustomerDto customer = new CustomerDto(firstName, lastName, email, phone, address);
        dao.saveCustomer(customer);
        System.out.println("Customer saved successfully.");
    }

    private static void getCustomer(Scanner scanner, CustomerDao dao) {
        System.out.println("\n--- Get Customer ---");
        System.out.print("Enter customer ID: ");
        String customerId = scanner.nextLine();

        CustomerDto customer = dao.getCustomerByIdWhereNotDeleted(customerId);
        if (customer != null) {
            System.out.println(customer);
        } else {
            System.out.println("Customer not found with ID: " + customerId);
        }
    }
    
    private static void getAllCustomers(CustomerDao dao) {
        System.out.println("\n--- Get All Customers ---");
        dao.getAllCustomersWhereNotDeleted().forEach(System.out::println);
    }
    
    private static void updateCustomer(Scanner scanner, CustomerDao dao) {
        System.out.println("\n--- Update Customer ---");
        System.out.print("Enter customer ID: ");
        String customerId = scanner.nextLine();

        CustomerDto customer = dao.getCustomerByIdWhereNotDeleted(customerId);
        if (customer == null) {
            System.out.println("Customer not found with ID: " + customerId);
            return;
        }

        System.out.println("Customer found: " + customer.getFirstName() + " " + customer.getLastName());
        System.out.println("Which field do you want to update?");
        System.out.println("1. First Name");
        System.out.println("2. Last Name");
        System.out.println("3. Email");
        System.out.println("4. Phone");
        System.out.println("5. Address");
        System.out.print("Your choice: ");
        String fieldChoice = scanner.nextLine();

        String newValue;
        switch (fieldChoice) {
            case "1":
                System.out.println("Current First Name: " + customer.getFirstName());
                System.out.print("Enter new First Name: ");
                newValue = scanner.nextLine();
                customer.setFirstName(newValue);
                break;
            case "2":
                System.out.println("Current Last Name: " + customer.getLastName());
                System.out.print("Enter new Last Name: ");
                newValue = scanner.nextLine();
                customer.setLastName(newValue);
                break;
            case "3":
                System.out.println("Current Email: " + customer.getEmail());
                System.out.print("Enter new Email: ");
                newValue = scanner.nextLine();
                customer.setEmail(newValue);
                break;
            case "4":
                System.out.println("Current Phone: " + customer.getPhone());
                System.out.print("Enter new Phone: ");
                newValue = scanner.nextLine();
                customer.setPhone(newValue);
                break;
            case "5":
                System.out.println("Current Address: " + customer.getAddress());
                System.out.print("Enter new Address: ");
                newValue = scanner.nextLine();
                customer.setAddress(newValue);
                break;
            default:
                System.out.println("Invalid choice. No updates made.");
                return;
        }

        dao.updateCustomer(customer);
        System.out.println("Customer updated successfully.");
    }
    
    private static void deleteCustomer(Scanner scanner, CustomerDao dao) {
        System.out.println("\n--- Delete Customer ---");
        System.out.print("Enter customer ID: ");
        String customerId = scanner.nextLine();

        CustomerDto customer = dao.getCustomerByIdWhereNotDeleted(customerId);
        if (customer == null) {
            System.out.println("Customer not found with ID: " + customerId);
            return;
        }
        
        dao.deleteCustomer(customerId);
        System.out.println("Customer deleted successfully.");
    }
}
