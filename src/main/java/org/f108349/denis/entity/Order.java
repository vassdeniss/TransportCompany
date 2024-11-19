package org.f108349.denis.entity;

import jakarta.persistence.*;
import org.f108349.denis.entity.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "`order`")
public class Order {
    @Id
    @Column(columnDefinition = "CHAR(36)")
    private UUID id;
    
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "shipment_date")
    private LocalDateTime shipmentDate;

    @Column(nullable = false)
    private String destination;
    
    @Column(name = "total_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalCost;

    @Column(name = "total_weigth", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalWeigth;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
    
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

    @ManyToOne
    @JoinColumn(name = "vehicle_type_id", foreignKey = @ForeignKey(name = "FK_vehicle_type_order"))
    private VehicleType vehicleType;
}
