package org.f108349.denis.entity.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LicensePlateValidator implements ConstraintValidator<LicensePlate, String> {
    @Override
    public void initialize(LicensePlate constraintAnnotation) { }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches("^[A-Z]{1,2}\\d{4}[A-Z]{2}$");
    }
}
