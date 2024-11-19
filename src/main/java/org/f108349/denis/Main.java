package org.f108349.denis;

import org.f108349.denis.configuration.SessionFactoryUtil;
import org.f108349.denis.dao.CustomerDao;
import org.f108349.denis.entity.Customer;
import org.hibernate.Session;

public class Main {
    public static void main(String[] args) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            System.out.println("Session is open: " + session.isOpen());

            Customer customer = new Customer(
                "John", 
                "Doe", 
                "john.doe@example.com", 
                "123-456-7890", 
                "123 Main Street, Anytown, USA"
            );

            CustomerDao.saveCustomer(customer);
        } catch (Exception e) {
            e.printStackTrace();
        }    
    }
}
