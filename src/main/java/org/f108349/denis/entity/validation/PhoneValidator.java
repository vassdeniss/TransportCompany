package org.f108349.denis.entity.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String> {
    @Override
    public void initialize(Phone constraintAnnotation) { }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches("^(?:\\+359|0)\\s?(?:87|88|89|98|99|70|43|32|2[1-9]|[1-9][2-9])\\s?\\d{3}\\s?\\d{4}$");
    }
}
