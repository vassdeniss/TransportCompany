package org.f108349.denis.dao;

import org.f108349.denis.configuration.SessionFactoryUtil;
import org.f108349.denis.dto.VehicleDto;
import org.f108349.denis.entity.Company;
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
public class VehicleDaoIntegrationTests {
    private static SessionFactory sessionFactory;
    private VehicleDao vehicleDao;
    private VehicleDto vehicleDto;

    @Container
    private static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb-vehicle")
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
            // Clear data
            session.createMutationQuery("DELETE FROM Vehicle").executeUpdate();
            session.createMutationQuery("DELETE FROM Company").executeUpdate();
            session.createMutationQuery("DELETE FROM VehicleType").executeUpdate();

            Company company = new Company("Test", "123456789", 
                    "testemail@gmail.com", "+359 88 2221111");
            String companyId = company.getId();
            session.persist(company);

            VehicleType vehicleType = new VehicleType("big");
            String vehicleTypeId = vehicleType.getId();
            session.persist(vehicleType);
            tx.commit();
            
            this.vehicleDto = new VehicleDto("ValidModel", "CA1234PB",
                    50, companyId, vehicleTypeId);
        }
        
        this.vehicleDao = new VehicleDao(sessionFactory);
    }
    
    @AfterAll
    static void afterAll() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
    
    @Test
    public void testVehicleDao_whenVehicleSaved_thenShouldBeAbleToRetrieveIt() {
        // Arrange

        // Act
        boolean saved = this.vehicleDao.saveVehicle(this.vehicleDto);
        assertTrue(saved, "Vehicle should be saved successfully.");

        List<VehicleDto> vehicles = this.vehicleDao.getAllVehiclesWhereNotDeleted();

        // Assert
        assertEquals(1, vehicles.size(), "Should have one vehicle after saving.");
        VehicleDto retrieved = vehicles.getFirst();
        assertEquals("ValidModel", retrieved.getModel());
        assertEquals("CA1234PB", retrieved.getLicensePlate());
        assertEquals(50, retrieved.getCapacity());
    }
    
    @Test
    public void testVehicleDao_whenVehicleUpdated_thenChangesShouldPersist() {
        // Arrange
        this.vehicleDao.saveVehicle(this.vehicleDto);
        
        VehicleDto saved = this.vehicleDao.getAllVehiclesWhereNotDeleted().getFirst();
        String vehicleId = saved.getId();

        // Act
        saved.setModel("UpdatedModel");
        saved.setLicensePlate("CB2222CB");
        saved.setCapacity(99);
        this.vehicleDao.updateVehicle(saved);

        VehicleDto updated = this.vehicleDao.getVehicleByIdWhereNotDeleted(vehicleId);
        
        // Assert
        assertEquals("UpdatedModel", updated.getModel());
        assertEquals("CB2222CB", updated.getLicensePlate());
        assertEquals(99, updated.getCapacity());
    }

    @Test
    public void testVehicleDao_whenVehicleDeleted_thenShouldNotBeRetrievable() {
        // Arrange
        this.vehicleDao.saveVehicle(this.vehicleDto);
        
        VehicleDto saved = this.vehicleDao.getAllVehiclesWhereNotDeleted().getFirst();
        String vehicleId = saved.getId();

        // Act
        this.vehicleDao.deleteVehicle(vehicleId);

        // Assert 
        List<VehicleDto> remainingVehicles = this.vehicleDao.getAllVehiclesWhereNotDeleted();
        assertTrue(remainingVehicles.isEmpty(), "No vehicles should remain after deletion.");

        VehicleDto deleted = this.vehicleDao.getVehicleByIdWhereNotDeleted(vehicleId);
        assertNull(deleted, "Deleted vehicle should not be retrievable.");
    }
}
