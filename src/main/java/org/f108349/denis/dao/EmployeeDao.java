package org.f108349.denis.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.f108349.denis.dto.EmployeeDto;
import org.f108349.denis.entity.Company;
import org.f108349.denis.entity.Employee;
import org.f108349.denis.entity.EmployeeClassification;
import org.f108349.denis.entity.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class EmployeeDao extends BaseDao<EmployeeDto, Employee> {
    private final SessionFactory sessionFactory;
    
    public EmployeeDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    public boolean saveEmployee(EmployeeDto employeeDto) {
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            
            EmployeeClassification classification = session
                    .createQuery("select ec from EmployeeClassification ec where ec.isDeleted = false and id = :id", EmployeeClassification.class)
                    .setParameter("id", employeeDto.getEmployeeClassificationId())
                    .uniqueResultOptional()
                    .orElse(null);             
            if (classification == null) {
                System.out.println("Invalid classification ID. Employee not saved.");
                tx.rollback();
                return false;
            }
            
            Company company = session
                    .createQuery("select c from Company c where c.isDeleted = false and id = :id", Company.class)
                    .setParameter("id", employeeDto.getCompanyId())
                    .uniqueResultOptional()
                    .orElse(null); 
            if (company == null) {
                System.out.println("Invalid company ID. Employee not saved.");
                tx.rollback();
                return false;
            }
            
            Employee employee = new Employee(employeeDto.getFirstName(), employeeDto.getLastName(), 
                    employeeDto.getEmail(), employeeDto.getPhone(), employeeDto.getHireDate(), employeeDto.getSalary());
            employee.setCompany(company);
            employee.setEmployeeClassification(classification);
            session.persist(employee);
            tx.commit();
            return true;
        }
    }
    
    public EmployeeDto getEmployeeByIdWhereNotDeleted(String id) {
        EmployeeDto employee;
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            employee = this.getByIdWhereNotDeleted(session, id, EmployeeDto.class, Employee.class);
            tx.commit();
        }
        
        return employee;
    }
    
    public List<EmployeeDto> getAllEmployeesWhereNotDeleted() {
        List<EmployeeDto> employees;
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            employees = this.getAllWhereNotDeleted(session, EmployeeDto.class, Employee.class);
            tx.commit();
        }
        
        return employees;
    }
    
    public List<Object[]> getAllEmployeeOrders() {
        List<Object[]> list;
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
            Root<Employee> root = cq.from(Employee.class);

            Join<Employee, Order> orderJoin = root.join("orders");
            Join<Employee, Company> companyJoin = root.join("company");
            
            cq.multiselect(
                    cb.concat(
                            cb.concat(root.get("firstName"), " "),
                            root.get("lastName")
                    ), 
                    companyJoin.get("name"), 
                    cb.count(orderJoin.get("id")),
                    cb.sumAsDouble(orderJoin.get("totalCost"))).groupBy(root.get("id"));
            
            list = session.createQuery(cq).getResultList();            
            
            tx.commit();
        }
        
        return list;
    }
    
    public void updateEmployee(EmployeeDto employeeDto) {
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Employee employee = session.get(Employee.class, employeeDto.getId());
            
            employee.setEmail(employeeDto.getEmail());
            employee.setPhone(employeeDto.getPhone());
            employee.setFirstName(employeeDto.getFirstName());
            employee.setLastName(employeeDto.getLastName());
            employee.setHireDate(employeeDto.getHireDate());
            employee.setSalary(employeeDto.getSalary());
            
            this.applyCompanyAndClassification(session, employeeDto, employee);
            
            session.merge(employee);
            tx.commit();
        }
    }
    
    public void deleteEmployee(String id) {
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Employee employee = session.get(Employee.class, id);
            employee.setDeleted(true);
            session.merge(employee);
            tx.commit();
        }
    }
    
    private void applyCompanyAndClassification(Session session, EmployeeDto employeeDto, Employee employee) {
        // TODO: replace base with else throw
        // TODO: could go to base
        if (employeeDto.getCompanyId() != null) {
            Company company = session.createQuery(
                "SELECT c FROM Company c WHERE c.id = :id AND c.isDeleted = false", Company.class)
                .setParameter("id", employeeDto.getCompanyId())
                .uniqueResultOptional()
                .orElseThrow(() -> new IllegalArgumentException("Invalid company ID provided."));
            employee.setCompany(company);
        }

        if (employeeDto.getEmployeeClassificationId() != null) {
            EmployeeClassification classification = session.createQuery(
                "SELECT ec FROM EmployeeClassification ec WHERE ec.id = :id AND ec.isDeleted = false", EmployeeClassification.class)
                .setParameter("id", employeeDto.getEmployeeClassificationId())
                .uniqueResultOptional()
                .orElseThrow(() -> new IllegalArgumentException("Invalid classification ID provided"));
            employee.setEmployeeClassification(classification);
        }
    }
}
