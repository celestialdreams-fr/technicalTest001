package fr.celestialdreams.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = MinAgeValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface MinAge {
    String message() default "age must be at least {min} years and at most {max} years";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int min() default 18;
    int max() default 120;
}
