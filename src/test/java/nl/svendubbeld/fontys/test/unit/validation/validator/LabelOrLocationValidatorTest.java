package nl.svendubbeld.fontys.test.unit.validation.validator;

import nl.svendubbeld.fontys.model.Location;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.lang.reflect.Field;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class LabelOrLocationValidatorTest extends ValidatorTest {

    @Test
    public void testLatitudeNotSet() throws IllegalAccessException, NoSuchFieldException {
        Location location = new Location(1.0f, 2.0f);
        Field latitudeField = Location.class.getDeclaredField("latitude");
        latitudeField.setAccessible(true);
        latitudeField.set(location, null);

        Set<ConstraintViolation<Location>> violations = getValidator().validate(location);

        assertThat(violations, is(not(empty())));
    }

    @Test
    public void testLongitudeNotSet() throws IllegalAccessException, NoSuchFieldException {
        Location location = new Location(1.0f, 2.0f);
        Field longitudeField = Location.class.getDeclaredField("longitude");
        longitudeField.setAccessible(true);
        longitudeField.set(location, null);

        Set<ConstraintViolation<Location>> violations = getValidator().validate(location);

        assertThat(violations, is(not(empty())));
    }

    @Test
    public void testNothingSet() throws IllegalAccessException, NoSuchFieldException {
        Location location = new Location("label");
        Field labelField = Location.class.getDeclaredField("label");
        labelField.setAccessible(true);
        labelField.set(location, null);

        Set<ConstraintViolation<Location>> violations = getValidator().validate(location);

        assertThat(violations, is(not(empty())));
    }

    @Test
    public void testOnlyLabelSet() {
        Location location = new Location("label");

        Set<ConstraintViolation<Location>> violations = getValidator().validate(location);

        assertThat(violations, is(empty()));
    }

    @Test
    public void testLabelAndCoordinatesSet() {
        Location location = new Location("label", 1.0f, 2.0f);

        Set<ConstraintViolation<Location>> violations = getValidator().validate(location);

        assertThat(violations, is(empty()));
    }

    @Test
    public void testOnlyCoordinatesSet() {
        Location location = new Location(1.0f, 2.0f);

        Set<ConstraintViolation<Location>> violations = getValidator().validate(location);

        assertThat(violations, is(empty()));
    }
}
