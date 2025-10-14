package fr.celestialdreams.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class CCCheckValidator implements ConstraintValidator<CCCheck, String> {

    private Set<String> allowedSet;
    @Override
    public void initialize(CCCheck constraintAnnotation) {
        String[] allowed = constraintAnnotation.allowed();
        allowedSet = Arrays.stream(allowed)
                .filter(s -> s != null && !s.isBlank())
                .map(s -> s.toUpperCase(Locale.ROOT))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        if (allowedSet.isEmpty()) {
            return true;
        }
        return allowedSet.contains(value.toUpperCase(Locale.ROOT));
    }
}
