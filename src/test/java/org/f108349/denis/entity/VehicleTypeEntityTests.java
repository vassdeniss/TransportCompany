package org.f108349.denis.entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleTypeEntityTests {
    @Test
    public void testTypeName_whenBlank_shouldReturnConstraintViolations() {
        // Arrange
        VehicleType vehicleType = new VehicleType("  ");

        // Act
        List<String> messages = EntityHelper.validate(vehicleType);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Vehicle type cannot be blank."));
    }
    
    @Test
    public void testTypeName_whenTooLong_shouldReturnConstraintViolations() {
        // Arrange
        VehicleType vehicleType = new VehicleType("A".repeat(256));

        // Act
        List<String> messages = EntityHelper.validate(vehicleType);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Vehicle type cannot exceed 255 characters."));
    }

    @Test
    public void testTypeName_whenValid_shouldNotReturnConstraintViolations() {
        // Arrange
        VehicleType vehicleType = new VehicleType("Car");

        // Act
        List<String> messages = EntityHelper.validate(vehicleType);

        // Assert
        assertTrue(messages.isEmpty(), "No constraint violations should occur for a valid VehicleType.");
    }
}
