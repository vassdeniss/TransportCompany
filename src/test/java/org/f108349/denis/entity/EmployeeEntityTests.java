package org.f108349.denis.entity;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmployeeEntityTests {
    @Test
    public void testFirstName_whenTooLong_shouldReturnConstraintViolations() {
        // Arrange
        Employee employee = new Employee("A".repeat(256), "Doe", "john.doe@example.com", 
                "+359 87 123 4567", Date.valueOf(LocalDate.now()), 1300);

        // Act
        List<String> messages = EntityHelper.validate(employee);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("First name cannot exceed 255 characters."));
    }

    @Test
    public void testFirstName_whenDoesNotStartWithCapital_shouldReturnConstraintViolations() {
        // Arrange
        Employee employee = new Employee("john", "Doe", "john.doe@example.com", 
                "+359 87 123 4567", Date.valueOf(LocalDate.now()), 1300);

        // Act
        List<String> messages = EntityHelper.validate(employee);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("First name hast to start with a capital letter."));
    }

    @Test
    public void testLastName_whenTooLong_shouldReturnConstraintViolations() {
        // Arrange
        Employee employee = new Employee("John", "B".repeat(256), "john.doe@example.com", 
                "+359 87 123 4567", Date.valueOf(LocalDate.now()), 1300);

        // Act
        List<String> messages = EntityHelper.validate(employee);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Last name cannot exceed 255 characters."));
    }

    @Test
    public void testLastName_whenDoesNotStartWithCapital_shouldReturnConstraintViolations() {
        // Arrange
        Employee employee = new Employee("John", "doe", "john.doe@example.com", 
                "+359 87 123 4567", Date.valueOf(LocalDate.now()), 1300);

        // Act
        List<String> messages = EntityHelper.validate(employee);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Last name hast to start with a capital letter."));
    }

    @Test
    public void testEmail_whenInvalid_shouldReturnConstraintViolations() {
        // Arrange
        Employee employee = new Employee("John", "Doe", "invalid-email", 
                "+359 87 123 4567", Date.valueOf(LocalDate.now()), 1300);

        // Act
        List<String> messages = EntityHelper.validate(employee);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Please provide a valid email address."));
    }

    @Test
    public void testPhone_whenInvalid_shouldReturnConstraintViolations() {
        // Arrange
        Employee employee = new Employee("John", "Doe", "john.doe@example.com", 
                "+123 456 789", Date.valueOf(LocalDate.now()), 1300);

        // Act
        List<String> messages = EntityHelper.validate(employee);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Phone number must be valid and can include an international prefix."));
    }

    @Test
    public void testHireDate_whenInTheFuture_shouldReturnConstraintViolations() {
        // Arrange
        Employee employee = new Employee("John", "Doe", 
                "john.doe@example.com", "+359 87 123 4567", Date.valueOf(LocalDate.now().plusDays(1)), 1300);

        // Act
        List<String> messages = EntityHelper.validate(employee);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Hire date cannot be in the future."));
    }
    
    @Test
    public void testSalary_whenNegative_shouldReturnConstraintViolations() {
        // Arrange
        Employee employee = new Employee("John", "Doe", 
                "john.doe@example.com", "+359 87 123 4567", Date.valueOf(LocalDate.now()), -2);

        // Act
        List<String> messages = EntityHelper.validate(employee);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Salary must be a positive value."));
    }

    @Test
    public void testAllFields_whenValid_shouldNotReturnConstraintViolations() {
        // Arrange
        Employee employee = new Employee("John", "Doe", "john.doe@example.com", 
                "+359 87 123 4567", Date.valueOf(LocalDate.now()), 1300);

        // Act
        List<String> messages = EntityHelper.validate(employee);

        // Assert
        assertTrue(messages.isEmpty(), "No constraint violations should occur for a valid Employee.");
    }
}
