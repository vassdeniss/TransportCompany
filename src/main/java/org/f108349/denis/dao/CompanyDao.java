package org.f108349.denis.dao;

import org.f108349.denis.configuration.SessionFactoryUtil;
import org.f108349.denis.dto.CompanyDto;
import org.f108349.denis.dto.VehicleTypeDto;
import org.f108349.denis.entity.Company;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CompanyDao {
    public static void saveCompany(CompanyDto companyDto) {
        Company company = new Company(companyDto.getName(), companyDto.getRegistrationNo(), 
                companyDto.getEmail(), companyDto.getPhone());
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(company);
            tx.commit();
        }
    }
    
    public static CompanyDto getCompanyById(String id) {
        CompanyDto company;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            company = session
                    .createQuery("select new org.f108349.denis.dto.CompanyDto(c) from Company c " +
                            "where c.isDeleted = false and id = :id", CompanyDto.class)
                    .setParameter("id", id)
                    .uniqueResultOptional()
                    .orElse(null);               
            tx.commit();
        }
        
        return company;
    }
    
    public static List<CompanyDto> getAllCompanies() {
        List<CompanyDto> companies;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            companies = session
                    .createQuery("select new org.f108349.denis.dto.CompanyDto(c) " +
                            "from Company c where c.isDeleted = false", CompanyDto.class)
                    .getResultList();
            tx.commit();
        }
        
        return companies;
    }
    
    public static void updateCompany(CompanyDto companyDto) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Company companny = session.get(Company.class, companyDto.getId());
            
            companny.setName(companyDto.getName());
            companny.setRegistrationNo(companyDto.getRegistrationNo());
            companny.setEmail(companyDto.getEmail());
            companny.setPhone(companyDto.getPhone());
            
            session.merge(companny);
            tx.commit();
        }
    }
    
    public static void deleteCompany(String id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Company company = session.get(Company.class, id);
            company.setDeleted(true);
            session.merge(company);
            tx.commit();
        }
    }
}
