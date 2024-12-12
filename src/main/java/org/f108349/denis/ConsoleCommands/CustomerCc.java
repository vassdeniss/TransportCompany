package org.f108349.denis.ConsoleCommands;

import org.f108349.denis.configuration.SessionFactoryUtil;
import org.f108349.denis.dao.CustomerDao;
import org.f108349.denis.dto.CustomerDto;

import java.util.Scanner;

public class CustomerCc {
    public static void run(Scanner scanner) {
        CustomerDao dao = new CustomerDao(SessionFactoryUtil.getSessionFactory());
        MenuHandler handler = new MenuHandler(scanner);
        handler.addOption("1", "Save Customer", () -> saveCustomer(scanner, dao));
        handler.addOption("2", "Get Customer", () -> getCustomer(scanner, dao));
        handler.addOption("3", "Get All Customers", () -> getAllCustomers(dao));
        handler.addOption("4", "Update Customer", () -> updateCustomer(scanner, dao));
        handler.addOption("5", "Delete Customer", () -> deleteCustomer(scanner, dao));
        handler.addOption("6", "Back", () -> { });
        handler.run();
    }
    
    private static void saveCustomer(Scanner scanner, CustomerDao dao) {
        System.out.println("\n--- Save Customer ---");
        String firstName = ConsoleUtils.promptString(scanner, "Enter first name: ");
        String lastName = ConsoleUtils.promptString(scanner,"Enter last name: ");
        String email = ConsoleUtils.promptString(scanner, "Enter email: ");
        String phone = ConsoleUtils.promptString(scanner, "Enter phone number: ");
        String address = ConsoleUtils.promptString(scanner, "Enter address: ");

        CustomerDto customer = new CustomerDto(firstName, lastName, email, phone, address);
        dao.saveCustomer(customer);
        System.out.println("Customer saved successfully.");
    }

    private static void getCustomer(Scanner scanner, CustomerDao dao) {
        System.out.println("\n--- Get Customer ---");
        String customerId = ConsoleUtils.promptString(scanner, "Enter customer ID: ");
        
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
        String customerId = ConsoleUtils.promptString(scanner, "Enter customer ID: ");

        CustomerDto customer = dao.getCustomerByIdWhereNotDeleted(customerId);
        if (customer == null) {
            System.out.println("Customer not found with ID: " + customerId);
            return;
        }

        System.out.println("Customer found: " + customer.getFirstName() + " " + customer.getLastName());
        System.out.println("Which field do you want to update?");
        
        MenuHandler menuHandler = new MenuHandler(scanner);
        menuHandler.addOption("1", "First Name", () -> {
            System.out.println("Current first name: " + customer.getFirstName());
            customer.setFirstName(ConsoleUtils.promptString(scanner, "Enter new first name: "));
        });
        menuHandler.addOption("2", "Last Name", () -> {
            System.out.println("Current last name: " + customer.getLastName());
            customer.setLastName(ConsoleUtils.promptString(scanner, "Enter new last name: "));
        });
        menuHandler.addOption("3", "Email", () -> {
            System.out.println("Current email: " + customer.getEmail());
            customer.setEmail(ConsoleUtils.promptString(scanner, "Enter new email: "));
        });
        menuHandler.addOption("4", "Phone", () -> {
            System.out.println("Current phone: " + customer.getPhone());
            customer.setPhone(ConsoleUtils.promptString(scanner, "Enter new phone: "));
        });
        menuHandler.addOption("5", "Address", () -> {
            System.out.println("Current address: " + customer.getAddress());
            customer.setAddress(ConsoleUtils.promptString(scanner, "Enter new address: "));
        });
        
        menuHandler.run();
        
        dao.updateCustomer(customer);
        System.out.println("Customer updated successfully.");
    }
    
    private static void deleteCustomer(Scanner scanner, CustomerDao dao) {
        System.out.println("\n--- Delete Customer ---");
        String customerId = ConsoleUtils.promptString(scanner, "Enter customer ID: ");

        CustomerDto customer = dao.getCustomerByIdWhereNotDeleted(customerId);
        if (customer == null) {
            System.out.println("Customer not found with ID: " + customerId);
            return;
        }
        
        dao.deleteCustomer(customerId);
        System.out.println("Customer deleted successfully.");
    }
}
