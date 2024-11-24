package org.f108349.denis.dao;

import org.f108349.denis.configuration.SessionFactoryUtil;
import org.f108349.denis.dto.CompanyDto;
import org.f108349.denis.entity.Company;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
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
        Company company;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            company = session.get(Company.class, id);
            tx.commit();
        }
        
        if (company == null || (company != null && company.isDeleted())) {
            return null;
        }
        
        return new CompanyDto(company);
    }
    
    public static List<CompanyDto> getAllCompanies() {
        List<Company> companies;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            companies = session
                    .createQuery("select c from Company c where c.isDeleted = false", Company.class)
                    .getResultList();
            tx.commit();
        }
        
        List<CompanyDto> companyDtos = new ArrayList<>();
        companies.forEach(company -> companyDtos.add(new CompanyDto(company)));
        
        return companyDtos;
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
