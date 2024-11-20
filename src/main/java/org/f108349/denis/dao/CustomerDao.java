package org.f108349.denis.dao;

import org.f108349.denis.configuration.SessionFactoryUtil;
import org.f108349.denis.entity.Customer;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Set;

public class CustomerDao {
    public static void saveCustomer(Customer customer) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(customer);
            tx.commit();
        }
    }
    
    public static Customer getCustomerById(String id) {
        Customer customer;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            customer = session.get(Customer.class, id);
            tx.commit();
        }
        return customer;
    }
    
    public static List<Customer> getAllCustomers() {
        List<Customer> customers;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            customers = session
                    .createQuery("select c from Customer c", Customer.class)
                    .getResultList();
            tx.commit();
        }
        return customers;
    }
    
    public static void updateCustomer(Customer customer) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(customer);
            tx.commit();
        }
    }
}
