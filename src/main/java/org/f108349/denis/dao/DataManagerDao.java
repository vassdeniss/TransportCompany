package org.f108349.denis.dao;

import jakarta.persistence.criteria.*;
import org.f108349.denis.dto.CompanyDto;
import org.f108349.denis.dto.EmployeeDto;
import org.f108349.denis.dto.OrderDto;
import org.f108349.denis.entity.Company;
import org.f108349.denis.entity.Employee;
import org.f108349.denis.entity.EmployeeClassification;
import org.f108349.denis.entity.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataManagerDao {
    private final SessionFactory sessionFactory;
    
    public DataManagerDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    public List<CompanyDto> getCompaniesByName(String nameFilter, String order) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<CompanyDto> cq = cb.createQuery(CompanyDto.class);
            Root<Company> root = cq.from(Company.class);

            if (nameFilter != null && !nameFilter.isEmpty()) {
                cq.where(cb.like(cb.lower(root.get("name")), "%" + nameFilter.toLowerCase() + "%"));
            }

            if ("desc".equalsIgnoreCase(order)) {
                cq.orderBy(cb.desc(root.get("name")));
            } else {
                cq.orderBy(cb.asc(root.get("name")));
            }

            return session.createQuery(cq).getResultList();
        }
    }
    
    public List<CompanyDto> getCompaniesByIncome(Double minIncome, Double maxIncome, String order) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<CompanyDto> cq = cb.createQuery(CompanyDto.class);
            Root<Company> root = cq.from(Company.class);

            List<Predicate> predicates = new ArrayList<>();
            if (minIncome != null) {
                predicates.add(cb.ge(root.get("income"), minIncome));
            }

            if (maxIncome != null) {
                predicates.add(cb.le(root.get("income"), maxIncome));
            }

            cq.where(predicates.toArray(new Predicate[0]));

            if ("desc".equalsIgnoreCase(order)) {
                cq.orderBy(cb.desc(root.get("income")));
            } else {
                cq.orderBy(cb.asc(root.get("income")));
            }

            return session.createQuery(cq).getResultList();
        }
    }
    
    public List<EmployeeDto> getEmployeesByClassification(String classificationFilter, String order) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<EmployeeDto> cq = cb.createQuery(EmployeeDto.class);
            Root<Employee> root = cq.from(Employee.class);

            Join<Employee, EmployeeClassification> classificationJoin
                    = root.join("employeeClassification");
            
            if (Objects.nonNull(classificationFilter) && !classificationFilter.isBlank()) {
                cq.where(cb.equal(cb.lower(classificationJoin.get("classificationName")), classificationFilter.toLowerCase()));
            }

            if ("desc".equalsIgnoreCase(order)) {
                cq.orderBy(cb.desc(classificationJoin.get("classificationName")));
            } else {
                cq.orderBy(cb.asc(classificationJoin.get("classificationName")));
            }

            return session.createQuery(cq).getResultList();
        }
    }
    
    public List<EmployeeDto> getEmployeesBySalary(Double minSalary, Double maxSalary, String order) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<EmployeeDto> cq = cb.createQuery(EmployeeDto.class);
            Root<Employee> root = cq.from(Employee.class);

            List<Predicate> predicates = new ArrayList<>();
            if (Objects.nonNull(minSalary)) {
                predicates.add(cb.ge(root.get("salary"), minSalary));
            }
            if (Objects.nonNull(maxSalary)) {
                predicates.add(cb.le(root.get("salary"), maxSalary));
            }
            cq.where(predicates.toArray(new Predicate[0]));

            if ("desc".equalsIgnoreCase(order)) {
                cq.orderBy(cb.desc(root.get("salary")));
            } else {
                cq.orderBy(cb.asc(root.get("salary")));
            }

            return session.createQuery(cq).getResultList();
        }
    }
    
    public List<OrderDto> getOrderByDestination(String destinationFilter, String order) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<OrderDto> cq = cb.createQuery(OrderDto.class);
            Root<Order> root = cq.from(Order.class);

            if (Objects.nonNull(destinationFilter) && !destinationFilter.isBlank()) {
                cq.where(cb.like(cb.lower(root.get("destination")), "%" + destinationFilter.toLowerCase() + "%"));
            }

            if ("desc".equalsIgnoreCase(order)) {
                cq.orderBy(cb.desc(root.get("destination")));
            } else {
                cq.orderBy(cb.asc(root.get("destination")));
            }

            return session.createQuery(cq).getResultList();
        }
    }
    
    public List<OrderDto> getShipmentsByCost(Double minCost, Double maxCost, String order) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<OrderDto> cq = cb.createQuery(OrderDto.class);
            Root<Order> root = cq.from(Order.class);

            List<Predicate> predicates = new ArrayList<>();
            if (Objects.nonNull(minCost)) {
                predicates.add(cb.ge(root.get("cost"), minCost));
            }
            if (Objects.nonNull(maxCost)) {
                predicates.add(cb.le(root.get("cost"), maxCost));
            }
            cq.where(predicates.toArray(new Predicate[0]));

            if ("desc".equalsIgnoreCase(order)) {
                cq.orderBy(cb.desc(root.get("cost")));
            } else {
                cq.orderBy(cb.asc(root.get("cost")));
            }

            return session.createQuery(cq).getResultList();
        }
    }
}
