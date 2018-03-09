package nl.svendubbeld.fontys.test.unit.validation.validator;

import nl.svendubbeld.fontys.validation.constraints.MaxFloat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validates that a double is lower or equal to the specified minimum.
 */
public class MaxDoubleValidator implements ConstraintValidator<MaxFloat, Double> {

    private double max;

    @Override
    public void initialize(MaxFloat constraintAnnotation) {
        max = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return !(value - max > 0.00001);
    }
}
