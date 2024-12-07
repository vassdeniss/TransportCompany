package org.f108349.denis.dao;

import org.f108349.denis.configuration.SessionFactoryUtil;
import org.f108349.denis.dto.CompanyDto;
import org.f108349.denis.entity.Company;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CompanyDao extends BaseDao<CompanyDto, Company> {
    public void saveCompany(CompanyDto companyDto) {
        Company company = new Company(companyDto.getName(), companyDto.getRegistrationNo(), 
                companyDto.getEmail(), companyDto.getPhone());
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(company);
            tx.commit();
        }
    }
    
    public CompanyDto getCompanyByIdWhereNotDeleted(String id) {
        CompanyDto company;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            company = this.getByIdWhereNotDeleted(session, id, CompanyDto.class, Company.class);
            tx.commit();
        }
        
        return company;
    }
    
    public List<CompanyDto> getAllCompaniesWhereNotDeleted() {
        List<CompanyDto> companies;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            companies = this.getAllWhereNotDeleted(session, CompanyDto.class, Company.class);
            tx.commit();
        }
        
        return companies;
    }
    
    public void updateCompany(CompanyDto companyDto) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Company company = session.get(Company.class, companyDto.getId());
            
            company.setName(companyDto.getName());
            company.setRegistrationNo(companyDto.getRegistrationNo());
            company.setEmail(companyDto.getEmail());
            company.setPhone(companyDto.getPhone());
            
            session.merge(company);
            tx.commit();
        }
    }
    
    public void deleteCompany(String id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Company company = session.get(Company.class, id);
            company.setDeleted(true);
            session.merge(company);
            tx.commit();
        }
    }
}
