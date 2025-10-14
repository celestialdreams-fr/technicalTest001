package fr.celestialdreams.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

public class MinAgeValidator implements ConstraintValidator<MinAge, LocalDate> {

    private int min;
    private int max;

    @Override
    public void initialize(MinAge constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        if (birthDate == null) {
            return false; 
        }
        LocalDate now = LocalDate.now(ZoneId.systemDefault());
        if (birthDate.isAfter(now)) {
            return false;
        }
        int years = Period.between(birthDate, now).getYears();
        return years >= min && years <= max;
    }
}
