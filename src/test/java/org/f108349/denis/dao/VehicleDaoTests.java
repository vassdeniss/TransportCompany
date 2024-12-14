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

import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleDaoTests {
    private static SessionFactory sessionFactory;
    private VehicleDao vehicleDao;

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
            // Clear data
            session.createMutationQuery("DELETE FROM Vehicle").executeUpdate();
            session.createMutationQuery("DELETE FROM Company").executeUpdate();
            session.createMutationQuery("DELETE FROM VehicleType").executeUpdate();
            tx.commit();

            tx = session.beginTransaction();
            Company company = new Company("Test", "123456789", 
                    "testemail@gmail.com", "+359 88 2221111");
            session.persist(company);

            VehicleType vehicleType = new VehicleType("big");
            session.persist(vehicleType);
            tx.commit();
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
        String companyId, vehicleTypeId;
        try (Session session = sessionFactory.openSession()) {
            companyId = session.createQuery("SELECT c.id FROM Company c WHERE c.isDeleted = false", String.class)
                    .setMaxResults(1)
                    .uniqueResult();
            vehicleTypeId = session.createQuery("SELECT vt.id FROM VehicleType vt WHERE vt.isDeleted = false", String.class)
                    .setMaxResults(1)
                    .uniqueResult();
        }

        VehicleDto dto = new VehicleDto("ValidModel", "CA1234PB", 50, companyId, vehicleTypeId);

        // Act
        this.vehicleDao.saveVehicle(dto);
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
        String companyId, vehicleTypeId;
        try (Session session = sessionFactory.openSession()) {
            companyId = session.createQuery("SELECT c.id FROM Company c WHERE c.isDeleted = false", String.class)
                    .setMaxResults(1)
                    .uniqueResult();
            vehicleTypeId = session.createQuery("SELECT vt.id FROM VehicleType vt WHERE vt.isDeleted = false", String.class)
                    .setMaxResults(1)
                    .uniqueResult();
        }
        
        VehicleDto dto = new VehicleDto("InitialModel", "CA1111CA", 10, companyId, vehicleTypeId);
        this.vehicleDao.saveVehicle(dto);
        
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
        String companyId, vehicleTypeId;
        try (Session session = sessionFactory.openSession()) {
            companyId = session.createQuery("SELECT c.id FROM Company c WHERE c.isDeleted = false", String.class)
                    .setMaxResults(1)
                    .uniqueResult();
            vehicleTypeId = session.createQuery("SELECT vt.id FROM VehicleType vt WHERE vt.isDeleted = false", String.class)
                    .setMaxResults(1)
                    .uniqueResult();
        }
        
        VehicleDto dto = new VehicleDto("InitialModel", "CA1111CA", 10, companyId, vehicleTypeId);
        this.vehicleDao.saveVehicle(dto);
        
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
