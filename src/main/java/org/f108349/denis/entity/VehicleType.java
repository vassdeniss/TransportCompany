package org.f108349.denis.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "vehicle_type")
public class VehicleType {
    public VehicleType() {}
    
    public VehicleType(String typeName) {
        this.id = UUID.randomUUID().toString();
        this.typeName = typeName;
        this.isDeleted = false;
    }
    
    @Id
    @Column(columnDefinition = "CHAR(36)")
    private String id;

    @Column(name = "type_name", nullable = false, unique = true)
    private String typeName;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    public String getId() {
        return this.id;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }
}
