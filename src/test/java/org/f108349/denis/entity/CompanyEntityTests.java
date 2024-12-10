package org.f108349.denis.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class CompanyEntityTests {
    @Test
    public void testCompanyName_whenTooShort_shouldReturnConstraintViolations() {
        // Arrange
        Company company = new Company("Aa", "123456789", 
                "someMail@mail.bg", "+359 88 1842866");

        // Act
        List<String> messages = this.validate(company);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Company name has to be between 3 and 255 characters."));
    }
    
    @Test
    public void testCompanyName_whenTooLong_shouldReturnConstraintViolations() {
        // Arrange
        Company company = new Company("A".repeat(256), "123456789", 
                "someMail@mail.bg", "+359 88 1842866");

        // Act
        List<String> messages = this.validate(company);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Company name has to be between 3 and 255 characters."));
    }
    
    @Test
    public void testCompanyName_whenDoesNotStartWithCapital_shouldReturnConstraintViolations() {
        // Arrange
        Company company = new Company("nestle", "123456789", 
                "someMail@mail.bg", "+359 88 1842866");

        // Act
        List<String> messages = this.validate(company);
        
        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Company cannot be blank and has to start with a capital letter."));
    }
    
    @Test
    public void testRegistrationNo_whenInvalid_shouldReturnConstraintViolations() {
        // Arrange
        Company company = new Company("Nestle", "022@222-22-2", 
                "someMail@mail.bg", "+359 88 1842866");

        // Act
        List<String> messages = this.validate(company);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Please enter a valid registration number."));
    }
    
    @Test
    public void testEmail_whenInvalid_shouldReturnConstraintViolations() {
        // Arrange
        Company company = new Company("Nestle", "123456789", 
                "invalid-email", "+359 88 1842866");

        // Act
        List<String> messages = this.validate(company);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Please provide a valid email address."));
    }
    
    @Test
    public void testPhoneNumber_whenInvalid_shouldReturnConstraintViolations() {
        // Assert
        Company company = new Company("Nestle", "123456789", 
                "someMail@mail.bg", "+123 456 789");

        // ACt
        List<String> messages = this.validate(company);

        // Assert
        assertEquals(1, messages.size());
        assertTrue(messages.contains("Phone number must be valid and can include an international prefix."));
    }
    
    @Test
    public void testAllFields_whenValid_shouldNotReturnConstraintViolations() {
        // Arrange
        Company company = new Company("Nestle", "123456789", 
                "someMail@mail.bg", "+359 88 1842866");

        // Act
        List<String> messages = this.validate(company);

        // Assert
        assertTrue(messages.isEmpty());
    }
    
    private List<String> validate(Company company) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(company)
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }
}
