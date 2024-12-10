package org.f108349.denis.ConsoleCommands;

import org.f108349.denis.dao.CompanyDao;
import org.f108349.denis.dto.CompanyDto;

import java.util.Scanner;

public class CompanyCc {
    public static void run(Scanner scanner) {
        CompanyDao dao = new CompanyDao();
        MenuHandler handler = new MenuHandler(scanner);
        handler.addOption("1", "Save Company", () -> saveCompany(scanner, dao));
        handler.addOption("2", "Get Company", () -> getCompany(scanner, dao));
        handler.addOption("3", "Get All Companies", () -> getAllCompanies(dao));
        handler.addOption("4", "Update Company", () -> updateCompany(scanner, dao));
        handler.addOption("5", "Delete Company", () -> deleteCompany(scanner, dao));
        handler.addOption("6", "Back", () -> { });
        handler.run();
    }
    
    private static void saveCompany(Scanner scanner, CompanyDao dao) {
        System.out.println("\n--- Save Company ---");
        String name = ConsoleUtils.promptString(scanner, "Enter name: ");
        String registrationNo = ConsoleUtils.promptString(scanner, "Enter registration no: ");
        String email = ConsoleUtils.promptString(scanner, "Enter email: ");
        String phone = ConsoleUtils.promptString(scanner, "Enter phone number: ");

        CompanyDto company = new CompanyDto(name, registrationNo, email, phone);
        dao.saveCompany(company);
        System.out.println("Company saved successfully.");
    }

    private static void getCompany(Scanner scanner, CompanyDao dao) {
        System.out.println("\n--- Get Company ---");
        String companyId = ConsoleUtils.promptString(scanner, "Enter company ID: ");

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
        String companyId = ConsoleUtils.promptString(scanner, "Enter company ID: ");

        CompanyDto company = dao.getCompanyByIdWhereNotDeleted(companyId);
        if (company == null) {
            System.out.println("Company not found with ID: " + companyId);
            return;
        }

        System.out.println("Company found: " + company.getName());
        System.out.println("Which field do you want to update?");
        
        MenuHandler menuHandler = new MenuHandler(scanner);
        menuHandler.addOption("1", "Name", () -> {
            System.out.println("Current Name: " + company.getName());
            company.setName(ConsoleUtils.promptString(scanner, "Enter new name: "));
        });
        menuHandler.addOption("2", "Registration No", () -> {
            System.out.println("Current registration number: " + company.getRegistrationNo());
            company.setRegistrationNo(ConsoleUtils.promptString(scanner, "Enter new registration number: "));
        });
        menuHandler.addOption("3", "Email", () -> {
            System.out.println("Current email: " + company.getEmail());
            company.setEmail(ConsoleUtils.promptString(scanner, "Enter new email: "));
        });
        menuHandler.addOption("4", "Phone", () -> {
            System.out.println("Current phone: " + company.getPhone());
            company.setPhone(ConsoleUtils.promptString(scanner, "Enter phone: "));
        });

        menuHandler.run();

        dao.updateCompany(company);
        System.out.println("Company updated successfully.");
    }
    
    private static void deleteCompany(Scanner scanner, CompanyDao dao) {
        System.out.println("\n--- Delete Company ---");
        String companyId = ConsoleUtils.promptString(scanner, "Enter company ID: ");

        CompanyDto customer = dao.getCompanyByIdWhereNotDeleted(companyId);
        if (customer == null) {
            System.out.println("Company not found with ID: " + companyId);
            return;
        }
        
        dao.deleteCompany(companyId);
        System.out.println("Company deleted successfully.");
    }
}
