package org.f108349.denis.dao;

import org.f108349.denis.configuration.SessionFactoryUtil;
import org.f108349.denis.dto.OrderDto;
import org.f108349.denis.entity.*;
import org.f108349.denis.entity.enums.Status;
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

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class OrderDaoIntegrationTests {
    private static SessionFactory sessionFactory;
    private OrderDao orderDao;
    private OrderDto orderDto;

    @Container
    private static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb-order")
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
            session.createMutationQuery("DELETE FROM Order").executeUpdate();
            session.createMutationQuery("DELETE FROM Customer").executeUpdate();
            session.createMutationQuery("DELETE FROM Vehicle").executeUpdate();
            session.createMutationQuery("DELETE FROM VehicleType").executeUpdate();
            session.createMutationQuery("DELETE FROM Company").executeUpdate();
            session.createMutationQuery("DELETE FROM Employee").executeUpdate();
            session.createMutationQuery("DELETE FROM EmployeeClassification").executeUpdate();

            Customer customer = new Customer("John", "Doe", "testmail@mail.bg", 
                    "+359881952511", "somestr");
            String customerId = customer.getId();
            session.persist(customer);

            EmployeeClassification classification = new EmployeeClassification("test");
            session.persist(classification);

            Employee employee = new Employee("John", "Doe", "testmail@mail.bg", 
                    "+359881952511", Date.valueOf(LocalDate.of(1990, 1, 1)));
            employee.setEmployeeClassification(classification);
            String employeeId = employee.getId();
            session.persist(employee);
            
            Company company = new Company("Test", "123456789", 
                    "testemail@gmail.com", "+359 88 2221111");
            String companyId = company.getId();
            session.persist(company);

            VehicleType type = new VehicleType("test");
            session.persist(type);
            
            Vehicle vehicle = new Vehicle("model", "BG1231SS", 20);
            vehicle.setVehicleType(type);
            vehicle.setCompany(company);
            String vehicleId = vehicle.getId();
            session.persist(vehicle);

            tx.commit();
            
            this.orderDto = new OrderDto("bricks", Date.valueOf(LocalDate.now()), null, "somedest", 
                    new BigDecimal("1500.00"), new BigDecimal("17.00"), customerId, employeeId, companyId, vehicleId,
                    null);
        }

        this.orderDao = new OrderDao(sessionFactory);
    }

    @AfterAll
    static void afterAll() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void testOrderDao_whenOrderSaved_thenShouldBeAbleToRetrieveIt() {
        // Arrange

        // Act
        this.orderDao.saveOrder(this.orderDto);
        List<OrderDto> orders = this.orderDao.getAllOrdersWhereNotDeleted();

        // Assert
        assertEquals(1, orders.size(), "Should have one order after saving.");
        OrderDto retrieved = orders.getFirst();
        assertEquals(this.orderDto.getItem(), retrieved.getItem());
        assertEquals(this.orderDto.getOrderDate(), retrieved.getOrderDate());
        assertNull(retrieved.getShipmentDate());
        assertEquals(this.orderDto.getDestination(), retrieved.getDestination());
        assertEquals(this.orderDto.getTotalCost(), retrieved.getTotalCost());
        assertEquals(this.orderDto.getTotalWeight(), retrieved.getTotalWeight());
        assertEquals(Status.PENDING, retrieved.getStatus());
    }

    @Test
    public void testOrderDao_whenOrderUpdated_thenChangesShouldPersist() {
        // Arrange
        this.orderDao.saveOrder(this.orderDto);

        OrderDto saved = this.orderDao.getAllOrdersWhereNotDeleted().getFirst();
        String orderId = saved.getId();

        // Act
        saved.setItem("bricks2");
        saved.setOrderDate(Date.valueOf(LocalDate.now().plusDays(2)));
        saved.setShipmentDate(Date.valueOf(LocalDate.now().plusDays(5)));
        saved.setDestination("newdest");
        saved.setTotalCost(new BigDecimal("1.00"));
        saved.setTotalWeight(new BigDecimal("2.00"));
        saved.setStatus(Status.DELIVERED);
        this.orderDao.updateOrder(saved);

        OrderDto updated = this.orderDao.getOrderByIdWhereNotDeleted(orderId);

        // Assert
        assertEquals(saved.getItem(), updated.getItem());
        assertEquals(saved.getOrderDate(), updated.getOrderDate());
        assertEquals(saved.getShipmentDate(), updated.getShipmentDate());
        assertEquals(saved.getDestination(), updated.getDestination());
        assertEquals(saved.getTotalCost(), updated.getTotalCost());
        assertEquals(saved.getTotalWeight(), updated.getTotalWeight());
        assertEquals(saved.getStatus(), updated.getStatus());
    }

    @Test
    public void testOrderDao_whenOrderDeleted_thenShouldNotBeRetrievable() {
        // Arrange
        this.orderDao.saveOrder(this.orderDto);

        OrderDto saved = this.orderDao.getAllOrdersWhereNotDeleted().getFirst();
        String orderId = saved.getId();

        // Act
        this.orderDao.deleteOrder(orderId);

        // Assert
        List<OrderDto> orders = this.orderDao.getAllOrdersWhereNotDeleted();
        assertTrue(orders.isEmpty(), "Should have no orders after deletion.");

        OrderDto deleted = this.orderDao.getOrderByIdWhereNotDeleted(orderId);
        assertNull(deleted, "Deleted order should not be retrievable.");
    }
}
