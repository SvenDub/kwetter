package nl.svendubbeld.fontys.validation.validator;

import nl.svendubbeld.fontys.validation.constraints.MaxFloat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validates that a float is lower or equal to the specified minimum.
 */
public class MaxFloatValidator implements ConstraintValidator<MaxFloat, Float> {

    private double max;

    @Override
    public void initialize(MaxFloat constraintAnnotation) {
        max = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Float value, ConstraintValidatorContext context) {
        return value == null || value - max <= 0.00001;

    }
}
