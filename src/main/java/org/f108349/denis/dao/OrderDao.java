package org.f108349.denis.dao;

import jakarta.persistence.criteria.*;
import org.f108349.denis.dto.OrderDto;
import org.f108349.denis.entity.*;
import org.f108349.denis.entity.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.Date;
import java.util.List;

public class OrderDao extends BaseDao<OrderDto, Order> {
    private final SessionFactory sessionFactory;
    
    public OrderDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    public void saveOrder(OrderDto orderDto) {
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            
            Customer customer = session
                    .createQuery("select c from Customer c where c.isDeleted = false and id = :id", Customer.class)
                    .setParameter("id", orderDto.getCustomerId())
                    .uniqueResultOptional()
                    .orElse(null); 
            if (customer == null) {
                System.out.println("Invalid customer ID. Vehicle not saved.");
                tx.rollback();
                return;
            }
            
            Employee employee = session
                    .createQuery("select e from Employee e where e.isDeleted = false and id = :id", Employee.class)
                    .setParameter("id", orderDto.getEmployeeId())
                    .uniqueResultOptional()
                    .orElse(null); 
            if (employee == null) {
                System.out.println("Invalid employee ID. Vehicle not saved.");
                tx.rollback();
                return;
            }
            
            Company company = session
                    .createQuery("select c from Company c where c.isDeleted = false and id = :id", Company.class)
                    .setParameter("id", orderDto.getCompanyId())
                    .uniqueResultOptional()
                    .orElse(null); 
            if (company == null) {
                System.out.println("Invalid company ID. Vehicle not saved.");
                tx.rollback();
                return;
            }
            
            Vehicle vehicle = session
                    .createQuery("select v from Vehicle v where v.isDeleted = false and id = :id", Vehicle.class)
                    .setParameter("id", orderDto.getVehicleId())
                    .uniqueResultOptional()
                    .orElse(null); 
            if (vehicle == null) {
                System.out.println("Invalid vehicle ID. Vehicle not saved.");
                tx.rollback();
                return;
            }
            
            Order order = new Order(orderDto.getItem(), orderDto.getOrderDate(), orderDto.getShipmentDate(), orderDto.getDestination(),
                orderDto.getTotalCost(), orderDto.getTotalWeight(), null);
            order.setCustomer(customer);
            order.setEmployee(employee);
            order.setCompany(company);
            order.setVehicle(vehicle);
            
            session.persist(order);
            tx.commit();
        }
    }
    
    public OrderDto getOrderByIdWhereNotDeleted(String id) {
        OrderDto order;
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            order = this.getByIdWhereNotDeleted(session, id, OrderDto.class, Order.class);
            tx.commit();
        }
        
        return order;
    }
    
    public List<OrderDto> getAllOrdersWhereNotDeleted() {
        List<OrderDto> orders;
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            orders = this.getAllWhereNotDeleted(session, OrderDto.class, Order.class);
            tx.commit();
        }
        
        return orders;
    }
    
    public List<OrderDto> getAllOrdersEagerWhereNotDeleted() {
        List<OrderDto> orders;
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            
            String hql = "SELECT new org.f108349.denis.dto.OrderDto(o) " +
                         "FROM Order o " +
                         "LEFT JOIN FETCH o.company c " +
                         "LEFT JOIN FETCH o.customer cu " +
                         "LEFT JOIN FETCH o.employee e " +
                         "LEFT JOIN FETCH e.employeeClassification ec " +
                         "LEFT JOIN FETCH o.vehicle v " +
                         "WHERE o.isDeleted = false";

            orders = session.createQuery(hql, OrderDto.class).getResultList();      
            
            tx.commit();
        }
        
        return orders;
    }
    
    public List<Object[]> getTotalIncomeForGivenTimePeriod(Date startDate, Date endDate) {
        List<Object[]> list;
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
            Root<Order> root = cq.from(Order.class);
            
            Join<Order, Company> join = root.join("company");
            
            cq.multiselect(
                join.get("name"),
                cb.sum(root.get("totalCost")),
                cb.literal(startDate),
                cb.literal(endDate));
                    
            Predicate dateRangePredicate = cb.between(
                root.get("orderDate"),
                cb.literal(startDate),
                cb.literal(endDate)
            );
            
            cq.where(dateRangePredicate).groupBy(join.get("name"));
            
            list = session.createQuery(cq).getResultList();
            tx.commit();
        }
        
        return list;
    }
    
    public void updateOrder(OrderDto orderDto) {
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Order order = session.get(Order.class, orderDto.getId());
            
            order.setItem(orderDto.getItem());
            order.setOrderDate(orderDto.getOrderDate());
            order.setShipmentDate(orderDto.getShipmentDate());
            order.setDestination(orderDto.getDestination());
            order.setTotalCost(orderDto.getTotalCost());
            order.setTotalWeight(orderDto.getTotalWeight());
            order.setStatus(orderDto.getStatus());
            
            session.merge(order);
            tx.commit();
        }
    }
    
    public void deleteOrder(String id) {
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Order order = session.get(Order.class, id);
            order.setDeleted(true);
            session.merge(order);
            tx.commit();
        }
    }
}
