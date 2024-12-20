package org.f108349.denis.dao;

import org.f108349.denis.configuration.SessionFactoryUtil;
import org.f108349.denis.dto.VehicleTypeDto;
import org.f108349.denis.entity.Company;
import org.f108349.denis.entity.Vehicle;
import org.f108349.denis.entity.VehicleType;
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
public class VehicleTypeDaoIntegrationTests {
    private static SessionFactory sessionFactory;
    private VehicleTypeDao vehicleTypeDao;

    @Container
    private static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb-vehicletype")
            .withUsername("user")
            .withPassword("password");
    
    @BeforeAll
    static void beforeAll() {
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
            session.createMutationQuery("DELETE FROM Vehicle").executeUpdate();
            session.createMutationQuery("DELETE FROM VehicleType").executeUpdate();
            session.createMutationQuery("DELETE FROM Company").executeUpdate();
            tx.commit();
        }

        this.vehicleTypeDao = new VehicleTypeDao(sessionFactory);
    }

    @AfterAll
    static void afterAll() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void testVehicleTypeDao_whenVehicleTypeSaved_thenShouldBeAbleToRetrieveIt() {
        // Arrange
        VehicleTypeDto dto = new VehicleTypeDto("SUV");

        // Act
        this.vehicleTypeDao.saveVehicleType(dto);
        List<VehicleTypeDto> vehicleTypes = this.vehicleTypeDao.getAllVehicleTypesWhereNotDeleted();

        // Assert
        assertEquals(1, vehicleTypes.size(), "Should have one vehicle type after saving.");
        VehicleTypeDto retrieved = vehicleTypes.getFirst();
        assertEquals("SUV", retrieved.getTypeName());
    }

    @Test
    public void testVehicleTypeDao_whenVehicleTypeDeleted_thenShouldNotBeRetrievable() {
        // Arrange
        VehicleTypeDto dto = new VehicleTypeDto("Truck");
        this.vehicleTypeDao.saveVehicleType(dto);

        VehicleTypeDto saved = this.vehicleTypeDao.getAllVehicleTypesWhereNotDeleted().getFirst();
        String vehicleTypeId = saved.getId();

        // Act
        this.vehicleTypeDao.deleteVehicleType(vehicleTypeId);

        // Assert
        List<VehicleTypeDto> vehicleTypes = this.vehicleTypeDao.getAllVehicleTypesWhereNotDeleted();
        assertTrue(vehicleTypes.isEmpty(), "Should have no vehicle types after deletion.");

        VehicleTypeDto deleted = this.vehicleTypeDao.getVehicleTypeByIdWhereNotDeleted(vehicleTypeId);
        assertNull(deleted, "Deleted vehicle type should not be retrievable.");
    }

    @Test
    public void testVehicleTypeDao_whenVehicleTypeAssignedToVehicle_thenDeleteShouldFail() {
        // Arrange
        VehicleTypeDto dto = new VehicleTypeDto("Sedan");
        this.vehicleTypeDao.saveVehicleType(dto);

        VehicleTypeDto saved = this.vehicleTypeDao.getAllVehicleTypesWhereNotDeleted().getFirst();
        String vehicleTypeId = saved.getId();

        // Simulate a vehicle assigned to this vehicle type
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            VehicleType vehicleType = session.get(VehicleType.class, vehicleTypeId);
            Company company = new Company("Test", "123456789", "testmail@mail.com", "+359882813211", 1200);
            Vehicle vehicle = new Vehicle("TestVehicle", "BG1234FF", 20);
            vehicle.setVehicleType(vehicleType);
            vehicle.setCompany(company);
            session.persist(company);
            session.persist(vehicle);
            tx.commit();
        }

        // Act & Assert
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            this.vehicleTypeDao.deleteVehicleType(vehicleTypeId);
        });

        String expectedMessage = "Cannot delete vehicle type; vehicles are still assigned.";
        assertEquals(expectedMessage, exception.getMessage());
    }
}
