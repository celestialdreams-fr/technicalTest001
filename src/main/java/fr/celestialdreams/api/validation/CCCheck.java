package fr.celestialdreams.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = CCCheckValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface CCCheck {
    String message() default "invalid country code";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String[] allowed() default {"FR","GP","MQ","GF","RE","YT","NC","PF","WF","PM","BL","MF","TF"};
}
