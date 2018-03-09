package nl.svendubbeld.fontys.validation.constraints;

import nl.svendubbeld.fontys.test.unit.validation.validator.MinDoubleValidator;
import nl.svendubbeld.fontys.test.unit.validation.validator.MinFloatValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The annotated element must be a number whose value must be higher or
 * equal to the specified minimum.
 * <p>
 * Supported types are:
 * <ul>
 *     <li>{@code Float} / {@code float}</li>
 *     <li>{@code Double} / {@code double}</li>
 * </ul>
 * <p>
 * {@code null} elements are considered valid.
 */
@Documented
@Constraint(validatedBy = {MinFloatValidator.class, MinDoubleValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Repeatable(MinFloat.List.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface MinFloat {

    String message() default "{nl.svendubbeld.fontys.validation.constraints.MinFloat.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     * @return value the element must be higher or equal to
     */
    double value();

    /**
     * Defines several {@link MinFloat} annotations on the same element.
     *
     * @see MinFloat
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    @interface List {

        MinFloat[] value();
    }
}
