package nl.svendubbeld.fontys.validation.constraints;

import nl.svendubbeld.fontys.test.unit.validation.validator.MaxDoubleValidator;
import nl.svendubbeld.fontys.test.unit.validation.validator.MaxFloatValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The annotated element must be a number whose value must be lower or
 * equal to the specified maximum.
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
@Constraint(validatedBy = {MaxFloatValidator.class, MaxDoubleValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Repeatable(MaxFloat.List.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxFloat {

    String message() default "{nl.svendubbeld.fontys.validation.constraints.MaxFloat.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     * @return value the element must be lower or equal to
     */
    double value();

    /**
     * Defines several {@link MaxFloat} annotations on the same element.
     *
     * @see MaxFloat
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    @interface List {

        MaxFloat[] value();
    }
}
