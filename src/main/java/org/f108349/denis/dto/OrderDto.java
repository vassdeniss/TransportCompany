package org.f108349.denis.dto;

import org.f108349.denis.entity.Order;
import org.f108349.denis.entity.Company;
import org.f108349.denis.entity.Customer;
import org.f108349.denis.entity.Employee;
import org.f108349.denis.entity.Vehicle;
import org.f108349.denis.entity.enums.Status;

import java.math.BigDecimal;
import java.sql.Date;

public class OrderDto {
    private String id;
    private String item;
    private Date orderDate;
    private Date shipmentDate;
    private String destination;
    private BigDecimal totalCost;
    private BigDecimal totalWeight;
    private Status status;
    private String customerId;
    private String employeeId;
    private String companyId;
    private String vehicleId;
    private Customer customer;
    private Employee employee;
    private Company company;
    private Vehicle vehicle;

    public OrderDto() {}
    
    public OrderDto(String item, Date orderDate, Date shipmentDate, String destination,
                    BigDecimal totalCost, BigDecimal totalWeight, 
                    String customerId, String employeeId, String companyId, String vehicleId, Status status) {
        this.item = item;
        this.orderDate = orderDate;
        this.shipmentDate = shipmentDate;
        this.destination = destination;
        this.totalCost = totalCost;
        this.totalWeight = totalWeight;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.companyId = companyId;
        this.vehicleId = vehicleId;
        this.status = status;
    }

    public OrderDto(Order order) {
        this.id = order.getId();
        this.item = order.getItem();
        this.orderDate = order.getOrderDate();
        this.shipmentDate = order.getShipmentDate();
        this.destination = order.getDestination();
        this.totalCost = order.getTotalCost();
        this.totalWeight = order.getTotalWeight();
        this.status = order.getStatus();
        this.customerId = order.getCustomer() != null ? order.getCustomer().getId() : null;
        this.employeeId = order.getEmployee() != null ? order.getEmployee().getId() : null;
        this.companyId = order.getCompany() != null ? order.getCompany().getId() : null;
        this.vehicleId = order.getVehicle() != null ? order.getVehicle().getId() : null;
        this.customer = order.getCustomer();
        this.employee = order.getEmployee();
        this.company = order.getCompany();
        this.vehicle = order.getVehicle();
    }

    public String getId() {
        return this.id;
    }

    public String getItem() {
        return this.item;
    }
    
    public void setItem(String item) {
        this.item = item;
    }
    
    public Date getOrderDate() {
        return this.orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getShipmentDate() {
        return this.shipmentDate;
    }

    public void setShipmentDate(Date shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public BigDecimal getTotalCost() {
        return this.totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getTotalWeight() {
        return this.totalWeight;
    }

    public void setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getVehicleId() {
        return this.vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public String toString() {
        return "Order Details: " +
                "\n    Item: " + this.item +
                "\n    Order Date: " + this.orderDate +
                "\n    Shipment Date: " + this.shipmentDate +
                "\n    Destination: " + this.destination +
                "\n    Total Cost: " + this.totalCost +
                "\n    Total Weight: " + this.totalWeight +
                "\n    Status: " + this.status +
                "\n    Customer: " + (this.customer != null ? this.customer.getFirstName() + this.customer.getLastName() : "N/A") +
                "\n    Employee: " + (this.employee != null ? this.employee.getFirstName() + this.employee.getLastName() : "N/A") +
                "\n    Company: " + (this.company != null ? this.company.getName() : "N/A") +
                "\n    Vehicle: " + (this.vehicle != null ? this.vehicle.getLicensePlate() : "N/A");
    }
}
