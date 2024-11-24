package org.f108349.denis.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehicle_type")
public class VehicleType {
    @Id
    @Column(columnDefinition = "CHAR(36)")
    private String id;

    @Column(name = "type_name", nullable = false, unique = true)
    private String typeName;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
}
