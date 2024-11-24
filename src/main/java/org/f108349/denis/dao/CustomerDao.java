package org.f108349.denis.dao;

import org.f108349.denis.configuration.SessionFactoryUtil;
import org.f108349.denis.dto.CustomerDto;
import org.f108349.denis.entity.Customer;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
 
public class CustomerDao {
    public static void saveCustomer(CustomerDto customerDto) {
        Customer customer = new Customer(customerDto.getFirstName(), customerDto.getLastName(), 
                customerDto.getEmail(), customerDto.getPhone(), customerDto.getAddress());
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(customer);
            tx.commit();
        }
    }
    
    public static CustomerDto getCustomerById(String id) {
        Customer customer;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            customer = session
                    .createQuery("select c from Customer c where c.isDeleted = false", Customer.class)
                    .getSingleResult();
            tx.commit();
        }
        
        if (customer == null) {
            return null;
        }
        
        return new CustomerDto(customer);
    }
    
    public static List<CustomerDto> getAllCustomers() {
        List<CustomerDto> customers;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            customers = session
                    .createQuery("select new org.f108349.denis.dto.CustomerDto(c) " +
                            "from Customer c where c.isDeleted = false", CustomerDto.class)
                    .getResultList();
            tx.commit();
        }
        
        return customers;
    }
    
    public static void updateCustomer(CustomerDto customerDto) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Customer customer = session.get(Customer.class, customerDto.getId());
            
            customer.setFirstName(customerDto.getFirstName());
            customer.setLastName(customerDto.getLastName());
            customer.setEmail(customerDto.getEmail());
            customer.setPhone(customerDto.getPhone());
            customer.setAddress(customerDto.getAddress());
            
            session.merge(customer);
            tx.commit();
        }
    }
    
    public static void deleteCustomer(String id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Customer customer = session.get(Customer.class, id);
            customer.setDeleted(true);
            session.merge(customer);
            tx.commit();
        }
    }
}
