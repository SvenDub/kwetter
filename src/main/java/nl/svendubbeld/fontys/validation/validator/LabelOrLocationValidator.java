package nl.svendubbeld.fontys.validation.validator;

import nl.svendubbeld.fontys.model.Location;
import nl.svendubbeld.fontys.validation.constraints.LabelOrLocation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validates a {@link Location} to contain either a label, coordinates, or both if annotated with {@link LabelOrLocation}.
 */
public class LabelOrLocationValidator implements ConstraintValidator<LabelOrLocation, Location> {

    @Override
    public boolean isValid(Location value, ConstraintValidatorContext context) {
        if (value.getLatitude().isPresent() && !value.getLongitude().isPresent()) {
            context.buildConstraintViolationWithTemplate("A longitude is required if the latitude has been set")
                    .addPropertyNode("longitude")
                    .addConstraintViolation();

            return false;
        }

        if (value.getLongitude().isPresent() && !value.getLatitude().isPresent()) {
            context.buildConstraintViolationWithTemplate("A latitude is required if the longitude has been set")
                    .addPropertyNode("latitude")
                    .addConstraintViolation();

            return false;
        }

        if (!value.getLatitude().isPresent() && !value.getLongitude().isPresent() && !value.getLabel().isPresent()) {
            context.buildConstraintViolationWithTemplate("Either a label or both latitude and longitude has to be set")
                    .addPropertyNode("latitude")
                    .addPropertyNode("longitude")
                    .addPropertyNode("label")
                    .addConstraintViolation();

            return false;
        }

        return true;
    }
}
