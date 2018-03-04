package nl.svendubbeld.fontys.validation.constraints;

import nl.svendubbeld.fontys.validation.validator.LabelOrLocationValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * The annotated class must contain either a label, coordinates, or both.
 */
@Documented
@Constraint(validatedBy = LabelOrLocationValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LabelOrLocation {

    String message() default "{nl.svendubbeld.fontys.validation.constraints.LabelOrLocation.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
