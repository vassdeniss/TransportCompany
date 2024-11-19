package org.f108349.denis.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "vehicle")
public class Vehicle {
    @Id
    @Column(columnDefinition = "CHAR(36)")
    private UUID id;
    
    @Column(nullable = false)
    private String model;

    @Column(name = "license_plate", nullable = false, unique = true)
    private String licensePlate;

    @Column(nullable = false)
    private int capacity;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
    
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false, foreignKey = @ForeignKey(name = "FK_vehicle_company"))
    private Company company;

    @ManyToOne
    @JoinColumn(name = "vehicle_type_id", nullable = false, foreignKey = @ForeignKey(name = "FK_vehicle_type"))
    private VehicleType vehicleType;
}
