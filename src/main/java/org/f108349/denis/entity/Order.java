package org.f108349.denis.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.f108349.denis.entity.enums.Status;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "`order`")
public class Order {
    public Order() {}

    public Order(String item, Date orderDate, Date shipmentDate, String destination, 
                 BigDecimal totalCost, BigDecimal totalWeight, Status status) {
        this.id = UUID.randomUUID().toString();
        this.item = item;
        this.orderDate = orderDate;
        this.shipmentDate = shipmentDate;
        this.destination = destination;
        this.totalCost = totalCost;
        this.totalWeight = totalWeight;
        this.status = status == null ? Status.PENDING : status;
        this.isDeleted = false;
    }

    @Id
    @Column(columnDefinition = "CHAR(36)")
    private String id;
    
    @NotNull
    @Size(min = 3, max = 255, message = "Item must be between 3 and 255 characters.")
    @Column(name = "item", nullable = false)
    private String item; 
    
    @NotNull(message = "Order date cannot be null.")
    @Column(name = "order_date", nullable = false)
    private Date orderDate;

    @Column(name = "shipment_date")
    private Date shipmentDate;

    @NotBlank(message = "Destination cannot be blank.")
    @Size(min = 3, max = 255, message = "Destination must be between 3 and 255 characters.")
    @Column(nullable = false)
    private String destination;
    
    @NotNull(message = "Total cost cannot be null.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total cost must be greater than zero.")
    @Column(name = "total_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalCost;

    @NotNull(message = "Total weight cannot be null.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total weight must be greater than zero.")
    @Column(name = "total_weight", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalWeight;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
    
    @NotNull(message = "Status cannot be null.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;
    
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "FK_customer_order"))
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "employee_id", foreignKey = @ForeignKey(name = "FK_employee_order"))
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false, foreignKey = @ForeignKey(name = "FK_company_order"))
    private Company company;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", foreignKey = @ForeignKey(name = "FK_vehicle_order"))
    private Vehicle vehicle;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem() {
        return this.item;
    }
    
    public void setItem(String item) {
        this.item = item;
    }
    
    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getShipmentDate() {
        return shipmentDate;
    }

    public void setShipmentDate(Date shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
    
    public static Order createTestOrder(int uniqueness, Company company, Customer customer) {
        Order order = new Order(
                "item" + uniqueness,
                Date.valueOf(LocalDate.now().plusDays(uniqueness)),
                Date.valueOf(LocalDate.now().plusMonths(uniqueness)),
                "somePlace" + uniqueness,
                BigDecimal.valueOf(1000 + uniqueness),
                BigDecimal.valueOf(10 + uniqueness),
                Status.SHIPPED);
        
        order.setCustomer(customer);
        order.setCompany(company);
        
        return order;
    }
}
