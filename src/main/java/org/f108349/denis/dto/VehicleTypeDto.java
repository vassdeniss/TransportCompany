package org.f108349.denis.dto;

import org.f108349.denis.entity.VehicleType;

public class VehicleTypeDto {
    private String id;
    private String typeName;
    
    public VehicleTypeDto(String typeName) {
        this.typeName = typeName;
    }
    
    public VehicleTypeDto(VehicleType vehicleType) {
        this.id = vehicleType.getId();
        this.typeName = vehicleType.getTypeName();
    }

    public String getId() {
        return this.id;
    }
    
    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return "Vehicle type " + this.typeName;
    }
}
