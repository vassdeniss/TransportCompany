package org.f108349.denis.entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleEntityTests {
    @Test
    public void testModel_whenBlank_shouldReturnConstraintViolation() {
        // Arrange
        Vehicle vehicle = new Vehicle(" ", "CA1234PB", 10);
        
        // Act
        List<String> messages = EntityHelper.validate(vehicle);
        
        // Assert 
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Model cannot be blank."));
    }

    @Test
    public void testModel_whenTooLong_shouldReturnConstraintViolation() {
        // Arrange
        String longModel = "A".repeat(256);
        Vehicle vehicle = new Vehicle(longModel, "CA1234PB", 10);
        
        // Act
        List<String> messages = EntityHelper.validate(vehicle);
        
        // Assert 
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Model cannot exceed 255 characters."));
    }

    @Test
    public void testLicensePlate_whenInvalidFormat_shouldReturnConstraintViolation() {
        // Arrange
        Vehicle vehicle = new Vehicle("ValidModel", "C123PB", 10);
        
        // Act
        List<String> messages = EntityHelper.validate(vehicle);
        
        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("License plate must follow the Bulgarian format."));
    }

    @Test
    public void testCapacity_whenTooLow_shouldReturnConstraintViolation() {
        // Arrange
        Vehicle vehicle = new Vehicle("ValidModel", "CA1234PB", 0);
        
        // Act
        List<String> messages = EntityHelper.validate(vehicle);
        
        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Capacity must be at least 1."));
    }

    @Test
    public void testCapacity_whenTooHigh_shouldReturnConstraintViolation() {
        // Arrange
        Vehicle vehicle = new Vehicle("ValidModel", "CA1234PB", 1000);
        
        // Act
        List<String> messages = EntityHelper.validate(vehicle);
        
        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Capacity cannot exceed 999."));
    }

    @Test
    public void testAllFields_whenValid_shouldNotReturnConstraintViolations() {
        // Arrange
        Vehicle vehicle = new Vehicle("ValidModel", "CA1234PB", 50);
        
        // Act
        List<String> messages = EntityHelper.validate(vehicle);
        
        // Assert
        assertTrue(messages.isEmpty(), "No constraint violations should occur for a valid Vehicle.");
    }
}
