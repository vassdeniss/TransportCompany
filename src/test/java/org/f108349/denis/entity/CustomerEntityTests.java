package org.f108349.denis.entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerEntityTests {
    @Test
    public void testFirstName_whenTooLong_shouldReturnConstraintViolations() {
        // Arrange
        Customer customer = new Customer("A".repeat(256), "Doe", "john.doe@example.com", 
                "+359 87 123 4567", "Some Address");

        // Act
        List<String> messages = EntityHelper.validate(customer);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("First name cannot exceed 255 characters."));
    }

    @Test
    public void testFirstName_whenDoesNotStartWithCapital_shouldReturnConstraintViolations() {
        // Arrange
        Customer customer = new Customer("john", "Doe", "john.doe@example.com", 
                "+359 87 123 4567", "Some Address");

        // Act
        List<String> messages = EntityHelper.validate(customer);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("First name hast to start with a capital letter."));
    }

    @Test
    public void testLastName_whenTooLong_shouldReturnConstraintViolations() {
        // Arrange
        Customer customer = new Customer("John", "B".repeat(256), "john.doe@example.com", 
                "+359 87 123 4567", "Some Address");

        // Act
        List<String> messages = EntityHelper.validate(customer);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Last name cannot exceed 255 characters."));
    }

    @Test
    public void testLastName_whenDoesNotStartWithCapital_shouldReturnConstraintViolations() {
        // Arrange
        Customer customer = new Customer("John", "doe", "john.doe@example.com", 
                "+359 87 123 4567", "Some Address");

        // Act
        List<String> messages = EntityHelper.validate(customer);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Last name hast to start with a capital letter."));
    }

    @Test
    public void testEmail_whenInvalid_shouldReturnConstraintViolations() {
        // Arrange
        Customer customer = new Customer("John", "Doe", "invalid-email", 
                "+359 87 123 4567", "Some Address");

        // Act
        List<String> messages = EntityHelper.validate(customer);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Please provide a valid email address."));
    }

    @Test
    public void testPhone_whenInvalid_shouldReturnConstraintViolations() {
        // Arrange
        Customer customer = new Customer("John", "Doe", "john.doe@example.com", 
                "+123 456 789", "Some Address");

        // Act
        List<String> messages = EntityHelper.validate(customer);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Phone number must be valid and can include an international prefix."));
    }

    @Test
    public void testAddress_whenBlank_shouldReturnConstraintViolations() {
        // Arrange
        Customer customer = new Customer("John", "Doe", 
                "john.doe@example.com", "+359 87 123 4567", "  ");

        // Act
        List<String> messages = EntityHelper.validate(customer);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Address cannot be blank."));
    }

    @Test
    public void testAddress_whenTooLong_shouldReturnConstraintViolations() {
        // Arrange
        Customer customer = new Customer("John", "Doe", "john.doe@example.com", 
                "+359 87 123 4567", "Street ".repeat(50));

        // Act
        List<String> messages = EntityHelper.validate(customer);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Address cannot exceed 255 characters."));
    }

    @Test
    public void testAllFields_whenValid_shouldNotReturnConstraintViolations() {
        // Arrange
        Customer customer = new Customer("John", "Doe", "john.doe@example.com", 
                "+359 87 123 4567", "Valid Address");

        // Act
        List<String> messages = EntityHelper.validate(customer);

        // Assert
        assertTrue(messages.isEmpty(), "No constraint violations should occur for a valid Customer.");
    }
}
