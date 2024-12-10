package org.f108349.denis.entity.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = RegistrationNumberValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RegistrationNumber {
     String message() default "Please enter a valid registration number.";
     
     Class<?>[] groups() default {};
     
     Class<? extends Payload>[] payload() default {};
}
