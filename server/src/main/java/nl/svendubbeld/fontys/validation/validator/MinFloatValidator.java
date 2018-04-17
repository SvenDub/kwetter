package nl.svendubbeld.fontys.validation.validator;

import nl.svendubbeld.fontys.validation.constraints.MinFloat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validates that a float is higher or equal to the specified minimum.
 */
public class MinFloatValidator implements ConstraintValidator<MinFloat, Float> {

    private double min;

    @Override
    public void initialize(MinFloat constraintAnnotation) {
        min = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Float value, ConstraintValidatorContext context) {
        return value == null || min - value <= 0.00001;

    }
}
