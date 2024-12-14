package org.f108349.denis.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.f108349.denis.dto.EmployeeClassificationDto;
import org.f108349.denis.entity.Employee;
import org.f108349.denis.entity.EmployeeClassification;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class EmployeeClassificationDao extends BaseDao<EmployeeClassificationDto, EmployeeClassification> {
    private final SessionFactory sessionFactory;
    
    public EmployeeClassificationDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    public void saveEmployeeClassification(EmployeeClassificationDto employeeClassificationDto) {
        EmployeeClassification employeeClassification = 
                new EmployeeClassification(employeeClassificationDto.getClassificationName());
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(employeeClassification);
            tx.commit();
        }
    }
    
    public EmployeeClassificationDto getEmployeeClassificationByIdWhereNotDeleted(String id) {
        EmployeeClassificationDto employeeClassification;
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            employeeClassification = this.getByIdWhereNotDeleted(session, id, EmployeeClassificationDto.class, EmployeeClassification.class);          
            tx.commit();
        }
        
        return employeeClassification;
    }
    
    public List<EmployeeClassificationDto> getAllEmployeeClassificationsWhereNotDeleted() {
        List<EmployeeClassificationDto> employeeClassifications;
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            employeeClassifications = this.getAllWhereNotDeleted(session, EmployeeClassificationDto.class, EmployeeClassification.class);
            tx.commit();
        }
        
        return employeeClassifications;
    }
    
    public void deleteEmployeeClassification(String id) {
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            EmployeeClassification employeeClassification = session.get(EmployeeClassification.class, id);

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> query = cb.createQuery(Long.class);
            Root<Employee> root = query.from(Employee.class);
            
            query.select(cb.count(root))
                    .where(cb.equal(root.get("employeeClassification").get("id"), id));
            
            Long count = session.createQuery(query).getSingleResult();
            
            if (count != null && count > 0) {
                throw new IllegalStateException("Cannot delete classification; employees are still assigned.");
            }
            
            employeeClassification.setDeleted(true);
            session.merge(employeeClassification);
            tx.commit();
        }
    }
}
