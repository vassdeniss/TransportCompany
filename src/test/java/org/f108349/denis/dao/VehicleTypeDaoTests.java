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

import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleTypeDaoTests {
    private static SessionFactory sessionFactory;
    private VehicleTypeDao vehicleTypeDao;

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
            Company company = new Company("Test", "123456789", "testmail@mail.com", "+359882813211");
            Vehicle vehicle = new Vehicle("TestVehicle", "123456", 20);
            vehicle.setVehicleType(vehicleType);
            vehicle.setCompany(company);
            session.persist(company);
            session.persist(vehicle);
            tx.commit();
        }

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> 
                this.vehicleTypeDao.deleteVehicleType(vehicleTypeId)
        );

        String expectedMessage = "Cannot delete vehicle type; vehicles are still assigned.";
        assertEquals(expectedMessage, exception.getMessage());
    }
}
