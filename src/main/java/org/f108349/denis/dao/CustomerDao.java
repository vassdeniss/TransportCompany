package org.f108349.denis.dao;

import org.f108349.denis.dto.CustomerDto;
import org.f108349.denis.entity.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
 
public class CustomerDao extends BaseDao<CustomerDto, Customer> {
    private final SessionFactory sessionFactory;
    
    public CustomerDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    public void saveCustomer(CustomerDto customerDto) {
        Customer customer = new Customer(customerDto.getFirstName(), customerDto.getLastName(), 
                customerDto.getEmail(), customerDto.getPhone(), customerDto.getAddress());
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(customer);
            tx.commit();
        }
    }
    
    public CustomerDto getCustomerByIdWhereNotDeleted(String id) {
        CustomerDto customer;
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            customer = this.getByIdWhereNotDeleted(session, id, CustomerDto.class, Customer.class);
            tx.commit();
        }
        
        return customer;
    }
    
    public List<CustomerDto> getAllCustomersWhereNotDeleted() {
        List<CustomerDto> customers;
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            customers = this.getAllWhereNotDeleted(session, CustomerDto.class, Customer.class);
            tx.commit();
        }
        
        return customers;
    }
    
    public void updateCustomer(CustomerDto customerDto) {
        try (Session session = this.sessionFactory.openSession()) {
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
    
    public void deleteCustomer(String id) {
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Customer customer = session.get(Customer.class, id);
            customer.setDeleted(true);
            session.merge(customer);
            tx.commit();
        }
    }
}
