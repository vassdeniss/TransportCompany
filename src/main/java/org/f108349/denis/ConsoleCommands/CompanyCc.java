package org.f108349.denis.ConsoleCommands;

import org.f108349.denis.dao.CompanyDao;
import org.f108349.denis.dto.CompanyDto;

import java.util.Scanner;

public class CompanyCc {
    public static void run(Scanner scanner) {
        CompanyDao dao = new CompanyDao();
        
        System.out.println("\nPlease select an option:");
        System.out.println("1. Save Company");
        System.out.println("2. Get Company");
        System.out.println("3. Get All Company");
        System.out.println("4. Update Company");
        System.out.println("5. Delete Company");
        System.out.println("6. Exit");
        System.out.print("Your choice: ");
        
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                CompanyCc.saveCompany(scanner, dao);
                break;
            case "2":
                CompanyCc.getCompany(scanner, dao);
                break;
            case "3":
                CompanyCc.getAllCompanies(dao);
                break;
            case "4":
                CompanyCc.updateCompany(scanner, dao);
                break;
            case "5":
                CompanyCc.deleteCompany(scanner, dao);
                break;
            case "6":
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }   
    }
    
    private static void saveCompany(Scanner scanner, CompanyDao dao) {
        System.out.println("\n--- Save Company ---");
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter registration no: ");
        String registrationNo = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();

        CompanyDto company = new CompanyDto(name, registrationNo, email, phone);
        dao.saveCompany(company);
        System.out.println("Company saved successfully.");
    }

    private static void getCompany(Scanner scanner, CompanyDao dao) {
        System.out.println("\n--- Get Company ---");
        System.out.print("Enter company ID: ");
        String companyId = scanner.nextLine();

        CompanyDto company = dao.getCompanyByIdWhereNotDeleted(companyId);
        if (company != null) {
            System.out.println(company);
        } else {
            System.out.println("Company not found with ID: " + companyId);
        }
    }
    
    private static void getAllCompanies(CompanyDao dao) {
        System.out.println("\n--- Get All Companies ---");
        dao.getAllCompaniesWhereNotDeleted().forEach(System.out::println);
    }
    
    private static void updateCompany(Scanner scanner, CompanyDao dao) {
        System.out.println("\n--- Update Company ---");
        System.out.print("Enter company ID: ");
        String companyId = scanner.nextLine();

        CompanyDto company = dao.getCompanyByIdWhereNotDeleted(companyId);
        if (company == null) {
            System.out.println("Company not found with ID: " + companyId);
            return;
        }

        System.out.println("Company found: " + company.getName());
        System.out.println("Which field do you want to update?");
        System.out.println("1. Name");
        System.out.println("2. Registration No");
        System.out.println("3. Email");
        System.out.println("4. Phone");
        System.out.print("Your choice: ");
        String fieldChoice = scanner.nextLine();

        String newValue;
        switch (fieldChoice) {
            case "1":
                System.out.println("Current Name: " + company.getName());
                System.out.print("Enter new Name: ");
                newValue = scanner.nextLine();
                company.setName(newValue);
                break;
            case "2":
                System.out.println("Current Registration No: " + company.getRegistrationNo());
                System.out.print("Enter new Registration No: ");
                newValue = scanner.nextLine();
                company.setRegistrationNo(newValue);
                break;
            case "3":
                System.out.println("Current Email: " + company.getEmail());
                System.out.print("Enter new Email: ");
                newValue = scanner.nextLine();
                company.setEmail(newValue);
                break;
            case "4":
                System.out.println("Current Phone: " + company.getPhone());
                System.out.print("Enter new Phone: ");
                newValue = scanner.nextLine();
                company.setPhone(newValue);
                break;
            default:
                System.out.println("Invalid choice. No updates made.");
                return;
        }

        dao.updateCompany(company);
        System.out.println("Company updated successfully.");
    }
    
    private static void deleteCompany(Scanner scanner, CompanyDao dao) {
        System.out.println("\n--- Delete Company ---");
        System.out.print("Enter company ID: ");
        String companyId = scanner.nextLine();

        CompanyDto customer = dao.getCompanyByIdWhereNotDeleted(companyId);
        if (customer == null) {
            System.out.println("Customer not found with ID: " + companyId);
            return;
        }
        
        dao.deleteCompany(companyId);
        System.out.println("Customer deleted successfully.");
    }
}
