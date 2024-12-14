package org.f108349.denis.dao;

import org.f108349.denis.configuration.SessionFactoryUtil;
import org.f108349.denis.dto.EmployeeClassificationDto;
import org.f108349.denis.entity.Employee;
import org.f108349.denis.entity.EmployeeClassification;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeClassificationDaoTests {
    private static SessionFactory sessionFactory;
    private EmployeeClassificationDao employeeClassificationDao;

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
            session.createMutationQuery("DELETE FROM Employee").executeUpdate();
            session.createMutationQuery("DELETE FROM EmployeeClassification").executeUpdate();
            tx.commit();
        }

        employeeClassificationDao = new EmployeeClassificationDao(sessionFactory);
    }

    @AfterAll
    static void afterAll() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
    
    @Test
    public void testEmployeeClassificationDao_whenClassificationSaved_thenShouldBeAbleToRetrieveIt() {
        // Arrange
        EmployeeClassificationDto dto = new EmployeeClassificationDto("Manager");

        // Act
        this.employeeClassificationDao.saveEmployeeClassification(dto);
        List<EmployeeClassificationDto> classifications = this.employeeClassificationDao
                .getAllEmployeeClassificationsWhereNotDeleted();

        // Assert
        assertEquals(1, classifications.size(), "Should have one classification after saving.");
        EmployeeClassificationDto retrieved = classifications.getFirst();
        assertEquals("Manager", retrieved.getClassificationName());
    }
    
    @Test
    public void testEmployeeClassificationDao_whenClassificationDeleted_thenShouldNotBeRetrievable() {
        // Arrange
        EmployeeClassificationDto dto = new EmployeeClassificationDto("Intern");
        this.employeeClassificationDao.saveEmployeeClassification(dto);

        EmployeeClassificationDto saved = this.employeeClassificationDao
                .getAllEmployeeClassificationsWhereNotDeleted().getFirst();
        String classificationId = saved.getId();

        // Act
        this.employeeClassificationDao.deleteEmployeeClassification(classificationId);

        // Assert
        List<EmployeeClassificationDto> allAfterDelete = this.employeeClassificationDao
                .getAllEmployeeClassificationsWhereNotDeleted();
        assertTrue(allAfterDelete.isEmpty(), "No classifications should remain after deletion.");
        
        EmployeeClassificationDto deleted = this.employeeClassificationDao
                .getEmployeeClassificationByIdWhereNotDeleted(classificationId);
        assertNull(deleted, "Deleted classification should not be retrievable.");
    }

    @Test
    public void testEmployeeClassificationDao_whenClassificationAssignedToEmployee_thenDeleteShouldFail() {
        // Arrange
        EmployeeClassificationDto dto = new EmployeeClassificationDto("Engineer");
        this.employeeClassificationDao.saveEmployeeClassification(dto);

        EmployeeClassificationDto saved = this.employeeClassificationDao
                .getAllEmployeeClassificationsWhereNotDeleted().getFirst();
        String classificationId = saved.getId();
        
        // Simulate a employee assigned to this classification
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            EmployeeClassification classification = session.get(EmployeeClassification.class, classificationId);
            Employee employee = new Employee("John", "Doe", "testMail@mail.com", 
                    "+359882121211", Date.valueOf("2024-02-02"));
            employee.setEmployeeClassification(classification);
            session.persist(employee);
            tx.commit();
        }

        // Act & Assert
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> 
                this.employeeClassificationDao.deleteEmployeeClassification(classificationId)
        );

        String expectedMessage = "Cannot delete classification; employees are still assigned.";
        assertEquals(expectedMessage, thrown.getMessage());
    }
}
