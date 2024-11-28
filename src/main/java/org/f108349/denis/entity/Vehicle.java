package org.f108349.denis.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "vehicle")
public class Vehicle {
    public Vehicle() {}
    
    public Vehicle(String model, String licensePlate, int capacity) {
        this.id = UUID.randomUUID().toString();
        this.model = model;
        this.licensePlate = licensePlate;
        this.capacity = capacity;
    }
    
    @Id
    @Column(columnDefinition = "CHAR(36)")
    private String id;
    
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

    public String getId() {
        return this.id;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLicensePlate() {
        return this.licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public VehicleType getVehicleType() {
        return this.vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
}
