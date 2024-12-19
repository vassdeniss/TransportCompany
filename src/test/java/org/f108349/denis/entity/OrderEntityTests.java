package org.f108349.denis.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderEntityTests {
    @Test
    public void testItem_whenReceedsMinLength_shouldReturnConstraintViolations() {
        // Arrange
        Order order = new Order("a", Date.valueOf("2024-12-16"), Date.valueOf("2024-12-18"), "somewhere", 
                BigDecimal.valueOf(100), BigDecimal.valueOf(10), null);

        // Act
        List<String> messages = EntityHelper.validate(order);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Item must be between 3 and 255 characters."));
    }
    
    @Test
    public void testItem_whenExceedsMaxLength_shouldReturnConstraintViolations() {
        // Arrange
        Order order = new Order("b".repeat(256), Date.valueOf("2024-12-16"), Date.valueOf("2024-12-18"), "somewhere", 
                BigDecimal.valueOf(100), BigDecimal.valueOf(10), null);

        // Act
        List<String> messages = EntityHelper.validate(order);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Item must be between 3 and 255 characters."));
    }
    
    @Test
    public void testOrderDate_whenNull_shouldReturnConstraintViolations() {
        // Arrange
        Order order = new Order("bricks", null, Date.valueOf("2024-12-16"), "Some destination", 
                BigDecimal.valueOf(100), BigDecimal.valueOf(10), null);

        // Act
        List<String> messages = EntityHelper.validate(order);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Order date cannot be null."));
    }
    
    @Test
    public void testDestination_whenReceedsMinLength_shouldReturnConstraintViolations() {
        // Arrange
        Order order = new Order("bricks", Date.valueOf("2024-12-16"), Date.valueOf("2024-12-18"), "aa", 
                BigDecimal.valueOf(100), BigDecimal.valueOf(10), null);

        // Act
        List<String> messages = EntityHelper.validate(order);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Destination must be between 3 and 255 characters."));
    }
    
    @Test
    public void testDestination_whenExceedsMaxLength_shouldReturnConstraintViolations() {
        // Arrange
        Order order = new Order("bricks", Date.valueOf("2024-12-16"), Date.valueOf("2024-12-18"), "a".repeat(256), 
                BigDecimal.valueOf(100), BigDecimal.valueOf(10), null);

        // Act
        List<String> messages = EntityHelper.validate(order);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Destination must be between 3 and 255 characters."));
    }
    
    @Test
    public void testTotalCost_whenNull_shouldReturnConstraintViolations() {
        // Arrange
        Order order = new Order("bricks", Date.valueOf("2024-12-16"), Date.valueOf("2024-12-18"), "Some destination", 
                null, BigDecimal.valueOf(10), null);

        // Act
        List<String> messages = EntityHelper.validate(order);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Total cost cannot be null."));
    }
    
    @Test
    public void testTotalCost_whenZero_shouldReturnConstraintViolations() {
        // Arrange
        Order order = new Order("bricks", Date.valueOf("2024-12-16"), Date.valueOf("2024-12-18"), "Some destination", 
                BigDecimal.valueOf(0), BigDecimal.valueOf(10), null);

        // Act
        List<String> messages = EntityHelper.validate(order);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Total cost must be greater than zero."));
    }
    
    @Test
    public void testTotalWeight_whenNull_shouldReturnConstraintViolations() {
        // Arrange
        Order order = new Order("bricks", Date.valueOf("2024-12-16"), Date.valueOf("2024-12-18"), "Some destination", 
                BigDecimal.valueOf(15), null, null);

        // Act
        List<String> messages = EntityHelper.validate(order);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Total weight cannot be null."));
    }
    
    @Test
    public void testTotalWeight_whenZero_shouldReturnConstraintViolations() {
        // Arrange
        Order order = new Order("bricks", Date.valueOf("2024-12-16"), Date.valueOf("2024-12-18"), "Some destination", 
                BigDecimal.valueOf(10), BigDecimal.valueOf(0), null);

        // Act
        List<String> messages = EntityHelper.validate(order);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Total weight must be greater than zero."));
    }
    
    @Test
    public void testOrder_whenValid_shouldNotReturnConstraintViolations() {
        // Arrange
        Order order = new Order("bricks", Date.valueOf("2024-12-16"), Date.valueOf("2024-12-18"), "Some destination", 
                BigDecimal.valueOf(100), BigDecimal.valueOf(10), null);

        // Act
        List<String> messages = EntityHelper.validate(order);

        // Assert
        assertTrue(messages.isEmpty());
    }
}
