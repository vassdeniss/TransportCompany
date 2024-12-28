package org.f108349.denis.dto;

import org.f108349.denis.entity.Order;
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
    private CustomerDto customer;
    private EmployeeDto employee;
    private CompanyDto company;
    private VehicleDto vehicle;

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
        this.customer = new CustomerDto(order.getCustomer());
        this.employee = new EmployeeDto(order.getEmployee());
        this.company = new CompanyDto(order.getCompany());
        this.vehicle = new VehicleDto(order.getVehicle());
    }

    public String getId() {
        return this.id;
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

    public CustomerDto getCustomer() {
        return this.customer;
    }

    public void setCustomer(CustomerDto customer) {
        this.customer = customer;
    }

    public EmployeeDto getEmployee() {
        return this.employee;
    }

    public void setEmployee(EmployeeDto employee) {
        this.employee = employee;
    }

    public CompanyDto getCompany() {
        return this.company;
    }

    public void setCompany(CompanyDto company) {
        this.company = company;
    }

    public VehicleDto getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(VehicleDto vehicle) {
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
