package org.f108349.denis.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "customer")
public class Customer {
    public Customer() {}
    
    public Customer(String firstName, String lastName, String email, String phone, String address) {
        this.id = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.isDeleted = false;
    }

    @Id
    @Column(columnDefinition = "CHAR(36)")
    private String id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
    
    @Override
public String toString() {
    return "Customer {" + 
           "\n    id='" + id + '\'' + 
           ",\n    firstName='" + firstName + '\'' + 
           ",\n    lastName='" + lastName + '\'' + 
           ",\n    email='" + email + '\'' + 
           ",\n    phone='" + phone + '\'' + 
           ",\n    address='" + address + '\'' + 
           ",\n    isDeleted=" + isDeleted + 
           "\n}";
}

}
