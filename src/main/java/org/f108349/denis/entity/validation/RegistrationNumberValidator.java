package org.f108349.denis.entity.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RegistrationNumberValidator implements ConstraintValidator<RegistrationNumber, String> {
    @Override
    public void initialize(RegistrationNumber constraintAnnotation) { }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches("^\\d{9}$");
    }
}
