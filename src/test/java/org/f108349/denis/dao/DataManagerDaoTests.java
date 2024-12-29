package org.f108349.denis.dao;

import org.f108349.denis.configuration.SessionFactoryUtil;
import org.f108349.denis.dto.CompanyDto;
import org.f108349.denis.dto.EmployeeDto;
import org.f108349.denis.dto.OrderDto;
import org.f108349.denis.entity.*;
import org.f108349.denis.entity.Order;
import org.f108349.denis.entity.enums.Status;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataManagerDaoTests {
    private static SessionFactory sessionFactory;
    private DataManagerDao dataManagerDao;
    
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
            session.createMutationQuery("DELETE FROM Vehicle").executeUpdate();
            session.createMutationQuery("DELETE FROM VehicleType").executeUpdate();
            session.createMutationQuery("DELETE FROM Employee").executeUpdate();
            session.createMutationQuery("DELETE FROM EmployeeClassification").executeUpdate();
            session.createMutationQuery("DELETE FROM Company").executeUpdate();
            session.createMutationQuery("DELETE FROM Customer").executeUpdate();
            tx.commit();
        }
        
        this.dataManagerDao = new DataManagerDao(sessionFactory);
        this.seedTestData();
    }
    
    @AfterAll
    static void afterAll() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
    
    @Test
    void testGetCompaniesByName_whenFilterExists_shouldReturnFilteredAndOrderedAsc() {
        // Arrange
        
        // Act
        List<CompanyDto> results = this.dataManagerDao.getCompaniesByName("a", "asc");
        
        // Assert
        assertEquals(2, results.size(), "Should find 2 companies containing 'a'");
        assertEquals("Alpha Inc.", results.get(0).getName());
        assertEquals("Bravo LLC", results.get(1).getName());
    }
    
    @Test
    void testGetCompaniesByName_whenFilterEmpty_shouldReturnAllOrderedDesc() {
        // Arrange
        
        // Act
        List<CompanyDto> results = this.dataManagerDao.getCompaniesByName("", "desc");

        // Assert
        assertEquals(3, results.size(), "Should find all companies with no filter");
        assertEquals("Dell", results.get(0).getName());
        assertEquals("Bravo LLC", results.get(1).getName());
        assertEquals("Alpha Inc.", results.get(2).getName());
    }
    
    @Test
    void testGetCompaniesByIncome_whenMinAndMaxSet_shouldReturnWithinRangeOrderedAsc() {
        // Arrange
        
        // Act
        List<CompanyDto> results = this.dataManagerDao.getCompaniesByIncome(1000.0, 6000.0, "asc");

        // Assert
        assertEquals(2, results.size(), "Should find 2 companies in range [1000..6000]");
        assertEquals("Alpha Inc.", results.get(0).getName());
        assertEquals("Bravo LLC", results.get(1).getName());
    }

    @Test
    void testGetCompaniesByIncome_whenOnlyMinSet_shouldReturnAllAboveMinOrderedDesc() {
        // Arrange
        
        // Act
        List<CompanyDto> results = this.dataManagerDao.getCompaniesByIncome(6000.0, null, "desc");
        
        // Assert
        assertEquals(2, results.size());
        assertEquals("Dell", results.get(0).getName());
        assertEquals("Bravo LLC", results.get(1).getName());
    }
    
    @Test
    void testGetEmployeesByClassification_whenFilterFullTime_shouldReturnOnlyFullTime() {
        // Arrange
        
        // Act
        List<EmployeeDto> results = this.dataManagerDao.getEmployeesByClassification("FULL_TIME", "asc");
        
        // Assert
        assertEquals(1, results.size(), "Should find only employees with classification=FULL_TIME");
        assertEquals("Alice", results.getFirst().getFirstName());
        assertEquals("Smith", results.getFirst().getLastName());
    }

    @Test
    void testGetEmployeesByClassification_whenNoFilter_shouldReturnAllOrderedDesc() {
        // Arrange
        
        // Act
        List<EmployeeDto> results = this.dataManagerDao.getEmployeesByClassification(null, "desc");
        
        // Assert
        assertEquals(2, results.size());        
        assertEquals("John", results.get(0).getFirstName());
        assertEquals("Doe", results.get(0).getLastName());
        assertEquals("Alice", results.get(1).getFirstName());
        assertEquals("Smith", results.get(1).getLastName());
    }

    @Test
    void testGetEmployeesBySalary_whenRangeProvided_shouldReturnOnlyEmployeesWithinRangeOrderedAsc() {
        // Arrange
        
        // Act
        List<EmployeeDto> results = this.dataManagerDao.getEmployeesBySalary(2000.0, 3000.0, "asc");

        // Assert
        assertEquals(1, results.size());
        assertTrue(results.getFirst().getSalary() >= 2000 && results.getFirst().getSalary() <= 3000);
    }

    @Test
    void testGetEmployeesBySalary_whenOnlyMinProvided_shouldReturnAllAboveMinOrderedDesc() {
        // Arrange
        
        // Act
        List<EmployeeDto> results = this.dataManagerDao.getEmployeesBySalary(1000.0, null, "desc");
        
        // Assert
        assertEquals(2, results.size());
        assertEquals("John", results.get(0).getFirstName());
        assertEquals("Doe", results.get(0).getLastName());
        assertEquals("Alice", results.get(1).getFirstName());
        assertEquals("Smith", results.get(1).getLastName());
    }
    
    @Test
    void testGetOrderByDestination_whenFilterSofia_shouldReturnSofiaOrder() {
        // Arrange
        
        // Act
        List<OrderDto> results = this.dataManagerDao.getOrderByDestination("Sofia", "asc");
        
        // Assert
        assertEquals(1, results.size(), "Should find only the order with 'Sofia'");
        assertEquals("Sofia", results.getFirst().getDestination());
    }

    @Test
    void testGetOrderByDestination_whenEmptyFilter_shouldReturnAllOrderedDesc() {
        // Arrange
        
        // Act        
        List<OrderDto> results = this.dataManagerDao.getOrderByDestination("", "desc");
        
        // Assert
        assertEquals(3, results.size());
        assertEquals("Sofia", results.get(0).getDestination());
        assertEquals("Plovdiv", results.get(1).getDestination());
        assertEquals("Burgas", results.get(2).getDestination());
    }
    
    @Test
    void testGetOrdersByCost_whenRangeProvided_shouldReturnWithinRange() {
        // Arrange
        
        // Act
        List<OrderDto> results = this.dataManagerDao.getOrdersByCost(400.00, 500.0, "asc");
        
        // Assert
        assertEquals(2, results.size());
        assertEquals("Burgas", results.get(0).getDestination());
        assertEquals("Plovdiv", results.get(1).getDestination());
    }

    @Test
    void testGetOrdersByCost_whenOnlyMaxProvided_shouldReturnAllBelowOrEqualMaxOrderedDesc() {
        // Arrange
        
        // Act
        List<OrderDto> results = this.dataManagerDao.getOrdersByCost(null, 500.0, "desc");
        
        // Assert
        assertEquals(3, results.size());
        assertEquals("Plovdiv", results.get(0).getDestination());
        assertEquals("Burgas", results.get(1).getDestination());
        assertEquals("Sofia", results.get(2).getDestination());
    }
    
    private void seedTestData() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
    
            // 1) Create a VehicleType
            VehicleType vehicleType = new VehicleType("Truck");
            session.persist(vehicleType);
    
            // 2) Create a Company
            Company company = new Company("Alpha Inc.", "123456789", 
                    "alpha@example.com", "+359 88 7123456", 5000.0);
            session.persist(company);
            
            Company company2 = new Company("Bravo LLC", "223456789", 
                    "beta@example.com", "+359 88 7223456", 6000.0);
            session.persist(company2);
            
            Company company3 = new Company("Dell", "323456789", 
                    "dell@example.com", "+359 88 7323456", 10000.0);
            session.persist(company3);
    
            // 3) Create a Customer
            Customer customer = new Customer("John", "Doe", "john.doe@example.com", 
                    "+359 88 7556666", "Some Street 123");
            session.persist(customer);
    
            // 4) Create an EmployeeClassification
            EmployeeClassification fullTime = new EmployeeClassification("FULL_TIME");
            session.persist(fullTime);
    
            EmployeeClassification partTime = new EmployeeClassification("PART_TIME");
            session.persist(partTime);
    
            // 5) Create an Employee referencing a classification & company
            Employee employee = new Employee("John", "Doe", "john.doe@example.com",
                    "+359 88 6777777", Date.valueOf(LocalDate.now()), 2800.0);
            employee.setEmployeeClassification(partTime);
            employee.setCompany(company);
            session.persist(employee);
            
            Employee employee2 = new Employee("Alice", "Smith", "alice.smith@example.com",
                    "+359 88 7777777", Date.valueOf(LocalDate.now()), 1800.0);
            employee2.setEmployeeClassification(fullTime);
            employee2.setCompany(company);
            session.persist(employee2);
    
            // 6) Create a Vehicle referencing the company & vehicleType
            Vehicle vehicle = new Vehicle("Volvo VNL", "CA1234XB", 18);
            vehicle.setCompany(company);
            vehicle.setVehicleType(vehicleType);
            session.persist(vehicle);
    
            // 7) Create an Order referencing: Company, Customer, Employee, Vehicle
            Order order = new Order("Office Supplies", Date.valueOf(LocalDate.now()), null, "Sofia",
                    new BigDecimal("300.00"), new BigDecimal("25.00"), Status.PENDING);
            order.setCompany(company);
            order.setCustomer(customer);
            order.setEmployee(employee);
            order.setVehicle(vehicle);
            session.persist(order);
            
            Order order2 = new Order("Office Supplies 2", Date.valueOf(LocalDate.now()), null, "Burgas",
                    new BigDecimal("400.00"), new BigDecimal("50.00"), Status.SHIPPED);
            order2.setCompany(company);
            order2.setCustomer(customer);
            order2.setEmployee(employee);
            order2.setVehicle(vehicle);
            session.persist(order2);
            
            Order order3 = new Order("Office Supplies 3", Date.valueOf(LocalDate.now()), null, "Plovdiv",
                    new BigDecimal("500.00"), new BigDecimal("50.00"), Status.CANCELED);
            order3.setCompany(company);
            order3.setCustomer(customer);
            order3.setEmployee(employee);
            order3.setVehicle(vehicle);
            session.persist(order3);
    
            tx.commit();
        }
    }
}
