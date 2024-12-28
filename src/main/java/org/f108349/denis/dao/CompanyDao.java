package org.f108349.denis.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.apache.logging.log4j.spi.ObjectThreadContextMap;
import org.f108349.denis.dto.CompanyDto;
import org.f108349.denis.entity.Company;
import org.f108349.denis.entity.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public class CompanyDao extends BaseDao<CompanyDto, Company> {
    private final SessionFactory sessionFactory;
    
    public CompanyDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    public void saveCompany(CompanyDto companyDto) {
        Company company = new Company(companyDto.getName(), companyDto.getRegistrationNo(), 
                companyDto.getEmail(), companyDto.getPhone(), companyDto.getIncome());
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(company);
            tx.commit();
        }
    }
    
    public CompanyDto getCompanyByIdWhereNotDeleted(String id) {
        CompanyDto company;
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            company = this.getByIdWhereNotDeleted(session, id, CompanyDto.class, Company.class);
            tx.commit();
        }
        
        return company;
    }
    
    public List<CompanyDto> getAllCompaniesWhereNotDeleted() {
        List<CompanyDto> companies;
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            companies = this.getAllWhereNotDeleted(session, CompanyDto.class, Company.class);
            tx.commit();
        }
        
        return companies;
    }
    
    public List<Object[]> getAllCompanyOrders() {
        List<Object[]> list;
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
            Root<Company> root = cq.from(Company.class);

            Join<Company, Order> join = root.join("orders");
            
            cq.multiselect(root.get("name"), cb.count(join.get("id")), cb.sumAsDouble(join.get("totalCost")))
                            .groupBy(root.get("id"));
            
            list = session.createQuery(cq).getResultList();            
            
            tx.commit();
        }
        
        return list;
    }
    
    public void updateCompany(CompanyDto companyDto) {
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Company company = session.get(Company.class, companyDto.getId());
            
            company.setName(companyDto.getName());
            company.setRegistrationNo(companyDto.getRegistrationNo());
            company.setEmail(companyDto.getEmail());
            company.setPhone(companyDto.getPhone());
            company.setIncome(companyDto.getIncome());
            
            session.merge(company);
            tx.commit();
        }
    }
    
    public void deleteCompany(String id) {
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Company company = session.get(Company.class, id);
            company.setDeleted(true);
            session.merge(company);
            tx.commit();
        }
    }
}
