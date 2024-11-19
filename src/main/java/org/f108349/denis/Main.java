package org.f108349.denis;

import org.f108349.denis.configuration.SessionFactoryUtil;
import org.f108349.denis.dao.CustomerDao;
import org.f108349.denis.entity.Customer;
import org.hibernate.Session;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Scanner scanner = new Scanner(System.in);
             
            System.out.println("Session is open: " + session.isOpen());

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
        } catch (Exception e) {
            e.printStackTrace();
        }    
    }
}
