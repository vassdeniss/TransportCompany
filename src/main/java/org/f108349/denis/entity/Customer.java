package org.f108349.denis.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import org.f108349.denis.entity.validation.Phone;

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

    @Size(max = 255, message = "First name cannot exceed 255 characters.")
    @Pattern(regexp = "^([A-Z]).*", message = "First name hast to start with a capital letter.")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Size(max = 255, message = "Last name cannot exceed 255 characters.")
    @Pattern(regexp = "^([A-Z]).*", message = "Last name hast to start with a capital letter.")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Email(message = "Please provide a valid email address.")
    @Column(nullable = false)
    private String email;

    @Phone
    @Column
    private String phone;

    @NotBlank(message = "Address cannot be blank.")
    @Size(max = 255, message = "Address cannot exceed 255 characters.")
    @Column(nullable = false)
    private String address;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }
    
    public static Customer createTestCustomer(int uniqueness) {
        return new Customer(
                "FirstName" + uniqueness,
                "LastName" + uniqueness,
                "email" + uniqueness + "@mail.bg",
                "+359 88 215261" + uniqueness,
                "street" + uniqueness);
    }
}
