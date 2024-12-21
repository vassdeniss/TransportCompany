package org.f108349.denis.dao;

import org.f108349.denis.configuration.SessionFactoryUtil;
import org.f108349.denis.dto.CompanyDto;
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
public class CompanyDaoIntegrationTests {
    private static SessionFactory sessionFactory;
    private CompanyDao companyDao;
    
    @Container
    private static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb-company")
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
            session.createMutationQuery("DELETE FROM Company").executeUpdate();
            tx.commit();
        }
        
        this.companyDao = new CompanyDao(sessionFactory);
    }
    
    @AfterAll
    static void afterAll() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
    
    @Test
    public void testCompanyDao_whenCompanySaved_thenShouldBeAbleToRetrieveIt() {
        // Arrange
        CompanyDto dto = new CompanyDto("Test Company", "123456789", 
                "test@example.com", "+359 88 7491288", 1200);

        // Act
        this.companyDao.saveCompany(dto);
        List<CompanyDto> companies = companyDao.getAllCompaniesWhereNotDeleted();

        // Assert
        assertEquals(1, companies.size(), "Should have one company after saving.");
        CompanyDto retrieved = companies.getFirst();
        assertEquals("Test Company", retrieved.getName());
        assertEquals("123456789", retrieved.getRegistrationNo());
        assertEquals("test@example.com", retrieved.getEmail());
        assertEquals("+359 88 7491288", retrieved.getPhone());
        assertEquals(1200, retrieved.getIncome());
    }
    
    @Test
    public void testCompanyDao_whenCompanyUpdated_thenChangesShouldPersist() {
        // Arrange
        CompanyDto dto = new CompanyDto("Old Name", "987654321", 
                "old@example.com", "+359 88 4123122", 1200);
        this.companyDao.saveCompany(dto);

        CompanyDto saved = this.companyDao.getAllCompaniesWhereNotDeleted().getFirst();
        String companyId = saved.getId();

        // Act
        saved.setName("New Name");
        saved.setRegistrationNo("123456789");
        saved.setEmail("new@example.com");
        saved.setPhone("+359 88 4132122");
        saved.setIncome(1300);
        this.companyDao.updateCompany(saved);

        CompanyDto updated = this.companyDao.getCompanyByIdWhereNotDeleted(companyId);

        // Assert
        assertEquals("New Name", updated.getName());
        assertEquals("123456789", updated.getRegistrationNo());
        assertEquals("new@example.com", updated.getEmail());
        assertEquals("+359 88 4132122", updated.getPhone());
        assertEquals(1300, updated.getIncome());
    }
    
    @Test
    public void testCompanyDao_whenCompanyDeleted_thenShouldNotBeRetrievable() {
        // Arrange
        CompanyDto dto = new CompanyDto("DeleteMe Inc.", "123456789", 
                "delete@example.com", "+359 88 4123122", 1200);
        this.companyDao.saveCompany(dto);

        CompanyDto saved = this.companyDao.getAllCompaniesWhereNotDeleted().getFirst();
        String companyId = saved.getId();

        // Act
        this.companyDao.deleteCompany(companyId);

        // Assert
        List<CompanyDto> companies = this.companyDao.getAllCompaniesWhereNotDeleted();
        assertTrue(companies.isEmpty(), "Should have no companies after deletion.");

        CompanyDto deleted = this.companyDao.getCompanyByIdWhereNotDeleted(companyId);
        assertNull(deleted, "Deleted company should not be retrievable.");
    }
}
