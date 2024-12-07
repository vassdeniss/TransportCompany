package org.f108349.denis.dto;

import org.f108349.denis.entity.Company;
import org.f108349.denis.entity.Vehicle;
import org.f108349.denis.entity.VehicleType;

public class VehicleDto {
    private String id;
    private String model;
    private String licensePlate;
    private int capacity;
    private String companyId;
    private Company company;
    private String vehicleTypeId;
    private VehicleType vehicleType;
    
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
        this.company = vehicle.getCompany();
        this.vehicleType = vehicle.getVehicleType();
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
    
    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
    
    public String getVehicleTypeId() {
        return this.vehicleTypeId;
    }
    
    public void setVehicleTypeId(String vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }
    
    public VehicleType getVehicleType() {
        return this.vehicleType;
    }
    
    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
    
    @Override
    public String toString() {
        return "Information about " + this.licensePlate + ":" + 
               "\n    License Plate is '" + this.licensePlate + '\'' + 
               "\n    Model is '" + this.model + '\'' + 
               "\n    Capacity is '" + this.capacity + '\'' + 
               "\n    Type is '" + this.vehicleType.getTypeName() + '\'' +
               "\n    Company is '" + this.company.getName() + '\'';
    }
}
