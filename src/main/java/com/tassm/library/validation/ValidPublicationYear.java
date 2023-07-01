package com.tassm.library.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.Year;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPublicationYear.Validator.class)
@Documented
public @interface ValidPublicationYear {
    String message() default "Invalid publication year";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<ValidPublicationYear, Integer> {
        @Override
        public boolean isValid(Integer publicationYear, ConstraintValidatorContext context) {
            if (publicationYear == null) {
                return true; // Allow null values, additional validation can be added if needed
            }

            int currentYear = Year.now().getValue();
            return publicationYear > 0 && publicationYear <= currentYear;
        }
    }
}
