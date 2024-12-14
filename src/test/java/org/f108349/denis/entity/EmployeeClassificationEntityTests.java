package org.f108349.denis.entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmployeeClassificationEntityTests {
    @Test
    public void testClassificationName_whenBlank_shouldReturnConstraintViolations() {
        // Arrange
        EmployeeClassification classification = new EmployeeClassification("  ");

        // Act
        List<String> messages = EntityHelper.validate(classification);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Classification cannot be blank."));
    }
    
    @Test
    public void testClassificationName_whenTooLong_shouldReturnConstraintViolations() {
        // Arrange
        EmployeeClassification classification = new EmployeeClassification("A".repeat(256));

        // Act
        List<String> messages = EntityHelper.validate(classification);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Classification cannot exceed 255 characters."));
    }

    @Test
    public void testClassificationName_whenValid_shouldNotReturnConstraintViolations() {
        // Arrange
        EmployeeClassification classification = new EmployeeClassification("Rider");

        // Act
        List<String> messages = EntityHelper.validate(classification);

        // Assert
        assertTrue(messages.isEmpty(), "No constraint violations should occur for a valid EmployeeClassification.");
    }
}
