package org.f108349.denis.dao;

import org.f108349.denis.configuration.SessionFactoryUtil;
import org.f108349.denis.dto.CompanyDto;
import org.f108349.denis.entity.Company;
import org.f108349.denis.entity.Customer;
import org.f108349.denis.entity.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class CompanyDaoTests {
    private static SessionFactory sessionFactory;
    private CompanyDao companyDao;
    
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
    public void testGetAllCompanyOrders_whenOrdersExist_thenShouldRetrieveAggregatedData() {
        // Arrange
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            Customer customer = Customer.createTestCustomer(1);
            session.persist(customer);
            
            Company company1 = Company.createTestCompany(1);
            Company company2 = Company.createTestCompany(2);
            session.persist(company1);
            session.persist(company2);
            
            Order order1 = Order.createTestOrder(1, company1, customer);
            Order order2 = Order.createTestOrder(2, company2, customer);
            Order order3 = Order.createTestOrder(3, company2, customer);
            session.persist(order1);
            session.persist(order2);
            session.persist(order3);
    
            tx.commit();
        }
    
        // Act
        List<Object[]> results = this.companyDao.getAllCompanyOrders();
    
        // Assert
        assertNotNull(results, "Results should not be null");
        assertEquals(2, results.size(), "There should be two companies with orders");
    
        Object[] companyAData = results.stream().filter(r -> r[0].equals("Company 1")).findFirst().orElse(null);
        assertNotNull(companyAData, "Company 1 data should be present");
        assertEquals(1, (long)companyAData[1], "Company 1 should have 1 order");
        assertEquals(new BigDecimal("1001.00"), companyAData[2], "Company 1's total cost should be 1001");
    
        Object[] companyBData = results.stream().filter(r -> r[0].equals("Company 2")).findFirst().orElse(null);
        assertNotNull(companyBData, "Company 2 data should be present");
        assertEquals(2, (long)companyBData[1], "Company 2 should have 2 orders");
        assertEquals(new BigDecimal("2005.00"), companyBData[2], "Company 2's total cost should be 2005");
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
