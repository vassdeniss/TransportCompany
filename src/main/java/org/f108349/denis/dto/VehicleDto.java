package org.f108349.denis.dto;

import org.f108349.denis.entity.Vehicle;

public class VehicleDto {
    private String id;
    private String model;
    private String licensePlate;
    private int capacity;
    private String companyId;
    private String vehicleTypeId;
    
    public VehicleDto(String model, String licensePlate, 
                      int capacity, String companyId, String vehicleTypeId) {
        this.model = model;
        this.licensePlate = licensePlate;
        this.capacity = capacity;
        this.companyId = companyId;
        this.vehicleTypeId = vehicleTypeId;
    }
    
    public VehicleDto(Vehicle vehicle) {
        this.id = vehicle.getId();
        this.model = vehicle.getModel();
        this.licensePlate = vehicle.getLicensePlate();
        this.capacity = vehicle.getCapacity();
        this.companyId = vehicle.getCompany() != null ? vehicle.getCompany().getId() : null;
        this.vehicleTypeId = vehicle.getVehicleType() != null ? vehicle.getVehicleType().getId() : null;
    }

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

    public String getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
    
    public String getVehicleTypeId() {
        return this.vehicleTypeId;
    }
    
    public void setVehicleTypeId(String vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }
    
    @Override
    public String toString() {
        return "Information about " + this.licensePlate + ":" + 
               "\n    License Plate is '" + this.licensePlate + '\'' + 
               "\n    Model is '" + this.model + '\'' + 
               "\n    Capacity is '" + this.capacity + '\'';
    }
}
