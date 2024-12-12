package org.f108349.denis.dao;

import org.f108349.denis.configuration.SessionFactoryUtil;
import org.f108349.denis.dto.CustomerDto;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class CustomerDaoIntegrationTests {
    private static SessionFactory sessionFactory;
    private CustomerDao customerDao;
    
    @Container
    private static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb-customer")
            .withUsername("user")
            .withPassword("password");
    
    @BeforeAll
    static void setupAll() {
        Properties hibernateProps = new Properties();
        hibernateProps.setProperty("hibernate.connection.url", mysql.getJdbcUrl());
        hibernateProps.setProperty("hibernate.connection.username", mysql.getUsername());
        hibernateProps.setProperty("hibernate.connection.password", mysql.getPassword());
        hibernateProps.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        hibernateProps.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        hibernateProps.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        
        sessionFactory = SessionFactoryUtil.getSessionFactory(hibernateProps);
    }
    
    @BeforeEach
    void setup() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery("DELETE FROM Customer").executeUpdate();
            tx.commit();
        }
        
        this.customerDao = new CustomerDao(sessionFactory);
    }
    
    @AfterAll
    static void afterAll() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
    
    @Test
    public void testCustomerDao_whenCustomerSaved_thenShouldBeAbleToRetrieveIt() {
        // Arrange
        CustomerDto dto = new CustomerDto("John", "Doe", "john.doe@example.com", "+359 88 1234567", "123 Main St");

        // Act
        this.customerDao.saveCustomer(dto);
        List<CustomerDto> customers = this.customerDao.getAllCustomersWhereNotDeleted();

        // Assert
        assertEquals(1, customers.size(), "Should have one customer after saving.");
        CustomerDto retrieved = customers.getFirst();
        assertEquals("John", retrieved.getFirstName());
        assertEquals("Doe", retrieved.getLastName());
        assertEquals("john.doe@example.com", retrieved.getEmail());
        assertEquals("+359 88 1234567", retrieved.getPhone());
        assertEquals("123 Main St", retrieved.getAddress());
    }
    
    @Test
    public void testCustomerDao_whenCustomerUpdated_thenChangesShouldPersist() {
        // Arrange
        CustomerDto dto = new CustomerDto("Jane", "Smith", "jane.smith@example.com", "+359 88 7654321", "456 Elm St");
        this.customerDao.saveCustomer(dto);

        CustomerDto saved = this.customerDao.getAllCustomersWhereNotDeleted().getFirst();
        String customerId = saved.getId();

        // Act
        saved.setFirstName("Emily");
        saved.setLastName("Johnson");
        saved.setEmail("emily.johnson@example.com");
        saved.setPhone("+359 88 9876543");
        saved.setAddress("789 Oak St");
        this.customerDao.updateCustomer(saved);

        CustomerDto updated = this.customerDao.getCustomerByIdWhereNotDeleted(customerId);

        // Assert
        assertEquals("Emily", updated.getFirstName());
        assertEquals("Johnson", updated.getLastName());
        assertEquals("emily.johnson@example.com", updated.getEmail());
        assertEquals("+359 88 9876543", updated.getPhone());
        assertEquals("789 Oak St", updated.getAddress());
    }
    
    @Test
    public void testCustomerDao_whenCustomerDeleted_thenShouldNotBeRetrievable() {
        // Arrange
        CustomerDto dto = new CustomerDto("Mark", "Taylor", "mark.taylor@example.com", "+359 88 1122334", "123 Pine St");
        this.customerDao.saveCustomer(dto);

        CustomerDto saved = this.customerDao.getAllCustomersWhereNotDeleted().get(0);
        String customerId = saved.getId();

        // Act
        this.customerDao.deleteCustomer(customerId);

        // Assert
        List<CustomerDto> customers = this.customerDao.getAllCustomersWhereNotDeleted();
        assertTrue(customers.isEmpty(), "Should have no customers after deletion.");

        CustomerDto deleted = this.customerDao.getCustomerByIdWhereNotDeleted(customerId);
        assertNull(deleted, "Deleted customer should not be retrievable.");
    }
}
