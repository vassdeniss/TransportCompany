package org.f108349.denis.dao;

import org.f108349.denis.configuration.SessionFactoryUtil;
import org.f108349.denis.dto.EmployeeDto;
import org.f108349.denis.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeDaoTests {
    private static SessionFactory sessionFactory;
    private EmployeeDao employeeDao;
    private EmployeeDto employeeDto;

    @BeforeAll
    static void beforeAll() {
        Properties hibernateProps = new Properties();
        hibernateProps.setProperty("hibernate.connection.url", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        hibernateProps.setProperty("hibernate.connection.username", "sa");
        hibernateProps.setProperty("hibernate.connection.password", " ");
        hibernateProps.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        hibernateProps.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        hibernateProps.setProperty("hibernate.hbm2ddl.auto", "create-drop");

        sessionFactory = SessionFactoryUtil.getSessionFactory(hibernateProps);
    }

    @BeforeEach
    void setup() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery("DELETE FROM Order").executeUpdate();
            session.createMutationQuery("DELETE FROM Employee").executeUpdate();
            session.createMutationQuery("DELETE FROM Company").executeUpdate();
            session.createMutationQuery("DELETE FROM EmployeeClassification").executeUpdate();

            Company company = new Company("Test", "123456789", 
                    "testemail@gmail.com", "+359 88 2221111", 1200);
            String companyId = company.getId();
            session.persist(company);

            EmployeeClassification employeeClassification = new EmployeeClassification("test");
            String classificationId = employeeClassification.getId();
            session.persist(employeeClassification);

            tx.commit();
            
            this.employeeDto = new EmployeeDto("John", "Doe", "john.doe@example.com", "+359 88 1234567", 
                1300, companyId, classificationId);
        }

        this.employeeDao = new EmployeeDao(sessionFactory);
    }

    @AfterAll
    static void afterAll() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
    
    @Test
    public void testGetAllEmployeeOrders_whenOrdersExist_thenShouldRetrieveAggregatedData() {
        // Arrange
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            Customer customer = Customer.createTestCustomer(1);
            session.persist(customer);
            
            Company company = Company.createTestCompany(1);
            session.persist(company);
    
            EmployeeClassification classification = EmployeeClassification.createTestClassification(1);
            session.persist(classification);
            
            Employee employee = Employee.createTestEmployee(1, classification, company);
            session.persist(employee);
    
            Order order1 = Order.createTestOrder(1, company, customer, employee);
            Order order2 = Order.createTestOrder(2, company, customer, employee);
            session.persist(order1);
            session.persist(order2);
    
            tx.commit();
        }
    
        // Act
        List<Object[]> result = this.employeeDao.getAllEmployeeOrders();
    
        // Assert
        assertEquals(1, result.size(), "There should be one employee in the result.");
        
        Object[] employeeOrderData = result.getFirst();
        assertEquals("FirstName1 LastName1", employeeOrderData[0], "The employee's full name should match.");
        assertEquals("Company1", employeeOrderData[1], "The company name should match.");
        assertEquals(2L, employeeOrderData[2], "The order count should match.");
        assertEquals(new BigDecimal("2003.00"), employeeOrderData[3], "The total cost should match.");
    }

    @Test
    public void testEmployeeDao_whenEmployeeSaved_thenShouldBeAbleToRetrieveIt() {
        // Arrange

        // Act
        this.employeeDao.saveEmployee(this.employeeDto);
        List<EmployeeDto> customers = this.employeeDao.getAllEmployeesWhereNotDeleted();

        // Assert
        assertEquals(1, customers.size(), "Should have one employee after saving.");
        EmployeeDto retrieved = customers.getFirst();
        assertEquals("John", retrieved.getFirstName());
        assertEquals("Doe", retrieved.getLastName());
        assertEquals("john.doe@example.com", retrieved.getEmail());
        assertEquals("+359 88 1234567", retrieved.getPhone());
        assertEquals(1300, retrieved.getSalary());
    }

    @Test
    public void testEmployeeDao_whenEmployeeUpdated_thenChangesShouldPersist() {
        // Arrange
        this.employeeDao.saveEmployee(this.employeeDto);

        EmployeeDto saved = this.employeeDao.getAllEmployeesWhereNotDeleted().getFirst();
        String employeeId = saved.getId();

        // Act
        saved.setFirstName("Emily");
        saved.setLastName("Johnson");
        saved.setEmail("emily.johnson@example.com");
        saved.setPhone("+359 88 9876543");
        saved.setHireDate(Date.valueOf(LocalDate.now().minusYears(5)));
        saved.setSalary(1500);
        this.employeeDao.updateEmployee(saved);

        EmployeeDto updated = this.employeeDao.getEmployeeByIdWhereNotDeleted(employeeId);

        // Assert
        assertEquals("Emily", updated.getFirstName());
        assertEquals("Johnson", updated.getLastName());
        assertEquals("emily.johnson@example.com", updated.getEmail());
        assertEquals("+359 88 9876543", updated.getPhone());
        assertEquals(Date.valueOf(LocalDate.now().minusYears(5)), updated.getHireDate());
        assertEquals(1500, updated.getSalary());
    }

    @Test
    public void testEmployeeDao_whenEmployeeDeleted_thenShouldNotBeRetrievable() {
        // Arrange
        this.employeeDao.saveEmployee(this.employeeDto);

        EmployeeDto saved = this.employeeDao.getAllEmployeesWhereNotDeleted().getFirst();
        String employeeId = saved.getId();

        // Act
        this.employeeDao.deleteEmployee(employeeId);

        // Assert
        List<EmployeeDto> employees = this.employeeDao.getAllEmployeesWhereNotDeleted();
        assertTrue(employees.isEmpty(), "Should have no employees after deletion.");

        EmployeeDto deleted = this.employeeDao.getEmployeeByIdWhereNotDeleted(employeeId);
        assertNull(deleted, "Deleted employee should not be retrievable.");
    }
}
